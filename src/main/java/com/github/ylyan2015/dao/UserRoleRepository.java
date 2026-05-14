
package com.github.ylyan2015.dao;

import com.github.ylyan2015.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联数据访问层
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * 根据用户ID查询关联记录
     */
    List<UserRole> findByUserId(Long userId);

    /**
     * 根据角色ID查询关联记录
     */
    List<UserRole> findByRoleId(Long roleId);

    /**
     * 根据用户ID删除关联记录
     */
    void deleteByUserId(Long userId);

    /**
     * 根据角色ID删除关联记录
     */
    void deleteByRoleId(Long roleId);

    /**
     * 根据用户ID和角色ID删除关联记录
     */
    void deleteByUserIdAndRoleId(Long userId, Long roleId);
}