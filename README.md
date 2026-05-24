# Spring Cloud Demo

基于 Spring Cloud 微服务架构的用户权限管理系统

## 项目简介

这是一个基于 Spring Boot + Spring Cloud 的微服务示例项目，实现了完整的用户、角色、菜单、字典管理功能，采用 RBAC 权限模型，支持国密算法密码加密。

## 核心特性

- 微服务架构: 基于 Spring Cloud Alibaba 技术栈
- 国密安全: 使用 SM3 算法进行密码加密（用户名作为 Salt）
- 数据缓存: Redis 缓存用户信息和登录令牌
- 前后端一体: Thymeleaf 模板引擎渲染页面
- RBAC 权限: 用户-角色-菜单三级权限控制
- 服务治理: Nacos 服务发现 + Sentinel 熔断限流

## 技术栈

### 后端框架

- Spring Boot: 2.7.18
- Spring Cloud: 2021.0.9
- Spring Cloud Alibaba: 2021.0.5.0
- JDK: 11

### 微服务组件

- Nacos: 服务注册发现 + 配置中心
- OpenFeign: 服务间远程调用
- Sentinel: 熔断限流
- LoadBalancer: 负载均衡

### 数据层

- Spring Data JPA: ORM 框架
- MySQL: 关系型数据库
- Redis: 缓存存储

### 前端

- Thymeleaf: 服务端模板引擎
- HTML5 + CSS3: 页面结构与样式
- JavaScript (ES6+): 前端交互逻辑

### 安全加密

- Bouncy Castle: 国密算法支持库
- SM3: 密码哈希加密

## 项目结构

spring-cloud-demo/
├── src/
│ ├── main/
│ │ ├── java/com/github/ylyan2015/
│ │ │ ├── common/ # 公共类
│ │ │ ├── config/ # 配置类
│ │ │ ├── controller/ # 控制器层
│ │ │ ├── dao/ # 数据访问层
│ │ │ ├── dto/ # 数据传输对象
│ │ │ ├── entity/ # 实体类
│ │ │ ├── service/ # 业务逻辑层
│ │ │ └── util/ # 工具类
│ │ ── resources/
│ │ ├── static/ # 静态资源
│ │ ├── templates/ # 页面模板
│ │ ├── application-*.yml # 环境配置
│ │ └── bootstrap.yml # Nacos配置
│ └── test/ # 测试代码
├── pom.xml # Maven配置
└── README.md # 项目说明

## 快速开始

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 5.7+ / 8.0+
- Redis 5.0+
- Nacos 2.x

### 安装步骤

1. 克隆项目
   git clone https://github.com/ylyan2015/spring-cloud-demo.git
   cd spring-cloud-demo

2. 创建数据库
   CREATE DATABASE spring_cloud_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

3. 配置数据库连接（application-test.yml）
   spring.datasource.url=jdbc:mysql://localhost:3306/spring_cloud_demo
   spring.datasource.username=root
   spring.datasource.password=your_password

4. 配置 Nacos（bootstrap.yml）
   spring.cloud.nacos.config.server-addr=127.0.0.1:8848
   spring.cloud.nacos.config.username=nacos
   spring.cloud.nacos.config.password=nacos

5. 构建并启动
   mvn clean package -DskipTests
   java -jar target/spring-cloud-demo-1.0.0.jar

6. 访问应用
   http://localhost:8080/login

## API 接口

### 用户管理 /user

- POST /user/login - 用户登录
- POST /user/add - 新增用户
- PUT /user/update - 修改用户
- DELETE /user/delete/{id} - 删除用户
- GET /user/get/{id} - 查询用户详情
- GET /user/list - 查询用户列表
- POST /user/assign-roles - 分配角色

### 角色管理 /role

- POST /role/add - 新增角色
- PUT /role/update - 修改角色
- DELETE /role/delete/{id} - 删除角色
- GET /role/get/{id} - 查询角色详情
- GET /role/list - 查询角色列表
- POST /role/assign-menus - 分配菜单

### 菜单管理 /menu

- POST /menu/add - 新增菜单
- PUT /menu/update - 修改菜单
- DELETE /menu/delete/{id} - 删除菜单
- GET /menu/get/{id} - 查询菜单详情
- GET /menu/list - 查询菜单列表

### 字典管理 /dict

- POST /dict/add - 新增字典
- PUT /dict/update - 修改字典
- DELETE /dict/delete/{id} - 删除字典
- GET /dict/get/{id} - 查询字典详情
- GET /dict/list - 查询字典列表

## 安全设计

### 密码加密

- 算法: 国密 SM3
- Salt: 使用用户名作为盐值
- 流程: 密码明文 -> SM3(密码 + 用户名) -> 哈希值存储

### Token 机制

- 生成: SM3(userId + timestamp)
- 存储: Redis 缓存，TTL 2 小时
- 格式: login:token:{token}

### 缓存策略

- user:{userId} - 用户信息（5分钟）
- user:role:{userId} - 用户角色列表（1分钟）
- login:token:{token} - 登录令牌（2小时）

## 数据库设计

核心表:

- t_user - 用户表
- t_role - 角色表
- t_menu - 菜单表
- t_dict - 字典表
- t_dict_item - 字典项表
- t_user_role - 用户角色关联表
- t_role_menu - 角色菜单关联表

## 开发指南

### 代码规范

1. 实体类命名: 使用 EO 后缀（如 UserEO）
2. DTO 命名: 使用 Dto 后缀（如 UserDto）
3. Repository 命名: 使用 Repository 后缀
4. Service 接口: 使用 I 前缀（如 IUserService）
5. 注释要求: 简洁明了，使用中文注释

### 开发流程

git pull
mvn clean compile
mvn test
mvn clean package
java -jar target/spring-cloud-demo-1.0.0.jar

## 常见问题

### JDK 11 注解问题

问题: @Resource 和 @PostConstruct 无法使用
解决: 项目已引入 javax.annotation-api 依赖

### Redis 连接失败

检查: Redis 服务是否启动、配置是否正确

### Nacos 注册失败

检查: Nacos 服务是否启动(端口8848)、用户名密码是否正确

## 许可证

MIT License

## 更新日志

### v1.0.0 (2026-05-23)

- 初始版本发布
- 用户、角色、菜单、字典管理功能
- 国密 SM3 密码加密
- Redis 缓存支持
- Thymeleaf 登录页面
- Nacos + Sentinel 微服务集成

---
Made with ❤️ by ylyan2015
