
package com.github.ylyan2015.dao;

import com.github.ylyan2015.entity.RoleEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色数据访问层
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEO, Long> {

    /**
     * 根据角色编码查询
     */
    Optional<RoleEO> findByRoleCode(String roleCode);

    /**
     * 根据角色名称查询
     */
    Optional<RoleEO> findByRoleName(String roleName);
}