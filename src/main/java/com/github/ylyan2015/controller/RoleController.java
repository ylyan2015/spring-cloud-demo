
package com.github.ylyan2015.controller;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.RoleDto;
import com.github.ylyan2015.service.IRoleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 新增角色
     */
    @PostMapping("/add")
    public Result<RoleDto> addRole(@RequestBody RoleDto roleDto) {
        return roleService.addRole(roleDto);
    }

    /**
     * 修改角色
     */
    @PutMapping("/update")
    public Result<RoleDto> updateRole(@RequestBody RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    /**
     * 查询单个角色
     */
    @GetMapping("/get/{id}")
    public Result<RoleDto> getRole(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    /**
     * 查询所有角色
     */
    @GetMapping("/list")
    public Result<List<RoleDto>> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * 为角色分配菜单
     */
    @PostMapping("/assign-menus")
    public Result<String> assignMenus(@RequestParam Long roleId, @RequestBody List<Long> menuIds) {
        return roleService.assignMenusToRole(roleId, menuIds);
    }

    /**
     * 获取角色的菜单ID列表
     */
    @GetMapping("/menu-ids/{roleId}")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        return roleService.getRoleMenuIds(roleId);
    }
}