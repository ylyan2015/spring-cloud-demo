package com.github.ylyan2015.springbootdemo.service.impl;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.common.SystemConstants;
import com.github.ylyan2015.springbootdemo.common.UserStatusEnum;
import com.github.ylyan2015.springbootdemo.dto.DictDto;
import com.github.ylyan2015.springbootdemo.dto.DictItemDto;
import com.github.ylyan2015.springbootdemo.entity.DictEO;
import com.github.ylyan2015.springbootdemo.entity.DictItemEO;
import com.github.ylyan2015.springbootdemo.repository.DictItemRepository;
import com.github.ylyan2015.springbootdemo.repository.DictRepository;
import com.github.ylyan2015.springbootdemo.service.IDictService;
import com.github.ylyan2015.springbootdemo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DictServiceImpl implements IDictService {

    private static final String DICT_CACHE_PREFIX = "dict:";
    private static final long CACHE_EXPIRE_TIME = SystemConstants.TimeSeconds.TWO_HOURS;

    @Resource
    private DictRepository dictRepository;

    @Resource
    private DictItemRepository dictItemRepository;

    @Resource
    private RedisUtil redisUtil;

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<DictDto> addDict(DictDto dictDto) {
        try {
            Optional<DictEO> existDict = dictRepository.findByDictCode(dictDto.getDictCode());
            if (existDict.isPresent()) {
                return Result.fail(SystemConstants.MSG_DICT_ALREADY_EXISTS);
            }

            DictEO dictEO = new DictEO();
            BeanUtils.copyProperties(dictDto, dictEO);

            if (dictEO.getStatus() == null) {
                dictEO.setStatus(UserStatusEnum.ENABLED);
            }

            DictEO savedDict = dictRepository.save(dictEO);

            DictDto result = toDto(savedDict);
            return Result.success(result);
        } catch (Exception e) {
            log.error("新增字典失败", e);
            return Result.fail("新增字典失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<DictDto> updateDict(DictDto dictDto) {
        try {
            Optional<DictEO> optional = dictRepository.findById(dictDto.getId());
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_NOT_FOUND);
            }

            DictEO dictEO = optional.get();
            BeanUtils.copyProperties(dictDto, dictEO, "id", "createTime");

            DictEO updatedDict = dictRepository.save(dictEO);

            clearDictCache(updatedDict.getDictCode());

            DictDto result = toDto(updatedDict);
            return Result.success(result);
        } catch (Exception e) {
            log.error("修改字典失败", e);
            return Result.fail("修改字典失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<String> deleteDict(Long id) {
        try {
            Optional<DictEO> optional = dictRepository.findById(id);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_NOT_FOUND);
            }

            DictEO dictEO = optional.get();
            String dictCode = dictEO.getDictCode();

            dictItemRepository.deleteByDictId(id);
            dictRepository.deleteById(id);

            clearDictCache(dictCode);

            return Result.success(SystemConstants.MSG_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error("删除字典失败", e);
            return Result.fail("删除字典失败，请联系管理员");
        }
    }

    @Override
    public Result<DictDto> getDictById(Long id) {
        try {
            Optional<DictEO> optional = dictRepository.findById(id);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_NOT_FOUND);
            }

            DictDto dictDto = toDto(optional.get());
            return Result.success(dictDto);
        } catch (Exception e) {
            log.error("查询字典失败", e);
            return Result.fail("查询字典失败，请联系管理员");
        }
    }

    @Override
    public Result<DictDto> getDictByCode(String dictCode) {
        try {
            String cacheKey = DICT_CACHE_PREFIX + dictCode;

            if (redisUtil.hasKey(cacheKey)) {
                Object cached = redisUtil.get(cacheKey);
                if (cached instanceof DictDto) {
                    return Result.success((DictDto) cached);
                }
            }

            Optional<DictEO> optional = dictRepository.findByDictCode(dictCode);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_NOT_FOUND);
            }

            DictEO dictEO = optional.get();
            DictDto dictDto = toDto(dictEO);

            List<DictItemEO> dictItems = dictItemRepository.findByDictId(dictEO.getId());
            if (!dictItems.isEmpty()) {
                List<DictItemDto> itemDtos = dictItems.stream()
                        .filter(item -> item.getStatus() == UserStatusEnum.ENABLED)
                        .map(this::toItemDto)
                        .collect(Collectors.toList());
                dictDto.setItems(itemDtos);
            }

            redisUtil.set(cacheKey, dictDto, CACHE_EXPIRE_TIME);

            return Result.success(dictDto);
        } catch (Exception e) {
            log.error("查询字典失败", e);
            return Result.fail("查询字典失败，请联系管理员");
        }
    }

    @Override
    public Result<List<DictDto>> getAllDicts() {
        try {
            List<DictEO> dicts = dictRepository.findAll();
            List<DictDto> dictDtos = dicts.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            return Result.success(dictDtos);
        } catch (Exception e) {
            log.error("查询字典列表失败", e);
            return Result.fail("查询字典列表失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<String> toggleDictStatus(Long id, Integer status) {
        try {
            Optional<DictEO> optional = dictRepository.findById(id);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_NOT_FOUND);
            }

            UserStatusEnum statusEnum;
            try {
                statusEnum = UserStatusEnum.fromCode(status);
            } catch (IllegalArgumentException e) {
                return Result.fail("状态值不正确");
            }

            DictEO dictEO = optional.get();
            dictEO.setStatus(statusEnum);
            dictRepository.save(dictEO);

            clearDictCache(dictEO.getDictCode());

            String statusText = statusEnum.getDescription();
            return Result.success("字典已" + statusText);
        } catch (Exception e) {
            log.error("切换字典状态失败", e);
            return Result.fail("切换字典状态失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<DictItemDto> addDictItem(DictItemDto dictItemDto) {
        try {
            Optional<DictEO> dictOptional = dictRepository.findById(dictItemDto.getDictId());
            if (dictOptional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_NOT_FOUND);
            }

            DictItemEO dictItemEO = new DictItemEO();
            BeanUtils.copyProperties(dictItemDto, dictItemEO);

            if (dictItemEO.getStatus() == null) {
                dictItemEO.setStatus(UserStatusEnum.ENABLED);
            }

            if (dictItemEO.getSortOrder() == null) {
                dictItemEO.setSortOrder(0);
            }

            DictItemEO savedItem = dictItemRepository.save(dictItemEO);

            clearDictCacheByDictId(dictItemEO.getDictId());

            DictItemDto result = toItemDto(savedItem);
            return Result.success(result);
        } catch (Exception e) {
            log.error("新增字典项失败", e);
            return Result.fail("新增字典项失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<DictItemDto> updateDictItem(DictItemDto dictItemDto) {
        try {
            Optional<DictItemEO> optional = dictItemRepository.findById(dictItemDto.getId());
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_ITEM_NOT_FOUND);
            }

            DictItemEO dictItemEO = optional.get();
            BeanUtils.copyProperties(dictItemDto, dictItemEO, "id", "createTime");

            DictItemEO updatedItem = dictItemRepository.save(dictItemEO);

            clearDictCacheByDictId(dictItemEO.getDictId());

            DictItemDto result = toItemDto(updatedItem);
            return Result.success(result);
        } catch (Exception e) {
            log.error("修改字典项失败", e);
            return Result.fail("修改字典项失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<String> deleteDictItem(Long id) {
        try {
            Optional<DictItemEO> optional = dictItemRepository.findById(id);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_ITEM_NOT_FOUND);
            }

            DictItemEO dictItemEO = optional.get();
            Long dictId = dictItemEO.getDictId();

            dictItemRepository.deleteById(id);

            clearDictCacheByDictId(dictId);

            return Result.success(SystemConstants.MSG_DELETE_SUCCESS);
        } catch (Exception e) {
            log.error("删除字典项失败", e);
            return Result.fail("删除字典项失败，请联系管理员");
        }
    }

    @Override
    public Result<List<DictItemDto>> getDictItemsByDictId(Long dictId) {
        try {
            Optional<DictEO> dictOptional = dictRepository.findById(dictId);
            if (dictOptional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_NOT_FOUND);
            }

            List<DictItemEO> dictItems = dictItemRepository.findByDictId(dictId);
            List<DictItemDto> itemDtos = dictItems.stream()
                    .map(this::toItemDto)
                    .collect(Collectors.toList());

            return Result.success(itemDtos);
        } catch (Exception e) {
            log.error("查询字典项列表失败", e);
            return Result.fail("查询字典项列表失败，请联系管理员");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public Result<String> toggleDictItemStatus(Long id, Integer status) {
        try {
            Optional<DictItemEO> optional = dictItemRepository.findById(id);
            if (optional.isEmpty()) {
                return Result.fail(SystemConstants.MSG_DICT_ITEM_NOT_FOUND);
            }

            UserStatusEnum statusEnum;
            try {
                statusEnum = UserStatusEnum.fromCode(status);
            } catch (IllegalArgumentException e) {
                return Result.fail("状态值不正确");
            }

            DictItemEO dictItemEO = optional.get();
            dictItemEO.setStatus(statusEnum);
            dictItemRepository.save(dictItemEO);

            clearDictCacheByDictId(dictItemEO.getDictId());

            String statusText = statusEnum.getDescription();
            return Result.success("字典项已" + statusText);
        } catch (Exception e) {
            log.error("切换字典项状态失败", e);
            return Result.fail("切换字典项状态失败，请联系管理员");
        }
    }

    private DictDto toDto(DictEO dictEO) {
        DictDto dictDto = new DictDto();
        BeanUtils.copyProperties(dictEO, dictDto);
        return dictDto;
    }

    private DictItemDto toItemDto(DictItemEO dictItemEO) {
        DictItemDto dictItemDto = new DictItemDto();
        BeanUtils.copyProperties(dictItemEO, dictItemDto);
        return dictItemDto;
    }

    private void clearDictCache(String dictCode) {
        String cacheKey = DICT_CACHE_PREFIX + dictCode;
        redisUtil.del(cacheKey);
    }

    private void clearDictCacheByDictId(Long dictId) {
        Optional<DictEO> dictOptional = dictRepository.findById(dictId);
        if (dictOptional.isPresent()) {
            clearDictCache(dictOptional.get().getDictCode());
        }
    }
}
