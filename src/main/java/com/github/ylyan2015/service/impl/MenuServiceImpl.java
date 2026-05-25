package com.github.ylyan2015.service.impl;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dao.MenuRepository;
import com.github.ylyan2015.dao.RoleMenuRepository;
import com.github.ylyan2015.dto.MenuDto;
import com.github.ylyan2015.dto.OperationLogDto;
import com.github.ylyan2015.entity.MenuEO;
import com.github.ylyan2015.entity.RoleMenuEO;
import com.github.ylyan2015.service.IMenuService;
import com.github.ylyan2015.service.IOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
@Slf4j
public class MenuServiceImpl implements IMenuService {

    @Resource
    private MenuRepository menuRepository;

    @Resource
    private RoleMenuRepository roleMenuRepository;

    @Resource
    private IOperationLogService operationLogService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<MenuDto> addMenu(MenuDto menuDto) {
        try {
            if (menuDto.getParentId() == null) {
                menuDto.setParentId(0L);
            }

            MenuEO menuEO = new MenuEO();
            BeanUtils.copyProperties(menuDto, menuEO);

            MenuEO savedMenu = menuRepository.save(menuEO);

            saveOperationLog(null, "菜单管理", "新增", "新增菜单：" + menuDto.getMenuName(), 
                    "POST", "/menu/add", menuDto, savedMenu, true, null);

            MenuDto result = convertToDto(savedMenu);
            return Result.success(result);
        } catch (Exception e) {
            log.error("新增菜单失败", e);
            saveOperationLog(null, "菜单管理", "新增", "新增菜单失败：" + menuDto.getMenuName(), 
                    "POST", "/menu/add", menuDto, null, false, e.getMessage());
            return Result.fail("新增菜单失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<MenuDto> updateMenu(MenuDto menuDto) {
        try {
            Optional<MenuEO> optional = menuRepository.findById(menuDto.getId());
            if (!optional.isPresent()) {
                return Result.fail("菜单不存在");
            }

            MenuEO menuEO = optional.get();
            BeanUtils.copyProperties(menuDto, menuEO, "id", "createTime");

            MenuEO updatedMenu = menuRepository.save(menuEO);

            saveOperationLog(null, "菜单管理", "修改", "修改菜单：" + menuDto.getMenuName(), 
                    "PUT", "/menu/update", menuDto, updatedMenu, true, null);

            MenuDto result = convertToDto(updatedMenu);
            return Result.success(result);
        } catch (Exception e) {
            log.error("修改菜单失败", e);
            saveOperationLog(null, "菜单管理", "修改", "修改菜单失败：" + menuDto.getMenuName(), 
                    "PUT", "/menu/update", menuDto, null, false, e.getMessage());
            return Result.fail("修改菜单失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteMenu(Long id) {
        try {
            Optional<MenuEO> optional = menuRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("菜单不存在");
            }

            String menuName = optional.get().getMenuName();

            List<RoleMenuEO> roleMenus = roleMenuRepository.findByMenuId(id);
            if (!roleMenus.isEmpty()) {
                return Result.fail("该菜单已被角色使用，无法删除");
            }

            List<MenuEO> childMenus = menuRepository.findByParentId(id);
            if (!childMenus.isEmpty()) {
                return Result.fail("该菜单存在子菜单，请先删除子菜单");
            }

            menuRepository.deleteById(id);

            saveOperationLog(null, "菜单管理", "删除", "删除菜单：" + menuName, 
                    "DELETE", "/menu/delete/" + id, null, menuName, true, null);

            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除菜单失败", e);
            saveOperationLog(null, "菜单管理", "删除", "删除菜单失败，id：" + id, 
                    "DELETE", "/menu/delete/" + id, null, null, false, e.getMessage());
            return Result.fail("删除菜单失败：" + e.getMessage());
        }
    }

    @Override
    public Result<MenuDto> getMenuById(Long id) {
        try {
            Optional<MenuEO> optional = menuRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("菜单不存在");
            }

            MenuDto menuDto = convertToDto(optional.get());
            return Result.success(menuDto);
        } catch (Exception e) {
            log.error("查询菜单失败", e);
            return Result.fail("查询菜单失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<MenuDto>> getAllMenus() {
        try {
            List<MenuEO> menus = menuRepository.findAll();
            List<MenuDto> menuDtos = menus.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(menuDtos);
        } catch (Exception e) {
            log.error("查询菜单列表失败", e);
            return Result.fail("查询菜单列表失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<MenuDto>> getMenuTree() {
        try {
            List<MenuEO> allMenus = menuRepository.findAll();
            List<MenuDto> allMenuDtos = allMenus.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            List<MenuDto> tree = buildMenuTree(allMenuDtos, 0L);
            return Result.success(tree);
        } catch (Exception e) {
            log.error("查询菜单树失败", e);
            return Result.fail("查询菜单树失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<MenuDto>> getMenusByParentId(Long parentId) {
        try {
            List<MenuEO> menus = menuRepository.findByParentId(parentId);
            List<MenuDto> menuDtos = menus.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(menuDtos);
        } catch (Exception e) {
            log.error("查询子菜单失败", e);
            return Result.fail("查询子菜单失败：" + e.getMessage());
        }
    }

    /**
     * 构建菜单树
     */
    private List<MenuDto> buildMenuTree(List<MenuDto> allMenus, Long parentId) {
        List<MenuDto> tree = new ArrayList<>();
        for (MenuDto menu : allMenus) {
            if (parentId.equals(menu.getParentId())) {
                List<MenuDto> children = buildMenuTree(allMenus, menu.getId());
                if (!children.isEmpty()) {
                    menu.setChildren(children);
                }
                tree.add(menu);
            }
        }
        return tree;
    }

    /**
     * Entity转Dto
     */
    private MenuDto convertToDto(MenuEO menuEO) {
        MenuDto menuDto = new MenuDto();
        BeanUtils.copyProperties(menuEO, menuDto);
        return menuDto;
    }

    /**
     * 保存操作日志
     */
    private void saveOperationLog(Long userId, String module, String operationType, String description,
                                  String requestMethod, String requestUrl, Object requestParams,
                                  Object responseResult, boolean success, String errorMsg) {
        try {
            OperationLogDto logDto = new OperationLogDto();
            logDto.setUserId(userId != null ? userId : 0L);
            logDto.setUsername("system");
            logDto.setModule(module);
            logDto.setOperationType(operationType);
            logDto.setDescription(description);
            logDto.setRequestMethod(requestMethod);
            logDto.setRequestUrl(requestUrl);
            logDto.setRequestParams(requestParams != null ? com.alibaba.fastjson.JSON.toJSONString(requestParams) : null);
            logDto.setResponseResult(responseResult != null ? com.alibaba.fastjson.JSON.toJSONString(responseResult) : null);
            logDto.setStatus(success ? 1 : 0);
            logDto.setErrorMsg(errorMsg);
            operationLogService.saveOperationLog(logDto);
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }
    }
}
