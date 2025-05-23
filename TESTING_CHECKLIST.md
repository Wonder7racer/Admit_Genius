# AdmitGenius 后端功能测试清单

## 系统启动测试

- [ ] 应用能够正常启动
- [ ] 数据库连接成功
- [ ] 默认管理员账户创建成功
- [ ] 示例学校数据初始化成功
- [ ] 健康检查端点响应正常

## 认证系统测试

### 用户注册 (POST /api/auth/signup)
- [ ] 正常注册新用户
- [ ] 邮箱格式验证
- [ ] 密码长度验证
- [ ] 重复邮箱注册失败
- [ ] 角色设置正确

### 用户登录 (POST /api/auth/signin)
- [ ] 正确邮箱密码登录成功
- [ ] 错误密码登录失败
- [ ] 不存在邮箱登录失败
- [ ] JWT令牌正确生成
- [ ] 用户信息正确返回

### JWT认证
- [ ] 有效令牌访问受保护端点成功
- [ ] 无效令牌访问受保护端点失败
- [ ] 过期令牌访问受保护端点失败
- [ ] 无令牌访问受保护端点失败

## 用户管理系统测试

### 用户信息管理
- [ ] 获取用户信息
- [ ] 更新用户信息
- [ ] 用户角色管理
- [ ] 用户状态管理

## 学校推荐系统测试

### 推荐生成 (POST /api/recommendations/generate)
- [ ] 基本推荐请求处理
- [ ] GPA匹配算法
- [ ] GRE分数匹配算法
- [ ] GMAT分数匹配算法
- [ ] 地点偏好匹配
- [ ] 学校类型偏好匹配
- [ ] 专业匹配
- [ ] 推荐结果排序正确

### 推荐查询
- [ ] 获取用户历史推荐 (GET /api/recommendations/user/{userId})
- [ ] 获取简化推荐 (GET /api/recommendations/simple/{userId})
- [ ] 获取所有学校列表 (GET /api/recommendations/schools)
- [ ] 获取特定学校详情 (GET /api/recommendations/schools/{id})

### 推荐反馈
- [ ] 提交推荐反馈 (POST /api/recommendations/feedback/{itemId})
- [ ] 标记已申请状态

## AI文书系统测试

### 文书生成 (POST /api/essays/generation/generate)
- [ ] 基本文书生成请求
- [ ] OpenAI API调用成功
- [ ] 生成内容保存正确
- [ ] 用户背景信息整合
- [ ] 学校匹配信息整合
- [ ] 错误处理（API失败）

### 文书润色 (POST /api/essays/polish/polish)
- [ ] 文书润色请求处理
- [ ] 润色类型选择
- [ ] 润色结果返回
- [ ] 原文保留

### 文书管理
- [ ] 获取用户文书列表 (GET /api/essays/user/{userId})
- [ ] 获取特定文书详情 (GET /api/essays/{id})
- [ ] 更新文书内容
- [ ] 删除文书

## 论坛系统测试

### 帖子管理
- [ ] 创建新帖子 (POST /api/forum/posts)
- [ ] 获取帖子列表 (GET /api/forum/posts)
- [ ] 获取特定帖子详情 (GET /api/forum/posts/{id})
- [ ] 搜索帖子功能
- [ ] 分页功能

### 评论系统
- [ ] 添加评论 (POST /api/forum/posts/{postId}/comments)
- [ ] 获取帖子评论
- [ ] 评论权限控制

### 点赞系统
- [ ] 点赞帖子 (POST /api/forum/posts/{postId}/like)
- [ ] 取消点赞
- [ ] 点赞数统计

## 管理员系统测试

### 用户管理
- [ ] 获取所有用户 (GET /api/admin/users)
- [ ] 更新用户角色 (PUT /api/admin/users/{id}/role)
- [ ] 重置用户密码 (PUT /api/admin/users/{id}/reset-password)
- [ ] 创建管理员账户 (POST /api/admin/create-admin)

### 学校管理
- [ ] 添加新学校 (POST /api/admin/schools)
- [ ] 更新学校信息 (PUT /api/admin/schools/{id})
- [ ] 删除学校 (DELETE /api/admin/schools/{id})
- [ ] 批量导入学校数据

### 系统管理
- [ ] 查看系统统计信息
- [ ] 管理员权限验证

## 异常处理测试

### 输入验证
- [ ] 必填字段验证
- [ ] 数据类型验证
- [ ] 数据范围验证
- [ ] 邮箱格式验证

### 错误响应
- [ ] 404错误处理
- [ ] 401认证错误处理
- [ ] 403权限错误处理
- [ ] 500服务器错误处理
- [ ] 验证错误详细信息返回

## 性能测试

### 响应时间
- [ ] 登录响应时间 < 1秒
- [ ] 推荐生成响应时间 < 5秒
- [ ] 文书生成响应时间 < 30秒
- [ ] 数据查询响应时间 < 2秒

### 并发测试
- [ ] 多用户同时登录
- [ ] 多用户同时生成推荐
- [ ] 多用户同时访问论坛

## 安全测试

### 认证安全
- [ ] JWT令牌安全性
- [ ] 密码加密存储
- [ ] 会话管理

### 权限控制
- [ ] 用户只能访问自己的数据
- [ ] 管理员权限正确控制
- [ ] 匿名用户权限限制

### 输入安全
- [ ] SQL注入防护
- [ ] XSS攻击防护
- [ ] CSRF攻击防护

## 集成测试

### 数据库集成
- [ ] 数据持久化正确
- [ ] 事务处理正确
- [ ] 数据一致性

### 外部API集成
- [ ] OpenAI API集成
- [ ] API错误处理
- [ ] API限流处理

## 部署测试

### 开发环境
- [ ] 本地启动成功
- [ ] 配置文件正确加载
- [ ] 日志输出正常

### 生产环境
- [ ] 生产配置正确
- [ ] 环境变量加载
- [ ] 安全配置生效
- [ ] 日志文件写入

## 测试工具推荐

### API测试
- Postman
- Insomnia
- curl命令

### 性能测试
- JMeter
- Artillery
- ab (Apache Bench)

### 数据库测试
- MySQL Workbench
- DBeaver
- 命令行工具

## 测试数据

### 测试用户
```json
{
  "email": "test@example.com",
  "password": "test123",
  "fullName": "测试用户",
  "gpa": 3.8,
  "greScore": 320,
  "targetMajor": "计算机科学"
}
```

### 测试推荐请求
```json
{
  "userId": 1,
  "gpa": 3.8,
  "greScore": 320,
  "greVerbal": 160,
  "greQuantitative": 160,
  "targetMajor": "计算机科学",
  "targetDegree": "MASTER",
  "locationPreferences": ["加利福尼亚州"],
  "recommendationType": "SCHOOL",
  "count": 5
}
``` 