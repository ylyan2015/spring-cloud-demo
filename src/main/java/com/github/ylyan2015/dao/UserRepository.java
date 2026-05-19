
package com.github.ylyan2015.dao;

import com.github.ylyan2015.entity.UserEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<UserEO, Long> {
    
    /**
     * 根据用户名查询用户
     */
    Optional<UserEO> findByUsername(String username);
    
    /**
     * 根据用户名查询用户（排除指定ID）
     */
    Optional<UserEO> findByUsernameAndIdNot(String username, Long id);
}