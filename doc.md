# AdmitGenius 后端文档

## 1. 项目概述

AdmitGenius 是一个智能留学申请助手系统的后端服务，使用Java Spring Boot框架构建。该系统提供用户管理、AI文书生成与润色、学校推荐、论坛交流等核心功能，帮助用户完成留学申请流程。

## 2. 技术栈

- **框架**: Spring Boot 3.4.3
- **Java版本**: 17
- **数据库**: MySQL (支持PostgreSQL)
- **安全认证**: Spring Security + JWT
- **ORM**: Spring Data JPA + Hibernate
- **AI集成**: OpenAI GPT API
- **构建工具**: Maven
- **其他依赖**: Lombok, Validation, DevTools

## 3. 核心功能模块

### 3.1 用户管理系统
- 用户注册、登录
- JWT令牌认证
- 用户信息管理
- 角色权限控制

### 3.2 AI文书系统
- **文书生成**: 基于用户背景信息和学校要求生成个性化文书
- **文书润色**: 对现有文书进行语法、结构、风格优化
- **文书管理**: 文书的创建、编辑、删除、查询

### 3.3 智能推荐系统
- 基于用户背景的学校推荐
- 支持多维度匹配算法
- 推荐反馈收集与分析

### 3.4 论坛交流系统
- 帖子发布与浏览
- 评论互动
- 点赞功能
- 分页查询支持

### 3.5 管理员系统
- 学校数据管理
- 用户管理
- 系统配置

## 4. API 端点详情

### 4.1 认证相关 (`/api/auth`)

#### 4.1.1 用户登录
```
POST /api/auth/signin
```

**请求体结构:**
```json
{
  "email": "用户邮箱",
  "password": "用户密码"
}
```

**响应:**
```json
{
  "accessToken": "JWT令牌",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "fullName": "用户姓名",
    "email": "用户邮箱",
    "role": "USER"
  }
}
```

#### 4.1.2 用户注册
```
POST /api/auth/signup
```

**请求体结构:**
```json
{
  "name": "用户姓名",
  "email": "用户邮箱",
  "password": "密码",
  "profilePicture": "头像URL（可选）",
  "undergraduateSchool": "本科院校（可选）",
  "gpa": 3.8,
  "greScore": 320,
  "gmatScore": 710,
  "role": "USER"
}
```

### 4.2 用户管理 (`/api/users`)

#### 4.2.1 用户注册 (备用接口)
```
POST /api/users/register
```

#### 4.2.2 用户登录 (备用接口)
```
POST /api/users/login
```

#### 4.2.3 获取用户信息
```
GET /api/users/{id}
```

#### 4.2.4 更新用户信息
```
PUT /api/users/{id}
```

**请求体结构:**
```json
{
  "fullName": "更新的姓名",
  "email": "更新的邮箱",
  "currentSchool": "当前学校",
  "gpa": 3.9,
  "greVerbal": 160,
  "greQuantitative": 170,
  "toeflTotal": 105
}
```

### 4.3 文书管理 (`/api/essays`)

#### 4.3.1 创建文书
```
POST /api/essays
```

**请求体结构:**
```json
{
  "title": "文书标题",
  "content": "文书内容",
  "userId": 1,
  "schoolId": 42,
  "wordLimit": 500,
  "essayType": "个人陈述"
}
```

#### 4.3.2 获取用户所有文书
```
GET /api/essays/user/{userId}
```

#### 4.3.3 获取特定文书
```
GET /api/essays/{id}
```

#### 4.3.4 更新文书
```
PUT /api/essays/{id}
```

#### 4.3.5 删除文书
```
DELETE /api/essays/{id}
```

### 4.4 文书生成 (`/api/essays/generation`)

#### 4.4.1 AI文书生成
```
POST /api/essays/generation
```

**请求体结构:**
```json
{
  "userId": 1,
  "schoolId": 42,
  "essayRequirementId": 15,
  "major": "计算机科学",
  "degree": "硕士",
  "currentSchool": "北京大学",
  "gpa": 3.8,
  "gradeScale": "4.0",
  
  "toeflScore": 105,
  "toeflReading": 28,
  "toeflListening": 27,
  "toeflSpeaking": 24,
  "toeflWriting": 26,
  
  "greTotal": 325,
  "greVerbal": 155,
  "greQuantitative": 170,
  "greAnalytical": 4.5,
  
  "researchExperience": ["参与语义分析研究项目"],
  "workExperience": ["科技公司实习6个月"],
  "awards": ["优秀毕业生", "编程竞赛一等奖"],
  
  "strengths": ["解决问题能力强", "善于团队协作"],
  "careerGoals": ["成为AI专家"],
  "personalStatement": "我的研究经历激发了我对AI的兴趣...",
  
  "whyThisSchool": "贵校在AI领域的卓越研究...",
  "specificProfessorsOfInterest": ["张教授（机器学习）"],
  "specificCoursesOfInterest": ["高级深度学习"]
}
```

### 4.5 文书润色 (`/api/essays/polish`)

#### 4.5.1 润色文书
```
POST /api/essays/polish
```

**请求体结构:**
```json
{
  "essayId": 123,
  "userId": 1,
  "originalContent": "原始文书内容",
  "improveStructure": true,
  "enhanceLanguage": true,
  "checkGrammar": true,
  "reduceClichés": true,
  "addExamples": false,
  "focusPoints": ["突出领导能力", "强调创新思维"],
  "avoidPoints": ["避免过度谦虚"],
  "targetAudience": "招生委员会",
  "targetWordCount": 500,
  "tonePreference": "专业且个人化"
}
```

#### 4.5.2 润色现有文书
```
POST /api/essays/polish/{essayId}
```

#### 4.5.3 获取润色建议
```
GET /api/essays/polish/suggestions/{essayId}
```

### 4.6 学校推荐 (`/api/recommendations`)

#### 4.6.1 生成推荐
```
POST /api/recommendations/generate
```

**请求体结构:**
```json
{
  "userId": 1,
  "gpa": 3.8,
  "undergraduateSchool": "北京大学",
  "toeflScore": 105,
  "greScore": 325,
  "greVerbal": 155,
  "greQuantitative": 170,
  "greAnalytical": 4.5,
  
  "targetMajor": "计算机科学",
  "targetDegree": "硕士",
  "locationPreferences": ["加利福尼亚", "马萨诸塞"],
  "schoolTypePreferences": ["研究型大学"],
  
  "recommendationType": "PROGRAM",
  "count": 10,
  
  "researchWeight": 5,
  "internshipWeight": 3,
  "publicationWeight": 4,
  
  "includeScholarshipInfo": true,
  "includeTuitionInfo": true,
  "includeAdmissionStatistics": true
}
```

#### 4.6.2 获取用户推荐
```
GET /api/recommendations/user/{userId}
```

#### 4.6.3 提交推荐反馈
```
POST /api/recommendations/feedback/{itemId}
```

**请求体结构:**
```json
{
  "feedback": "推荐很符合期望",
  "applied": true
}
```

#### 4.6.4 获取简化推荐
```
GET /api/recommendations/simple/{userId}
```

#### 4.6.5 获取所有学校
```
GET /api/recommendations/schools
```

#### 4.6.6 获取学校详情
```
GET /api/recommendations/schools/{id}
```

### 4.7 论坛系统 (`/api/forum`)

#### 4.7.1 获取帖子列表
```
GET /api/forum/posts?page=0&size=10&keyword=关键词
```

**查询参数:**
- `page`: 页码 (默认0)
- `size`: 每页大小 (默认10)  
- `keyword`: 搜索关键词 (可选)

#### 4.7.2 创建帖子
```
POST /api/forum/posts
```

**请求体结构:**
```json
{
  "title": "帖子标题",
  "content": "帖子内容"
}
```

#### 4.7.3 获取帖子详情
```
GET /api/forum/posts/{postId}
```

#### 4.7.4 添加评论
```
POST /api/forum/posts/{postId}/comments
```

**请求体结构:**
```json
{
  "content": "评论内容"
}
```

#### 4.7.5 切换点赞状态
```
POST /api/forum/posts/{postId}/toggle-like
```

### 4.8 管理员接口 (`/api/admin`)

#### 4.8.1 学校管理
```
POST /api/admin/schools    # 创建学校
PUT /api/admin/schools/{id}    # 更新学校
DELETE /api/admin/schools/{id}    # 删除学校
GET /api/admin/schools    # 获取所有学校
```

#### 4.8.2 用户管理
```
GET /api/admin/users    # 获取所有用户
POST /api/admin/users/create-admin    # 创建管理员
PUT /api/admin/users/{userId}/role    # 更新用户角色
POST /api/admin/users/{userId}/reset-password    # 重置密码
```

## 5. 数据模型

### 5.1 核心实体

#### User (用户)
- 基本信息: id, fullName, email, password
- 学术背景: currentSchool, gpa, major
- 标准化考试: toeflTotal, greVerbal, greQuantitative, gmatTotal
- 系统信息: role, status, createdAt, updatedAt

#### School (学校)
- 基本信息: name, location, ranking, website
- 录取信息: acceptanceRate, averageGPA, averageGRE, averageGMAT
- 其他: description, imageUrl, hasScholarship, tuitionFee

#### Essay (文书)
- 内容: title, content, wordLimit, essayType
- 关联: userId, schoolId, essayRequirementId
- 状态: status, version, createdAt, updatedAt

#### ForumPost (论坛帖子)
- 内容: title, content, authorId
- 互动: likeCount, commentCount, comments, likes
- 时间: createdAt, updatedAt

#### Recommendation (推荐)
- 用户信息: userId, requestData
- 推荐结果: recommendationItems
- 统计: totalRecommendations, createdAt

## 6. 安全配置

### 6.1 认证方式
- JWT Token认证
- 支持角色权限控制 (USER, ADMIN)

### 6.2 API安全策略
- 公开接口: 登录、注册、部分论坛查看
- 需要认证: 文书操作、推荐生成、论坛发布
- 管理员权限: 用户管理、学校管理

## 7. 部署配置

### 7.1 数据库配置
```properties
spring.datasource.url=jdbc:mysql:///admitgenius_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 7.2 JWT配置
```properties
app.jwt.secret=YOUR_JWT_SECRET
app.jwt.expiration-ms=86400000
```

### 7.3 OpenAI配置
```properties
openai.api.key=YOUR_OPENAI_API_KEY
```

## 8. 开发指南

### 8.1 项目启动
1. 配置MySQL数据库
2. 更新application.properties中的数据库密码和API密钥
3. 运行 `mvn spring-boot:run`
4. 访问 `http://localhost:7077`

### 8.2 API测试
建议使用Postman或类似工具测试API端点，注意需要在请求头中包含JWT令牌：
```
Authorization: Bearer YOUR_JWT_TOKEN
```

### 8.3 数据库初始化
首次运行时会自动创建数据库表结构，可以通过运行create_table.sql来初始化基础数据。
