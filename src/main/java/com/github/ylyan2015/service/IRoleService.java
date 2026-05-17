
package com.github.ylyan2015.service;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dto.RoleDto;

import java.util.List;

/**
 * 角色服务接口
 */
public interface IRoleService {

    /**
     * 新增角色
     */
    Result<RoleDto> addRole(RoleDto roleDto);

    /**
     * 修改角色
     */
    Result<RoleDto> updateRole(RoleDto roleDto);

    /**
     * 删除角色
     */
    Result<String> deleteRole(Long id);

    /**
     * 根据ID查询角色
     */
    Result<RoleDto> getRoleById(Long id);

    /**
     * 查询所有角色
     */
    Result<List<RoleDto>> getAllRoles();

    /**
     * 为角色分配菜单
     */
    Result<String> assignMenusToRole(Long roleId, List<Long> menuIds);

    /**
     * 获取角色的菜单ID列表
     */
    Result<List<Long>> getRoleMenuIds(Long roleId);
}