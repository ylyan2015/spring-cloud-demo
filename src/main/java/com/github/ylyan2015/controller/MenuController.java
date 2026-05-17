package com.github.ylyan2015.controller;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.MenuDto;
import com.github.ylyan2015.service.IMenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    public Result<MenuDto> addMenu(@RequestBody MenuDto menuDto) {
        return menuService.addMenu(menuDto);
    }

    /**
     * 修改菜单
     */
    @PutMapping("/update")
    public Result<MenuDto> updateMenu(@RequestBody MenuDto menuDto) {
        return menuService.updateMenu(menuDto);
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteMenu(@PathVariable Long id) {
        return menuService.deleteMenu(id);
    }

    /**
     * 查询单个菜单
     */
    @GetMapping("/get/{id}")
    public Result<MenuDto> getMenu(@PathVariable Long id) {
        return menuService.getMenuById(id);
    }

    /**
     * 查询所有菜单
     */
    @GetMapping("/list")
    public Result<List<MenuDto>> getAllMenus() {
        return menuService.getAllMenus();
    }

    /**
     * 查询菜单树
     */
    @GetMapping("/tree")
    public Result<List<MenuDto>> getMenuTree() {
        return menuService.getMenuTree();
    }

    /**
     * 根据父菜单ID查询
     */
    @GetMapping("/children/{parentId}")
    public Result<List<MenuDto>> getMenusByParentId(@PathVariable Long parentId) {
        return menuService.getMenusByParentId(parentId);
    }
}
