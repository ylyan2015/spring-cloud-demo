package com.github.ylyan2015.springbootdemo.repository;

import com.github.ylyan2015.springbootdemo.entity.OperationLogEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 操作日志数据访问接口
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLogEO, Long> {

    /**
     * 根据用户ID查询操作日志
     */
    List<OperationLogEO> findByUserIdOrderByOperationTimeDesc(Long userId);

    /**
     * 根据模块查询操作日志
     */
    List<OperationLogEO> findByModuleOrderByOperationTimeDesc(String module);

    /**
     * 根据操作类型查询操作日志
     */
    List<OperationLogEO> findByOperationTypeOrderByOperationTimeDesc(String operationType);
}
