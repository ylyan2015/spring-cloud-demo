package com.github.ylyan2015.springbootdemo.repository;

import com.github.ylyan2015.springbootdemo.entity.RoleMenuEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色菜单关联数据访问层
 */
@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenuEO, Long> {

    /**
     * 根据角色ID查询关联记录
     */
    List<RoleMenuEO> findByRoleId(Long roleId);

    /**
     * 根据菜单ID查询关联记录
     */
    List<RoleMenuEO> findByMenuId(Long menuId);

    /**
     * 根据角色ID删除关联记录
     */
    void deleteByRoleId(Long roleId);

    /**
     * 根据菜单ID删除关联记录
     */
    void deleteByMenuId(Long menuId);
}
