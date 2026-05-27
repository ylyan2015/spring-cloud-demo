package com.github.ylyan2015.controller;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.MenuDto;
import com.github.ylyan2015.service.IMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单控制器
 */
@Tag(name = "菜单管理", description = "菜单增删改查、菜单树等相关接口")
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    /**
     * 新增菜单
     */
    @Operation(summary = "新增菜单", description = "创建新菜单，支持多级菜单")
    @PostMapping("/add")
    public Result<MenuDto> addMenu(@RequestBody MenuDto menuDto) {
        return menuService.addMenu(menuDto);
    }

    /**
     * 修改菜单
     */
    @Operation(summary = "修改菜单", description = "更新菜单信息")
    @PutMapping("/update")
    public Result<MenuDto> updateMenu(@RequestBody MenuDto menuDto) {
        return menuService.updateMenu(menuDto);
    }

    /**
     * 删除菜单
     */
    @Operation(summary = "删除菜单", description = "根据ID删除菜单，会检查是否有子菜单或角色关联")
    @Parameter(name = "id", description = "菜单ID", required = true, example = "1")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteMenu(@PathVariable Long id) {
        return menuService.deleteMenu(id);
    }

    /**
     * 查询单个菜单
     */
    @Operation(summary = "查询菜单详情", description = "根据ID查询菜单详细信息")
    @Parameter(name = "id", description = "菜单ID", required = true, example = "1")
    @GetMapping("/get/{id}")
    public Result<MenuDto> getMenu(@PathVariable Long id) {
        return menuService.getMenuById(id);
    }

    /**
     * 查询所有菜单
     */
    @Operation(summary = "查询菜单列表", description = "查询所有菜单信息")
    @GetMapping("/list")
    public Result<List<MenuDto>> getAllMenus() {
        return menuService.getAllMenus();
    }

    /**
     * 查询菜单树
     */
    @Operation(summary = "查询菜单树", description = "以树形结构返回所有菜单，适合前端展示")
    @GetMapping("/tree")
    public Result<List<MenuDto>> getMenuTree() {
        return menuService.getMenuTree();
    }

    /**
     * 根据父菜单ID查询
     */
    @Operation(summary = "查询子菜单", description = "根据父菜单ID查询子菜单列表")
    @Parameter(name = "parentId", description = "父菜单ID，0表示查询根菜单", required = true, example = "0")
    @GetMapping("/children/{parentId}")
    public Result<List<MenuDto>> getMenusByParentId(@PathVariable Long parentId) {
        return menuService.getMenusByParentId(parentId);
    }
}
