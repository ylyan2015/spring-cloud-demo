package com.github.ylyan2015.springbootdemo.controller;

import com.github.ylyan2015.springbootdemo.common.Result;
import com.github.ylyan2015.springbootdemo.dto.RoleDto;
import com.github.ylyan2015.springbootdemo.service.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 角色控制器
 */
@Tag(name = "角色管理", description = "角色增删改查、菜单分配等相关接口")
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色", description = "创建新角色")
    @PostMapping("/add")
    public Result<RoleDto> addRole(@Valid @RequestBody RoleDto roleDto) {
        return roleService.addRole(roleDto);
    }

    /**
     * 修改角色
     */
    @Operation(summary = "修改角色", description = "更新角色信息")
    @PutMapping("/update")
    public Result<RoleDto> updateRole(@Valid @RequestBody RoleDto roleDto) {
        return roleService.updateRole(roleDto);
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "根据ID删除角色，会检查是否有用户关联")
    @Parameter(name = "id", description = "角色ID", required = true, example = "1")
    @DeleteMapping("/delete/{id}")
    public Result<String> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }

    /**
     * 查询单个角色
     */
    @Operation(summary = "查询角色详情", description = "根据ID查询角色详细信息")
    @Parameter(name = "id", description = "角色ID", required = true, example = "1")
    @GetMapping("/get/{id}")
    public Result<RoleDto> getRole(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    /**
     * 查询所有角色
     */
    @Operation(summary = "查询角色列表", description = "查询所有角色信息")
    @GetMapping("/list")
    public Result<List<RoleDto>> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * 为角色分配菜单
     */
    @Operation(summary = "分配菜单", description = "为角色分配一个或多个菜单权限")
    @Parameter(name = "roleId", description = "角色ID", required = true, example = "1")
    @PostMapping("/assign-menus")
    public Result<String> assignMenus(@RequestParam Long roleId, @RequestBody List<Long> menuIds) {
        return roleService.assignMenusToRole(roleId, menuIds);
    }

    /**
     * 获取角色的菜单ID列表
     */
    @Operation(summary = "查询角色菜单", description = "获取角色已分配的菜单ID列表")
    @Parameter(name = "roleId", description = "角色ID", required = true, example = "1")
    @GetMapping("/menu-ids/{roleId}")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        return roleService.getRoleMenuIds(roleId);
    }
}