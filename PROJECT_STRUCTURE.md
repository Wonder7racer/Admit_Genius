# AdmitGenius 后端项目结构

## 概述

AdmitGenius 后端采用标准的 Spring Boot 项目结构，遵循分层架构原则，将业务逻辑、数据访问、Web层和安全配置清晰分离。

## 项目目录结构

```
AdmitGeniusBackEnd/
├── pom.xml                                    # Maven 构建配置文件
├── mvnw                                       # Maven 包装器脚本 (Unix/Linux/macOS)
├── mvnw.cmd                                   # Maven 包装器脚本 (Windows)
├── doc.md                                     # 项目API文档
├── PROJECT_STRUCTURE.md                       # 本项目结构文档
├── create_table.sql                           # 数据库初始化脚本
├── class_diagram.puml                         # UML类图
├── HELP.md                                    # Spring Boot帮助文档
├── .gitignore                                 # Git忽略文件配置
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── admitgenius/
    │   │           └── backend/
    │   │               ├── AdmitGeniusBackEndApplication.java  # Spring Boot 应用入口点
    │   │               ├── config/                            # 应用配置包
    │   │               │   ├── OpenAIConfig.java             # OpenAI API配置
    │   │               │   └── WebConfig.java                # Web跨域配置
    │   │               ├── controller/                        # Web控制层
    │   │               │   ├── AuthController.java           # 认证控制器
    │   │               │   ├── UserController.java           # 用户管理控制器
    │   │               │   ├── EssayController.java          # 文书管理控制器
    │   │               │   ├── EssayGenerationController.java # 文书生成控制器
    │   │               │   ├── EssayPolishController.java    # 文书润色控制器
    │   │               │   ├── RecommendationController.java # 推荐系统控制器
    │   │               │   ├── ForumController.java          # 论坛控制器
    │   │               │   ├── SchoolManagementController.java # 学校管理控制器
    │   │               │   └── InternalAdminController.java  # 内部管理控制器
    │   │               ├── dto/                               # 数据传输对象
    │   │               │   ├── UserDTO.java                  # 用户DTO
    │   │               │   ├── SchoolDTO.java                # 学校DTO
    │   │               │   ├── SchoolProgramDTO.java         # 学校项目DTO
    │   │               │   ├── EssayDTO.java                 # 文书DTO
    │   │               │   ├── EssayGenerationDTO.java       # 文书生成DTO
    │   │               │   ├── EssayPolishDTO.java           # 文书润色DTO
    │   │               │   ├── RecommendationRequestDTO.java # 推荐请求DTO
    │   │               │   ├── RecommendationResponseDTO.java # 推荐响应DTO
    │   │               │   ├── ForumPostDTO.java             # 论坛帖子DTO
    │   │               │   ├── CommentDTO.java               # 评论DTO
    │   │               │   ├── CreateAdminRequestDTO.java    # 创建管理员请求DTO
    │   │               │   ├── UpdateUserRoleRequestDTO.java # 更新用户角色DTO
    │   │               │   └── AdminResetPasswordRequestDTO.java # 管理员重置密码DTO
    │   │               ├── exception/                         # 异常处理
    │   │               │   ├── GlobalExceptionHandler.java   # 全局异常处理器
    │   │               │   ├── ResourceNotFoundException.java # 资源不存在异常
    │   │               │   ├── BadRequestException.java      # 请求错误异常
    │   │               │   └── UnauthorizedAccessException.java # 未授权访问异常
    │   │               ├── model/                             # 数据模型/实体
    │   │               │   ├── User.java                     # 用户实体
    │   │               │   ├── UserRole.java                 # 用户角色枚举
    │   │               │   ├── UserStatus.java               # 用户状态枚举
    │   │               │   ├── School.java                   # 学校实体
    │   │               │   ├── SchoolProgram.java            # 学校项目实体
    │   │               │   ├── Essay.java                    # 文书实体
    │   │               │   ├── EssayRequirement.java         # 文书要求实体
    │   │               │   ├── Recommendation.java           # 推荐实体
    │   │               │   ├── RecommendationItem.java       # 推荐项实体
    │   │               │   ├── ApplicationStatistic.java     # 申请统计实体
    │   │               │   ├── ForumPost.java                # 论坛帖子实体
    │   │               │   ├── Comment.java                  # 评论实体
    │   │               │   ├── PostLike.java                 # 帖子点赞实体
    │   │               │   ├── Document.java                 # 文档实体
    │   │               │   └── ExpertReview.java             # 专家评审实体
    │   │               ├── payload/                           # 请求/响应负载
    │   │               │   ├── LoginRequest.java             # 登录请求
    │   │               │   ├── SignUpRequest.java            # 注册请求
    │   │               │   ├── JwtAuthenticationResponse.java # JWT认证响应
    │   │               │   └── ApiResponse.java              # 通用API响应
    │   │               ├── repository/                        # 数据访问层
    │   │               │   ├── UserRepository.java           # 用户数据访问
    │   │               │   ├── SchoolRepository.java         # 学校数据访问
    │   │               │   ├── SchoolProgramRepository.java  # 学校项目数据访问
    │   │               │   ├── EssayRepository.java          # 文书数据访问
    │   │               │   ├── RecommendationRepository.java # 推荐数据访问
    │   │               │   ├── RecommendationItemRepository.java # 推荐项数据访问
    │   │               │   ├── ApplicationStatisticRepository.java # 申请统计数据访问
    │   │               │   ├── ForumRepository.java          # 论坛数据访问
    │   │               │   ├── CommentRepository.java        # 评论数据访问
    │   │               │   └── PostLikeRepository.java       # 点赞数据访问
    │   │               ├── security/                          # 安全配置
    │   │               │   ├── SecurityConfig.java           # Spring Security配置
    │   │               │   ├── JwtTokenProvider.java         # JWT令牌提供者
    │   │               │   ├── JwtAuthenticationFilter.java  # JWT认证过滤器
    │   │               │   ├── JwtAuthenticationEntryPoint.java # JWT认证入口点
    │   │               │   └── UserDetailsServiceImpl.java   # 用户详情服务实现
    │   │               ├── service/                           # 业务逻辑层
    │   │               │   ├── UserService.java              # 用户业务服务
    │   │               │   ├── SchoolService.java            # 学校业务服务
    │   │               │   ├── EssayService.java             # 文书业务服务
    │   │               │   ├── EssayGenerationService.java   # 文书生成服务
    │   │               │   ├── EssayPolishService.java       # 文书润色服务
    │   │               │   ├── RecommendationService.java    # 推荐系统服务
    │   │               │   ├── ForumService.java             # 论坛业务服务
    │   │               │   └── impl/                         # 服务实现包
    │   │               └── util/                              # 工具类
    │   │                   └── AuthUtil.java                 # 认证工具类
    │   └── resources/                                         # 资源文件
    │       ├── application.properties                         # 应用配置文件
    │       └── openai.properties                             # OpenAI配置文件
    └── test/                                                  # 测试代码
        └── java/
            └── com/
                └── admitgenius/
                    └── backend/                               # 测试包结构镜像主代码
```

## 架构层次说明

### 1. 应用入口层
- **AdmitGeniusBackEndApplication.java**: Spring Boot应用启动类，包含main方法

### 2. Web控制层 (Controller)
负责处理HTTP请求，参数验证，调用服务层，返回响应结果

#### 认证相关控制器
- **AuthController**: 处理用户登录、注册等认证相关操作
- **UserController**: 处理用户信息的CRUD操作

#### 核心业务控制器
- **EssayController**: 文书基础CRUD操作
- **EssayGenerationController**: AI文书生成功能
- **EssayPolishController**: 文书润色功能
- **RecommendationController**: 学校推荐系统
- **ForumController**: 论坛系统功能

#### 管理控制器
- **SchoolManagementController**: 学校数据管理
- **InternalAdminController**: 内部管理功能

### 3. 业务逻辑层 (Service)
封装核心业务逻辑，处理复杂的业务规则

#### 核心服务
- **UserService**: 用户管理业务逻辑
- **EssayService**: 文书管理核心逻辑
- **EssayGenerationService**: AI文书生成核心算法
- **EssayPolishService**: 文书润色核心算法
- **RecommendationService**: 智能推荐算法
- **ForumService**: 论坛业务逻辑
- **SchoolService**: 学校数据业务逻辑

### 4. 数据访问层 (Repository)
使用Spring Data JPA实现数据库访问

#### 主要Repository
- **UserRepository**: 用户数据访问
- **SchoolRepository**: 学校数据访问
- **EssayRepository**: 文书数据访问
- **RecommendationRepository**: 推荐数据访问
- **ForumRepository**: 论坛数据访问

### 5. 数据传输层 (DTO)
定义前后端数据交互的结构

#### 核心DTO
- **UserDTO**: 用户数据传输对象
- **EssayDTO/EssayGenerationDTO/EssayPolishDTO**: 文书相关DTO
- **RecommendationRequestDTO/RecommendationResponseDTO**: 推荐系统DTO
- **SchoolDTO**: 学校数据传输对象

### 6. 数据模型层 (Model/Entity)
定义数据库表结构和实体关系

#### 核心实体
- **User**: 用户实体，包含基本信息、学术背景、考试成绩
- **School**: 学校实体，包含学校信息、排名、录取要求
- **Essay**: 文书实体，包含文书内容、类型、状态
- **Recommendation**: 推荐实体，记录推荐历史和结果
- **ForumPost**: 论坛帖子实体

### 7. 安全层 (Security)
实现认证和授权机制

#### 安全组件
- **SecurityConfig**: Spring Security主配置
- **JwtTokenProvider**: JWT令牌生成和验证
- **JwtAuthenticationFilter**: JWT认证过滤器
- **UserDetailsServiceImpl**: 用户详情服务

### 8. 配置层 (Config)
应用程序配置和第三方服务集成

- **WebConfig**: Web相关配置（跨域等）
- **OpenAIConfig**: OpenAI API配置

### 9. 异常处理层 (Exception)
统一异常处理和错误响应

- **GlobalExceptionHandler**: 全局异常处理器
- **自定义异常类**: 业务相关异常定义

### 10. 工具层 (Util)
通用工具类和辅助方法

- **AuthUtil**: 认证相关工具方法

## 依赖关系

```
Controller → Service → Repository → Entity
     ↓         ↓
    DTO   ←   Model
     ↓         ↓
  Payload   Exception
     ↓
  Security
```

## 数据库设计

### 核心表结构
1. **users**: 用户基本信息表
2. **schools**: 学校信息表
3. **school_programs**: 学校项目表
4. **essays**: 文书表
5. **recommendations**: 推荐记录表
6. **recommendation_items**: 推荐项详情表
7. **forum_posts**: 论坛帖子表
8. **comments**: 评论表
9. **post_likes**: 点赞记录表

### 关系设计
- 用户与文书：一对多关系
- 用户与推荐：一对多关系
- 学校与推荐项：一对多关系
- 帖子与评论：一对多关系
- 用户与点赞：多对多关系

## 外部集成

### API集成
- **OpenAI GPT API**: 用于文书生成和润色
- **学校排名API**: 获取最新学校排名信息（预留）

### 安全特性
- JWT令牌认证
- 角色权限控制 (USER, ADMIN)
- 密码加密存储
- 跨域资源共享 (CORS) 配置

## 开发规范

### 代码组织
1. 使用Lombok减少样板代码
2. 使用Spring Boot Validation进行参数验证
3. 统一异常处理和响应格式
4. 遵循RESTful API设计规范

### 配置管理
1. 敏感信息使用环境变量
2. 分环境配置文件管理
3. 统一配置属性命名规范

这个项目结构清晰地分离了各个关注点，便于维护和扩展，同时保持了代码的可读性和可测试性。




