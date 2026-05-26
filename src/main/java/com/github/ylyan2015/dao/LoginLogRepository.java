package com.github.ylyan2015.dao;

import com.github.ylyan2015.entity.LoginLogEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 登录日志数据访问接口
 */
@Repository
public interface LoginLogRepository extends JpaRepository<LoginLogEO, Long> {

    /**
     * 根据用户ID查询登录日志
     */
    List<LoginLogEO> findByUserIdOrderByOperationTimeDesc(Long userId);

    /**
     * 根据用户名查询登录日志
     */
    List<LoginLogEO> findByUsernameOrderByOperationTimeDesc(String username);

    /**
     * 根据操作类型查询登录日志
     */
    List<LoginLogEO> findByOperationTypeOrderByOperationTimeDesc(String operationType);
}
