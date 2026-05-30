# Spring Cloud Demo

基于 Spring Boot 微服务架构的用户权限管理系统（单体应用）

## 项目简介

这是一个基于 Spring Boot 的微服务示例项目，实现了完整的用户、角色、菜单、字典管理功能，采用 RBAC 权限模型，支持国密算法密码加密和 JWT 认证。

## 核心特性

- **微服务技术栈**: 基于 Spring Cloud Alibaba 技术栈（单体部署）
- **国密安全**: 使用 SM3 算法进行密码加密（用户名作为 Salt）
- **JWT 认证**: 基于 JSON Web Token 的无状态认证机制
- **数据缓存**: Redis 缓存用户信息和登录令牌
- **前后端一体**: Thymeleaf 模板引擎渲染页面
- **RBAC 权限**: 用户-角色-菜单三级权限控制
- **服务治理**: Nacos 服务发现 + Sentinel 熔断限流
- **API 文档**: 集成 SpringDoc OpenAPI 3.0 文档
- **操作日志**: 记录用户操作日志和登录日志
- **IP 地址解析**: 集成 ip2region 实现 IP 地址归属地查询

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
- MySQL: 关系型数据库（mysql-connector-j）
- Redis: 缓存存储（Lettuce 连接池）

### 前端

- Thymeleaf: 服务端模板引擎
- HTML5 + CSS3: 页面结构与样式
- JavaScript (ES6+): 前端交互逻辑

### 安全加密

- Bouncy Castle: 国密算法支持库（bcprov-jdk15on 1.70）
- SM3: 密码哈希加密
- JWT: JSON Web Token 认证（jjwt 0.11.5）

### API 文档

- SpringDoc OpenAPI: 1.7.0（OpenAPI 3.0 规范）

### 工具库

- Lombok: 简化代码编写
- ip2region: 2.7.0（IP 地址解析库）
- javax.annotation-api: 1.3.2（JDK 11 注解支持）

## 项目结构
```
spring-cloud-demo/
├── src/
│   ├── main/
│   │   ├── java/com/github/ylyan2015/
│   │   │   ├── common/           # 公共类（Result、SystemConstants）
│   │   │   ├── config/           # 配置类（JWT、Redis、Swagger、全局异常处理等）
│   │   │   ├── controller/       # 控制器层
│   │   │   │   ├── UserController.java        # 用户管理
│   │   │   │   ├── RoleController.java        # 角色管理
│   │   │   │   ├── MenuController.java        # 菜单管理
│   │   │   │   ├── DictController.java        # 字典管理
│   │   │   │   ├── LoginLogController.java    # 登录日志
│   │   │   │   ├── OperationLogController.java # 操作日志
│   │   │   │   ── PageController.java        # 页面路由
│   │   │   ├── dao/              # 数据访问层（Repository）
│   │   │   ├── dto/              # 数据传输对象
│   │   │   ├── entity/           # 实体类（EO 后缀）
│   │   │   ├── service/          # 业务逻辑层
│   │   │   │   ├── impl/         # 服务实现类
│   │   │   │   ── I*Service.java # 服务接口
│   │   │   ├── util/             # 工具类
│   │   │   │   ├── JwtUtil.java          # JWT 工具
│   │   │   │   ├── SmCryptoUtil.java     # 国密加密工具
│   │   │   │   ├── RedisUtil.java        # Redis 工具
│   │   │   │   └── IpUtil.java           # IP 地址解析工具
│   │   │   ├── exception/        # 自定义异常类
│   │   │   └── SpringCloudDemoApplication.java # 启动类
│   │   └── resources/
│   │       ├── static/           # 静态资源（CSS、JS、图片）
│   │       ├── templates/        # 页面模板
│   │       ├── application-test.yml   # 测试环境配置
│   │       ├── application-prd.yml    # 生产环境配置
│   │       ├── bootstrap.yml          # Nacos 配置
│   │       ├── ip2region_v4.xdb       # IPv4 地址库
│   │       └── ip2region_v6.xdb       # IPv6 地址库
│   └── test/                     # 测试代码
├── target/                       # 编译输出目录
├── pom.xml                       # Maven 配置
└── README.md                     # 项目说明
```
## 快速开始

### 环境要求

- JDK 11+
- Maven 3.6+
- MySQL 5.7+ / 8.0+
- Redis 5.0+
- Nacos 2.x

### 安装步骤

1. **克隆项目**

2. **创建数据库**

CREATE DATABASE spring_cloud_demo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

3. **配置数据库连接**（application-test.yml）

spring.datasource.url=jdbc:mysql://localhost:3306/test_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true
spring.datasource.username=root
spring.datasource.password=123456

4. **配置 Nacos**（bootstrap.yml）

spring.cloud.nacos.config.server-addr=127.0.0.1:8848
spring.cloud.nacos.config.username=nacos
spring.cloud.nacos.config.password=nacos

5. **构建并启动**

mvn clean package -DskipTests
java -jar target/spring-cloud-demo-1.0.0.jar

6. **访问应用**

- 登录页面: http://localhost:8080/login
- API 文档: http://localhost:8080/swagger-ui.html

## API 接口

### 用户管理 /user

- POST /user/login - 用户登录
- POST /user/logout - 用户登出
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
- GET /role/menu-ids/{roleId} - 查询角色菜单

### 菜单管理 /menu

- POST /menu/add - 新增菜单
- PUT /menu/update - 修改菜单
- DELETE /menu/delete/{id} - 删除菜单
- GET /menu/get/{id} - 查询菜单详情
- GET /menu/list - 查询菜单列表
- GET /menu/tree - 查询菜单树形结构
- GET /menu/children/{parentId} - 查询子菜单

### 字典管理 /dict

- POST /dict/add - 新增字典
- PUT /dict/update - 修改字典
- DELETE /dict/delete/{id} - 删除字典
- GET /dict/get/{id} - 查询字典详情
- GET /dict/getByCode/{dictCode} - 根据编码查询字典
- GET /dict/list - 查询字典列表
- PUT /dict/toggleStatus - 切换字典状态
- POST /dict/item/add - 新增字典项
- PUT /dict/item/update - 修改字典项
- DELETE /dict/item/delete/{id} - 删除字典项
- GET /dict/item/list/{dictId} - 查询字典项列表
- PUT /dict/item/toggleStatus - 切换字典项状态

### 登录日志 /login-log

- GET /login-log/user/{userId} - 查询用户登录日志
- GET /login-log/username/{username} - 根据用户名查询登录日志
- GET /login-log/list - 查询登录日志列表

### 操作日志 /operation-log

- GET /operation-log/user/{userId} - 查询用户操作日志
- GET /operation-log/list - 查询操作日志列表

## 安全设计

### 密码加密

- **算法**: 国密 SM3
- **Salt**: 使用用户名作为盐值
- **流程**: 密码明文 -> SM3(密码 + 用户名) -> 哈希值存储

### JWT 认证

- **算法**: HS256
- **密钥**: Base64 编码（配置在 bootstrap.yml）
- **有效期**: 7200 秒（2 小时）
- **请求头**: Authorization: Bearer {token}
- **Claims**: 包含 userId、username、sub、iat、exp

### Token 机制

- **生成**: JWT 标准格式，包含用户 ID 和用户名
- **存储**: Redis 缓存，TTL 2 小时
- **刷新**: 支持 Token 刷新机制

### 缓存策略

- user:{userId} - 用户信息（5分钟）
- user:role:{userId} - 用户角色列表（1分钟）
- login:token:{token} - 登录令牌（2小时）

## 数据库设计

### 核心表

- t_user - 用户表
- t_role - 角色表
- t_menu - 菜单表
- t_dict - 字典表
- t_dict_item - 字典项表
- t_user_role - 用户角色关联表
- t_role_menu - 角色菜单关联表
- t_login_log - 登录日志表
- t_operation_log - 操作日志表

### 实体命名规范

- 实体类: 使用 EO 后缀（如 UserEO）
- DTO: 使用 Dto 后缀（如 UserDto）
- Repository: 使用 Repository 后缀
- Service 接口: 使用 I 前缀（如 IUserService）

## 开发指南

### 代码规范

1. **实体类命名**: 使用 EO 后缀（如 UserEO）
2. **DTO 命名**: 使用 Dto 后缀（如 UserDto）
3. **Repository 命名**: 使用 Repository 后缀
4. **Service 接口**: 使用 I 前缀（如 IUserService）
5. **注释要求**: 简洁明了，使用中文注释
6. **异常管理**: 使用自定义异常类，集中在 exception 包

### 开发流程

git pull
mvn clean compile
mvn test
mvn clean package
java -jar target/spring-cloud-demo-1.0.0.jar

### 常用命令

**清理构建**

mvn clean

**编译项目**

mvn compile

**运行测试**

mvn test

**打包项目**

mvn package

**跳过测试打包**

mvn package -DskipTests

**查看依赖树**

mvn dependency:tree

## 常见问题

### JDK 11 注解问题

**问题**: @Resource 和 @PostConstruct 无法使用
**解决**: 项目已引入 javax.annotation-api 依赖

### Redis 连接失败

**检查**:
- Redis 服务是否启动
- 配置是否正确（application-test.yml）
- 端口 6379 是否可访问

### Nacos 注册失败

**检查**:
- Nacos 服务是否启动（端口 8848）
- 用户名密码是否正确
- 网络连接是否正常

### Swagger 文档无法访问

**检查**:
- 访问路径是否正确：http://localhost:8080/swagger-ui.html
- 是否引入 springdoc-openapi-ui 依赖
- Controller 是否正确添加 @Tag 和 @Operation 注解

### IP 地址解析失败

**检查**:
- ip2region_v4.xdb 和 ip2region_v6.xdb 文件是否存在于 resources 目录
- 是否正确配置 ip2region 依赖

## 许可证

MIT License

## 更新日志

### v1.0.0 (2026-05-30)

- 初始版本发布
- 用户、角色、菜单、字典管理功能
- 国密 SM3 密码加密
- JWT 认证机制
- Redis 缓存支持
- Thymeleaf 登录页面
- Nacos + Sentinel 微服务集成
- SpringDoc OpenAPI 3.0 文档
- 登录日志和操作日志功能
- IP 地址归属地解析
- 全局异常处理
- 用户登出功能

---
Made with ❤️ by ylyan2015