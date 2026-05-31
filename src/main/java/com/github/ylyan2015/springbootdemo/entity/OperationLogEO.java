package com.github.ylyan2015.springbootdemo.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@Entity
@Table(name = "t_operation_log")
public class OperationLogEO {

    /**
     * 日志ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作用户ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 操作用户名
     */
    @Column(length = 50)
    private String username;

    /**
     * 操作模块（菜单/角色/字典等）
     */
    @Column(length = 50, nullable = false)
    private String module;

    /**
     * 操作类型（新增/修改/删除/查询等）
     */
    @Column(length = 20, nullable = false)
    private String operationType;

    /**
     * 操作描述
     */
    @Column(length = 500)
    private String description;

    /**
     * 请求方法
     */
    @Column(length = 100)
    private String requestMethod;

    /**
     * 请求URL
     */
    @Column(length = 500)
    private String requestUrl;

    /**
     * 请求参数
     */
    @Column(columnDefinition = "TEXT")
    private String requestParams;

    /**
     * 响应结果
     */
    @Column(columnDefinition = "TEXT")
    private String responseResult;

    /**
     * 操作IP地址
     */
    @Column(length = 50)
    private String ipAddress;

    /**
     * 操作耗时（毫秒）
     */
    private Long costTime;

    /**
     * 操作状态（0-失败，1-成功）
     */
    private Integer status;

    /**
     * 错误信息
     */
    @Column(columnDefinition = "TEXT")
    private String errorMsg;

    /**
     * 操作时间
     */
    @Column(updatable = false)
    private LocalDateTime operationTime;

    @PrePersist
    protected void onCreate() {
        operationTime = LocalDateTime.now();
    }
}
