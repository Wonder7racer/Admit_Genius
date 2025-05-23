# AdmitGenius 后端系统

## 项目简介

AdmitGenius 是一个智能留学申请助手系统的后端服务，使用Java Spring Boot框架构建。该系统提供以下核心功能：

- **用户管理系统**: 用户注册、登录、JWT认证、角色权限控制
- **AI文书系统**: 基于OpenAI GPT的文书生成与润色功能
- **智能推荐系统**: 基于用户背景的学校推荐算法
- **论坛交流系统**: 帖子发布、评论互动、点赞功能
- **管理员系统**: 学校数据管理、用户管理、系统配置

## 技术栈

- **框架**: Spring Boot 3.4.3
- **Java版本**: 17
- **数据库**: MySQL (支持PostgreSQL)
- **安全认证**: Spring Security + JWT
- **ORM**: Spring Data JPA + Hibernate
- **AI集成**: OpenAI GPT API
- **构建工具**: Maven
- **其他依赖**: Lombok, Validation, DevTools

## 快速开始

### 环境要求

- Java 17 或更高版本
- Maven 3.6 或更高版本
- MySQL 8.0 或更高版本
- OpenAI API Key (用于AI文书功能)

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd AdmitGeniusBackEnd
   ```

2. **配置数据库**
   
   创建MySQL数据库：
   ```sql
   CREATE DATABASE admitgenius_db;
   ```
   
   运行数据库初始化脚本：
   ```bash
   mysql -u root -p admitgenius_db < create_table.sql
   ```

3. **配置应用**
   
   编辑 `src/main/resources/application.properties` 文件：
   ```properties
   # 数据库配置
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   
   # JWT密钥 (请生成一个安全的随机字符串)
   app.jwt.secret=YOUR_JWT_SECRET_KEY
   
   # OpenAI API密钥
   openai.api.key=YOUR_OPENAI_API_KEY
   ```

4. **编译项目**
   ```bash
   mvn clean compile
   ```

5. **运行项目**
   ```bash
   mvn spring-boot:run
   ```

   或者打包后运行：
   ```bash
   mvn clean package
   java -jar target/AdmitGeniusBackEnd-0.0.1-SNAPSHOT.jar
   ```

6. **访问应用**
   
   应用将在 `http://localhost:7077` 启动

## API 文档

详细的API文档请参考 [doc.md](doc.md) 文件。

### 主要API端点

- **认证**: `/api/auth/*`
- **用户管理**: `/api/users/*`
- **文书管理**: `/api/essays/*`
- **文书生成**: `/api/essays/generation/*`
- **文书润色**: `/api/essays/polish/*`
- **学校推荐**: `/api/recommendations/*`
- **论坛系统**: `/api/forum/*`
- **管理员接口**: `/api/admin/*`

### 默认管理员账户

系统初始化时会创建一个默认管理员账户：
- 邮箱: `admin@admitgenius.com`
- 密码: `admin123`

## 开发指南

### 项目结构

详细的项目结构说明请参考 [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) 文件。

### 代码规范

1. 使用Lombok减少样板代码
2. 使用Spring Boot Validation进行参数验证
3. 统一异常处理和响应格式
4. 遵循RESTful API设计规范

### 测试

使用Postman或类似工具测试API端点。需要在请求头中包含JWT令牌：
```
Authorization: Bearer YOUR_JWT_TOKEN
```

### 数据库设计

项目使用JPA自动创建表结构，也可以手动运行 `create_table.sql` 脚本初始化数据库。

核心表包括：
- `users`: 用户信息
- `schools`: 学校信息
- `essays`: 文书数据
- `recommendations`: 推荐记录
- `forum_posts`: 论坛帖子

## 部署

### 生产环境配置

1. **环境变量配置**
   ```bash
   export DB_PASSWORD=your_production_db_password
   export JWT_SECRET=your_production_jwt_secret
   export OPENAI_API_KEY=your_openai_api_key
   ```

2. **应用配置**
   ```properties
   spring.profiles.active=prod
   spring.datasource.password=${DB_PASSWORD}
   app.jwt.secret=${JWT_SECRET}
   openai.api.key=${OPENAI_API_KEY}
   ```

3. **打包部署**
   ```bash
   mvn clean package -Pprod
   java -jar target/AdmitGeniusBackEnd-0.0.1-SNAPSHOT.jar
   ```

## 故障排除

### 常见问题

1. **数据库连接失败**
   - 检查MySQL服务是否启动
   - 验证数据库用户名和密码
   - 确认数据库名称正确

2. **JWT认证失败**
   - 检查JWT密钥配置
   - 验证令牌格式和有效期

3. **OpenAI API调用失败**
   - 验证API密钥是否正确
   - 检查网络连接和API配额

### 日志查看

应用日志级别配置：
```properties
logging.level.root=INFO
logging.level.com.admitgenius=DEBUG
```

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件。

## 联系方式

如有问题或建议，请通过以下方式联系：

- 项目Issues: [GitHub Issues](https://github.com/your-repo/issues)
- 邮箱: admin@admitgenius.com

## 更新日志

### v0.0.1-SNAPSHOT (最新)
- ✅ 初始版本发布
- ✅ 实现用户管理、文书系统、推荐系统、论坛功能
- ✅ 集成OpenAI GPT API
- ✅ 完整的管理员后台功能
- ✅ **新增**: 数据自动初始化功能
- ✅ **新增**: 增强的异常处理和输入验证
- ✅ **新增**: 系统健康检查端点
- ✅ **新增**: 自动化部署脚本
- ✅ **新增**: 生产环境配置
- ✅ **新增**: 完整的功能测试清单
- ✅ **改进**: MySQL连接器更新到最新版本
- ✅ **改进**: OpenAI配置优化
- ✅ **改进**: 安全配置增强

## 新增功能

### 🔧 系统监控
- **健康检查**: `GET /api/system/health` - 检查系统和数据库状态
- **系统信息**: `GET /api/system/info` - 获取详细系统信息
- **API版本**: `GET /api/system/version` - 获取API版本信息

### 🚀 自动化部署
- **部署脚本**: `./deploy.sh` - 一键部署和管理应用
- **环境支持**: 支持开发和生产环境配置
- **进程管理**: 自动启动、停止、重启功能
- **日志管理**: 集成日志查看功能

### 📊 数据初始化
- **自动初始化**: 应用启动时自动创建基础数据
- **默认管理员**: 自动创建系统管理员账户
- **示例数据**: 预置知名大学和项目数据

### 🛡️ 安全增强
- **输入验证**: 全面的请求参数验证
- **异常处理**: 统一的错误响应格式
- **环境变量**: 敏感信息通过环境变量配置
- **生产配置**: 专门的生产环境安全配置

## 部署指南

### 快速开始

1. **环境准备**
   ```bash
   # 确保已安装 Java 17+ 和 Maven 3.6+
   java -version
   mvn -version
   ```

2. **配置环境变量**
   ```bash
   export DB_PASSWORD=your_mysql_password
   export JWT_SECRET=$(openssl rand -base64 32)
   export OPENAI_API_KEY=your_openai_api_key
   ```

3. **一键部署**
   ```bash
   # 开发环境
   ./deploy.sh dev deploy
   
   # 生产环境
   ./deploy.sh prod deploy
   ```

4. **验证部署**
   ```bash
   curl http://localhost:7077/api/system/health
   ```

### 部署脚本使用

```bash
# 构建应用
./deploy.sh dev build

# 启动应用
./deploy.sh dev start

# 停止应用
./deploy.sh dev stop

# 重启应用
./deploy.sh dev restart

# 查看状态
./deploy.sh dev status

# 查看日志
./deploy.sh dev logs
```

## 配置说明

详细配置说明请参考 [CONFIGURATION.md](CONFIGURATION.md)

## 功能测试

完整的功能测试清单请参考 [TESTING_CHECKLIST.md](TESTING_CHECKLIST.md)

## 技术改进

### 依赖更新
- MySQL连接器升级到最新版本
- 移除过时的依赖警告

### 代码质量
- 统一异常处理机制
- 完善输入验证
- 优化配置管理

### 运维支持
- 健康检查端点
- 系统监控信息
- 自动化部署脚本
- 日志管理

## 故障排除

### 常见问题

1. **应用启动失败**
   ```bash
   # 检查日志
   ./deploy.sh dev logs
   
   # 检查健康状态
   curl http://localhost:7077/api/system/health
   ```

2. **数据库连接问题**
   ```bash
   # 验证数据库配置
   mysql -u root -p -e "SHOW DATABASES;"
   ```

3. **OpenAI API问题**
   ```bash
   # 验证API密钥
   curl -H "Authorization: Bearer $OPENAI_API_KEY" \
        https://api.openai.com/v1/models
   ```

### 获取帮助

- 查看 [CONFIGURATION.md](CONFIGURATION.md) 了解配置详情
- 查看 [TESTING_CHECKLIST.md](TESTING_CHECKLIST.md) 进行功能测试
- 检查应用日志: `./deploy.sh dev logs`
- 访问健康检查端点: `http://localhost:7077/api/system/health` 