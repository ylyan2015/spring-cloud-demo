package com.github.ylyan2015.springbootdemo.service;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.dto.MenuDto;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface IMenuService {

    /**
     * 新增菜单
     */
    Result<MenuDto> addMenu(MenuDto menuDto);

    /**
     * 修改菜单
     */
    Result<MenuDto> updateMenu(MenuDto menuDto);

    /**
     * 删除菜单
     */
    Result<String> deleteMenu(Long id);

    /**
     * 查询单个菜单
     */
    Result<MenuDto> getMenuById(Long id);

    /**
     * 查询所有菜单
     */
    Result<List<MenuDto>> getAllMenus();

    /**
     * 查询菜单树
     */
    Result<List<MenuDto>> getMenuTree();

    /**
     * 根据父菜单ID查询
     */
    Result<List<MenuDto>> getMenusByParentId(Long parentId);
}
