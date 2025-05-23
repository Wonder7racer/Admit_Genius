# AdmitGenius 后端配置指南

## 环境变量配置

在部署应用之前，需要配置以下环境变量：

### 必需配置

#### 数据库配置
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password_here
```

#### JWT配置
```bash
export JWT_SECRET=your_jwt_secret_key_here_at_least_32_characters_long
export JWT_EXPIRATION=86400000
```

#### OpenAI API配置
```bash
export OPENAI_API_KEY=your_openai_api_key_here
export OPENAI_PROXY_URL=https://api.openai.com/v1
```

### 可选配置

#### 服务器配置
```bash
export SERVER_PORT=7077
export SPRING_PROFILES_ACTIVE=prod
```

## 配置文件说明

### application.properties (开发环境)
- 用于本地开发
- 数据库密码和API密钥需要手动配置
- 启用SQL日志和调试信息

### application-prod.properties (生产环境)
- 用于生产部署
- 所有敏感信息通过环境变量配置
- 禁用调试信息，启用安全配置

### openai.properties
- OpenAI API相关配置
- 支持代理URL配置

## 安全建议

1. **JWT密钥生成**
   ```bash
   openssl rand -base64 32
   ```

2. **数据库安全**
   - 创建专用数据库用户而非使用root
   - 设置强密码
   - 限制数据库访问权限

3. **API密钥管理**
   - 不要在代码中硬编码API密钥
   - 使用环境变量或密钥管理服务
   - 定期轮换API密钥

## 部署配置

### 开发环境
```bash
# 设置环境变量
export DB_PASSWORD=your_dev_password
export JWT_SECRET=your_dev_jwt_secret
export OPENAI_API_KEY=your_openai_key

# 启动应用
./deploy.sh dev run
```

### 生产环境
```bash
# 设置环境变量
export DB_PASSWORD=your_prod_password
export JWT_SECRET=your_prod_jwt_secret
export OPENAI_API_KEY=your_openai_key
export SPRING_PROFILES_ACTIVE=prod

# 部署应用
./deploy.sh prod deploy
```

## 数据库初始化

1. **创建数据库**
   ```sql
   CREATE DATABASE admitgenius_db;
   ```

2. **运行初始化脚本**
   ```bash
   mysql -u root -p admitgenius_db < create_table.sql
   ```

3. **验证数据**
   - 应用启动时会自动创建默认管理员账户
   - 邮箱: admin@admitgenius.com
   - 密码: admin123

## 健康检查

应用启动后，可以通过以下端点检查系统状态：

- 健康检查: `GET /api/system/health`
- 系统信息: `GET /api/system/info`
- API版本: `GET /api/system/version`

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
   - 确认代理URL配置

### 日志查看

```bash
# 查看应用日志
./deploy.sh dev logs

# 或直接查看日志文件
tail -f app.log
``` 