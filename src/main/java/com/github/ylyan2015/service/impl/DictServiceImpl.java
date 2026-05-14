package com.github.ylyan2015.service.impl;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dao.DictItemRepository;
import com.github.ylyan2015.dao.DictRepository;
import com.github.ylyan2015.dto.DictDto;
import com.github.ylyan2015.dto.DictItemDto;
import com.github.ylyan2015.entity.DictEO;
import com.github.ylyan2015.entity.DictItemEO;
import com.github.ylyan2015.service.IDictService;
import com.github.ylyan2015.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 数据字典服务实现类
 */
@Service
@Slf4j
public class DictServiceImpl implements IDictService {

    @Resource
    private DictRepository dictRepository;

    @Resource
    private DictItemRepository dictItemRepository;

    @Resource
    private RedisUtil redisUtil;

    private static final String DICT_CACHE_PREFIX = "dict:";
    private static final long CACHE_EXPIRE_TIME = 3600;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<DictDto> addDict(DictDto dictDto) {
        try {
            Optional<DictEO> existDict = dictRepository.findByDictCode(dictDto.getDictCode());
            if (existDict.isPresent()) {
                return Result.fail("字典编码已存在");
            }

            DictEO dictEO = new DictEO();
            BeanUtils.copyProperties(dictDto, dictEO);

            if (dictEO.getStatus() == null) {
                dictEO.setStatus(1);
            }

            DictEO savedDict = dictRepository.save(dictEO);

            DictDto result = convertToDto(savedDict);
            return Result.success(result);
        } catch (Exception e) {
            log.error("新增字典失败", e);
            return Result.fail("新增字典失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<DictDto> updateDict(DictDto dictDto) {
        try {
            Optional<DictEO> optional = dictRepository.findById(dictDto.getId());
            if (!optional.isPresent()) {
                return Result.fail("字典不存在");
            }

            DictEO dictEO = optional.get();
            BeanUtils.copyProperties(dictDto, dictEO, "id", "createTime");

            DictEO updatedDict = dictRepository.save(dictEO);

            clearDictCache(updatedDict.getDictCode());

            DictDto result = convertToDto(updatedDict);
            return Result.success(result);
        } catch (Exception e) {
            log.error("修改字典失败", e);
            return Result.fail("修改字典失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteDict(Long id) {
        try {
            Optional<DictEO> optional = dictRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("字典不存在");
            }

            DictEO dictEO = optional.get();
            String dictCode = dictEO.getDictCode();

            dictItemRepository.deleteByDictId(id);
            dictRepository.deleteById(id);

            clearDictCache(dictCode);

            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除字典失败", e);
            return Result.fail("删除字典失败：" + e.getMessage());
        }
    }

    @Override
    public Result<DictDto> getDictById(Long id) {
        try {
            Optional<DictEO> optional = dictRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("字典不存在");
            }

            DictDto dictDto = convertToDto(optional.get());
            return Result.success(dictDto);
        } catch (Exception e) {
            log.error("查询字典失败", e);
            return Result.fail("查询字典失败：" + e.getMessage());
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
            if (!optional.isPresent()) {
                return Result.fail("字典不存在");
            }

            DictEO dictEO = optional.get();
            DictDto dictDto = convertToDto(dictEO);

            List<DictItemEO> dictItems = dictItemRepository.findByDictId(dictEO.getId());
            if (!dictItems.isEmpty()) {
                List<DictItemDto> itemDtos = dictItems.stream()
                        .filter(item -> item.getStatus() == 1)
                        .map(this::convertToItemDto)
                        .collect(Collectors.toList());
                dictDto.setItems(itemDtos);
            }

            redisUtil.set(cacheKey, dictDto, CACHE_EXPIRE_TIME);

            return Result.success(dictDto);
        } catch (Exception e) {
            log.error("查询字典失败", e);
            return Result.fail("查询字典失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<DictDto>> getAllDicts() {
        try {
            List<DictEO> dicts = dictRepository.findAll();
            List<DictDto> dictDtos = dicts.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(dictDtos);
        } catch (Exception e) {
            log.error("查询字典列表失败", e);
            return Result.fail("查询字典列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> toggleDictStatus(Long id, Integer status) {
        try {
            Optional<DictEO> optional = dictRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("字典不存在");
            }

            if (status != 0 && status != 1) {
                return Result.fail("状态值不正确");
            }

            DictEO dictEO = optional.get();
            dictEO.setStatus(status);
            dictRepository.save(dictEO);

            clearDictCache(dictEO.getDictCode());

            String statusText = status == 1 ? "启用" : "禁用";
            return Result.success("字典已" + statusText);
        } catch (Exception e) {
            log.error("切换字典状态失败", e);
            return Result.fail("切换字典状态失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<DictItemDto> addDictItem(DictItemDto dictItemDto) {
        try {
            Optional<DictEO> dictOptional = dictRepository.findById(dictItemDto.getDictId());
            if (!dictOptional.isPresent()) {
                return Result.fail("字典不存在");
            }

            DictItemEO dictItemEO = new DictItemEO();
            BeanUtils.copyProperties(dictItemDto, dictItemEO);

            if (dictItemEO.getStatus() == null) {
                dictItemEO.setStatus(1);
            }

            if (dictItemEO.getSortOrder() == null) {
                dictItemEO.setSortOrder(0);
            }

            DictItemEO savedItem = dictItemRepository.save(dictItemEO);

            clearDictCacheByDictId(dictItemEO.getDictId());

            DictItemDto result = convertToItemDto(savedItem);
            return Result.success(result);
        } catch (Exception e) {
            log.error("新增字典项失败", e);
            return Result.fail("新增字典项失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<DictItemDto> updateDictItem(DictItemDto dictItemDto) {
        try {
            Optional<DictItemEO> optional = dictItemRepository.findById(dictItemDto.getId());
            if (!optional.isPresent()) {
                return Result.fail("字典项不存在");
            }

            DictItemEO dictItemEO = optional.get();
            BeanUtils.copyProperties(dictItemDto, dictItemEO, "id", "createTime");

            DictItemEO updatedItem = dictItemRepository.save(dictItemEO);

            clearDictCacheByDictId(dictItemEO.getDictId());

            DictItemDto result = convertToItemDto(updatedItem);
            return Result.success(result);
        } catch (Exception e) {
            log.error("修改字典项失败", e);
            return Result.fail("修改字典项失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteDictItem(Long id) {
        try {
            Optional<DictItemEO> optional = dictItemRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("字典项不存在");
            }

            DictItemEO dictItemEO = optional.get();
            Long dictId = dictItemEO.getDictId();

            dictItemRepository.deleteById(id);

            clearDictCacheByDictId(dictId);

            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除字典项失败", e);
            return Result.fail("删除字典项失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<DictItemDto>> getDictItemsByDictId(Long dictId) {
        try {
            Optional<DictEO> dictOptional = dictRepository.findById(dictId);
            if (!dictOptional.isPresent()) {
                return Result.fail("字典不存在");
            }

            List<DictItemEO> dictItems = dictItemRepository.findByDictId(dictId);
            List<DictItemDto> itemDtos = dictItems.stream()
                    .map(this::convertToItemDto)
                    .collect(Collectors.toList());

            return Result.success(itemDtos);
        } catch (Exception e) {
            log.error("查询字典项列表失败", e);
            return Result.fail("查询字典项列表失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> toggleDictItemStatus(Long id, Integer status) {
        try {
            Optional<DictItemEO> optional = dictItemRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("字典项不存在");
            }

            if (status != 0 && status != 1) {
                return Result.fail("状态值不正确");
            }

            DictItemEO dictItemEO = optional.get();
            dictItemEO.setStatus(status);
            dictItemRepository.save(dictItemEO);

            clearDictCacheByDictId(dictItemEO.getDictId());

            String statusText = status == 1 ? "启用" : "禁用";
            return Result.success("字典项已" + statusText);
        } catch (Exception e) {
            log.error("切换字典项状态失败", e);
            return Result.fail("切换字典项状态失败：" + e.getMessage());
        }
    }

    /**
     * Entity转Dto
     */
    private DictDto convertToDto(DictEO dictEO) {
        DictDto dictDto = new DictDto();
        BeanUtils.copyProperties(dictEO, dictDto);
        return dictDto;
    }

    /**
     * Item Entity转Dto
     */
    private DictItemDto convertToItemDto(DictItemEO dictItemEO) {
        DictItemDto dictItemDto = new DictItemDto();
        BeanUtils.copyProperties(dictItemEO, dictItemDto);
        return dictItemDto;
    }

    /**
     * 清除字典缓存
     */
    private void clearDictCache(String dictCode) {
        String cacheKey = DICT_CACHE_PREFIX + dictCode;
        redisUtil.del(cacheKey);
    }

    /**
     * 根据字典ID清除缓存
     */
    private void clearDictCacheByDictId(Long dictId) {
        Optional<DictEO> dictOptional = dictRepository.findById(dictId);
        if (dictOptional.isPresent()) {
            clearDictCache(dictOptional.get().getDictCode());
        }
    }
}
