
package com.github.ylyan2015.service.impl;

import com.github.ylyan2015.common.Result;
import com.github.ylyan2015.dao.RoleRepository;
import com.github.ylyan2015.dao.UserRoleRepository;
import com.github.ylyan2015.dao.RoleMenuRepository;
import com.github.ylyan2015.dto.RoleDto;
import com.github.ylyan2015.entity.RoleEO;
import com.github.ylyan2015.entity.UserRoleEO;
import com.github.ylyan2015.entity.RoleMenuEO;
import com.github.ylyan2015.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
@Slf4j
public class RoleServiceImpl implements IRoleService {

    @Resource
    private RoleRepository roleRepository;

    @Resource
    private UserRoleRepository userRoleRepository;

    @Resource
    private RoleMenuRepository roleMenuRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<RoleDto> addRole(RoleDto roleDto) {
        try {
            Optional<RoleEO> existing = roleRepository.findByRoleCode(roleDto.getRoleCode());
            if (existing.isPresent()) {
                return Result.fail("角色编码已存在");
            }

            RoleEO roleEO = new RoleEO();
            BeanUtils.copyProperties(roleDto, roleEO);

            RoleEO savedRole = roleRepository.save(roleEO);

            RoleDto result = convertToDto(savedRole);
            return Result.success(result);
        } catch (Exception e) {
            log.error("新增角色失败", e);
            return Result.fail("新增角色失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<RoleDto> updateRole(RoleDto roleDto) {
        try {
            Optional<RoleEO> optional = roleRepository.findById(roleDto.getId());
            if (!optional.isPresent()) {
                return Result.fail("角色不存在");
            }

            RoleEO roleEO = optional.get();
            BeanUtils.copyProperties(roleDto, roleEO, "id", "createTime");

            RoleEO updatedRole = roleRepository.save(roleEO);

            RoleDto result = convertToDto(updatedRole);
            return Result.success(result);
        } catch (Exception e) {
            log.error("修改角色失败", e);
            return Result.fail("修改角色失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteRole(Long id) {
        try {
            Optional<RoleEO> optional = roleRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("角色不存在");
            }

            List<UserRoleEO> userRoles = userRoleRepository.findByRoleId(id);
            if (!userRoles.isEmpty()) {
                return Result.fail("该角色已被用户使用，无法删除");
            }

            roleRepository.deleteById(id);

            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除角色失败", e);
            return Result.fail("删除角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<RoleDto> getRoleById(Long id) {
        try {
            Optional<RoleEO> optional = roleRepository.findById(id);
            if (!optional.isPresent()) {
                return Result.fail("角色不存在");
            }

            RoleDto roleDto = convertToDto(optional.get());
            return Result.success(roleDto);
        } catch (Exception e) {
            log.error("查询角色失败", e);
            return Result.fail("查询角色失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<RoleDto>> getAllRoles() {
        try {
            List<RoleEO> roles = roleRepository.findAll();
            List<RoleDto> roleDtos = roles.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return Result.success(roleDtos);
        } catch (Exception e) {
            log.error("查询角色列表失败", e);
            return Result.fail("查询角色列表失败：" + e.getMessage());
        }
    }

    /**
     * Entity转Dto
     */
    private RoleDto convertToDto(RoleEO roleEO) {
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(roleEO, roleDto);
        return roleDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> assignMenusToRole(Long roleId, List<Long> menuIds) {
        try {
            Optional<RoleEO> optional = roleRepository.findById(roleId);
            if (!optional.isPresent()) {
                return Result.fail("角色不存在");
            }

            roleMenuRepository.deleteByRoleId(roleId);

            if (menuIds != null && !menuIds.isEmpty()) {
                List<RoleMenuEO> roleMenus = menuIds.stream()
                        .map(menuId -> {
                            RoleMenuEO roleMenu = new RoleMenuEO();
                            roleMenu.setRoleId(roleId);
                            roleMenu.setMenuId(menuId);
                            return roleMenu;
                        })
                        .collect(Collectors.toList());
                roleMenuRepository.saveAll(roleMenus);
            }

            return Result.success("分配菜单成功");
        } catch (Exception e) {
            log.error("分配菜单失败", e);
            return Result.fail("分配菜单失败：" + e.getMessage());
        }
    }

    @Override
    public Result<List<Long>> getRoleMenuIds(Long roleId) {
        try {
            Optional<RoleEO> optional = roleRepository.findById(roleId);
            if (!optional.isPresent()) {
                return Result.fail("角色不存在");
            }

            List<RoleMenuEO> roleMenus = roleMenuRepository.findByRoleId(roleId);
            List<Long> menuIds = roleMenus.stream()
                    .map(RoleMenuEO::getMenuId)
                    .collect(Collectors.toList());

            return Result.success(menuIds);
        } catch (Exception e) {
            log.error("查询角色菜单失败", e);
            return Result.fail("查询角色菜单失败：" + e.getMessage());
        }
    }
}