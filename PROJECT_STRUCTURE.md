# AdmitGenius 后端项目结构

## 目录结构

```
AdmitGeniusBackEnd/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── admitgenius/
    │   │           └── backend/
    │   │               ├── AdmitGeniusBackEndApplication.java  # 应用入口
    │   │               ├── config/                            # 配置类（跨域、拦截器等）
    │   │               │   └── WebConfig.java
    │   │               ├── controller/                        # 控制层（用户、推荐、文书、论坛等）
    │   │               │   ├── UserController.java
    │   │               │   ├── AdminController.java
    │   │               │   ├── RecommendationController.java
    │   │               │   ├── EssayController.java
    │   │               │   └── ForumController.java
    │   │               ├── dto/                               # 数据传输对象
    │   │               │   ├── UserDTO.java
    │   │               │   ├── SchoolDTO.java
    │   │               │   ├── EssayDTO.java
    │   │               │   └── ForumPostDTO.java
    │   │               ├── exception/                         # 全局异常处理
    │   │               │   └── GlobalExceptionHandler.java
    │   │               ├── integration/                       # 第三方服务集成
    │   │               │   ├── RankingApiClient.java
    │   │               │   └── ESignatureClient.java
    │   │               ├── model/                             # 数据模型（用户、院校、文书等）
    │   │               │   ├── User.java
    │   │               │   ├── School.java
    │   │               │   ├── Essay.java
    │   │               │   └── ForumPost.java
    │   │               ├── repository/                        # 数据访问层
    │   │               │   ├── UserRepository.java
    │   │               │   ├── SchoolRepository.java
    │   │               │   ├── EssayRepository.java
    │   │               │   └── ForumRepository.java
    │   │               ├── security/                          # 安全配置
    │   │               │   └── SecurityConfig.java
    │   │               ├── service/                           # 业务逻辑层
    │   │               │   ├── UserService.java
    │   │               │   ├── RecommendationService.java    # 智能择校推荐
    │   │               │   ├── EssayOptimizationService.java # AI 文书优化
    │   │               │   └── ForumService.java
    │   │               └── util/                              # 工具类
    │   │                   └── FileUtil.java
    │   └── resources/                                         # 配置文件及静态资源
    │       ├── application.properties
    │       ├── logback-spring.xml
    │       └── other
    └── test/                                                  # 测试代码
        └── java/
            └── com/
                └── admitgenius/
                    └── backend/
                        ├── controller/
                        ├── service/
                        └── repository/
```

## 结构说明

### 1. 入口与配置
- `AdmitGeniusBackEndApplication.java`：Spring Boot 应用启动类。
- `config/`：存放项目配置（如跨域、拦截器）。

### 2. 控制层（Controller）
- 负责处理 HTTP 请求，调用 Service 层并返回响应。

### 3. 业务层（Service）
- 核心业务逻辑封装，如智能择校推荐、AI 文书优化。

### 4. 数据访问层（Repository）
- 使用 Spring Data JPA 访问数据库。

### 5. 领域模型（Model）与 DTO
- `model/`：数据库实体。
- `dto/`：请求与响应数据封装。

### 6. 安全与集成
- `security/`：认证与授权机制。
- `integration/`：第三方 API（如高校排名、电子签名）。

### 7. 工具类（Util）
- 处理文件、格式转换等辅助功能。




