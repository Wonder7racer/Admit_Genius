# AdmitGenius 后端文档

## 1. 项目概述

本文档概述了 AdmitGenius 后端代码库，包括其结构、核心功能和 REST API 端点。后端使用 Java 和 Spring Boot 框架构建。

## 2. 项目结构

项目遵循标准的 Maven 和 Spring Boot 项目结构：

```
.
├── pom.xml                   # Maven 构建配置文件
├── mvnw                      # Maven 包装器脚本 (Linux/macOS)
├── mvnw.cmd                  # Maven 包装器脚本 (Windows)
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── admitgenius
│   │   │           └── backend
│   │   │               ├── AdmitGeniusBackEndApplication.java  # Spring Boot 应用入口点
│   │   │               ├── config        # 应用配置 (安全, Web 等)
│   │   │               ├── controller    # REST API 控制器
│   │   │               ├── dto           # 数据传输对象
│   │   │               ├── exception     # 自定义异常处理
│   │   │               ├── model         # 数据模型 / 实体
│   │   │               ├── payload       # 请求/响应负载结构
│   │   │               ├── repository    # 数据访问对象 (仓库)
│   │   │               ├── security      # 安全配置和组件
│   │   │               └── service       # 业务逻辑服务
│   │   └── resources
│   │       ├── application.properties    # 应用配置属性文件
│   │       └── ...                     # 其他资源 (例如静态文件, 模板)
│   └── test                    # 单元测试和集成测试
├── target/                     # 构建输出目录
├── .gitignore                # Git 忽略规则
├── README.md                 # 项目 README (如果存在)
└── doc.md                    # 本文档文件
```

## 3. 核心功能


*   **用户管理:** 用户注册、登录、信息获取和更新 (`UserController`, `AuthController`)。
*   **文书管理:**
    *   生成: 根据要求创建文书 (`EssayGenerationController`)。
    *   存储与检索: 保存、获取用户文书 (`EssayController`)。
    *   优化/润色: 改进现有文书 (`EssayController`, `EssayPolishController`)，可能涉及建议功能。
*   **学校推荐:** 为用户生成学校推荐（可能基于用户资料或偏好），并收集反馈 (`RecommendationController`)。包括检索学校信息。
*   **论坛:** 发布主题、查看帖子、添加评论和点赞帖子 (`ForumController`)。
*   **管理员操作:** 管理用户和学校数据 (`AdminController`)。
*   **认证与授权:** 保护 API 端点 (`AuthController`, `security/`)。

## 4. REST API 端点


### 4.1 认证 (`/api/auth`)

*   `POST /api/auth/signin`: 用户登录。
*   `POST /api/auth/signup`: 用户注册。

### 4.2 用户 (`/api/users`)

*   `POST /api/users/register`: (可能与 `/api/auth/signup` 重复?) 用户注册。
*   `POST /api/users/login`: (可能与 `/api/auth/signin` 重复?) 用户登录。
*   `GET /api/users/{id}`: 根据 ID 获取用户详情。
*   `PUT /api/users/{id}`: 根据 ID 更新用户详情。

### 4.3 文书 (`/api/essays`)

*   `POST /api/essays`: 创建新文书。
*   `GET /api/essays/user/{userId}`: 获取特定用户的所有文书。
*   `GET /api/essays/{id}`: 根据 ID 获取特定文书。
*   `POST /api/essays/{id}/optimize`: 优化特定文书。

### 4.4 文书生成 (`/api/essays/generation`)

*   `POST /api/essays/generation`: 生成新文书。
*   `GET /api/essays/generation/requirements`: 获取文书生成要求 (?)。

### 4.5 文书润色 (`/api/essays/polish`)

*   `POST /api/essays/polish`: 润色文书 (请求体中需要包含细节)。
*   `POST /api/essays/polish/{essayId}`: 根据 ID 润色特定文书。
*   `GET /api/essays/polish/suggestions/{essayId}`: 获取特定文书的润色建议。

### 4.6 推荐 (`/api/recommendations`)

*   `POST /api/recommendations/generate`: 生成推荐 (请求体中可能需要用户信息/偏好)。
*   `GET /api/recommendations/user/{userId}`: 获取特定用户的推荐。
*   `POST /api/recommendations/feedback/{itemId}`: 提交对推荐项目的反馈。
*   `GET /api/recommendations/simple/{userId}`: 获取用户的简化推荐 (?)。
*   `GET /api/recommendations/schools`: 获取所有学校的列表。
*   `GET /api/recommendations/schools/{id}`: 根据 ID 获取特定学校的详情。

### 4.7 论坛 (`/api/forum`)

*   `GET /api/forum/posts`: 获取论坛帖子列表。
*   `POST /api/forum/posts`: 创建新的论坛帖子。
*   `GET /api/forum/posts/{id}`: 根据 ID 获取特定论坛帖子。
*   `POST /api/forum/posts/{id}/comments`: 为特定帖子添加评论。
*   `POST /api/forum/posts/{id}/like`: 点赞特定帖子。

### 4.8 管理员 (`/api/admin`)

*   `GET /api/admin/users`: 获取所有用户列表 (需要管理员权限)。
*   `POST /api/admin/schools`: 添加新学校 (需要管理员权限)。
*   `PUT /api/admin/schools/{id}`: 根据 ID 更新学校信息 (需要管理员权限)。
*   `DELETE /api/admin/schools/{id}`: 根据 ID 删除学校 (需要管理员权限)。



## 5. API 请求/响应结构

以下是各个 API 端点的请求体结构和示例内容：

### 5.1 认证 API

#### 5.1.1 用户注册 (`POST /api/auth/signup`)

请求体结构：
```json
{
  "name": "用户姓名",
  "email": "用户邮箱",
  "password": "用户密码"
}
```

示例：
```json
{
  "name": "张三",
  "email": "zhangsan@example.com",
  "password": "securepassword123"
}
```

#### 5.1.2 用户登录 (`POST /api/auth/signin`)

请求体结构：
```json
{
  "email": "用户邮箱",
  "password": "用户密码"
}
```

示例：
```json
{
  "email": "zhangsan@example.com",
  "password": "securepassword123"
}
```

### 5.2 用户 API

#### 5.2.1 用户注册 (`POST /api/users/register`)

请求体结构：
```json
{
  "id": null,
  "email": "用户邮箱",
  "password": "用户密码",
  "fullName": "用户全名",
  "profilePicture": "头像URL（可选）",
  "role": "用户角色（可选）",
  "undergraduateSchool": "本科院校",
  "gpa": 平均成绩（可选）,
  "greScore": GRE总分（可选）,
  "gmatScore": GMAT总分（可选）
}
```

示例：
```json
{
  "email": "zhangsan@example.com",
  "password": "securepassword123",
  "fullName": "张三",
  "undergraduateSchool": "北京大学",
  "gpa": 3.8,
  "greScore": 320,
  "gmatScore": 710
}
```

#### 5.2.2 用户登录 (`POST /api/users/login`)

请求体结构：
```json
{
  "email": "用户邮箱",
  "password": "用户密码"
}
```

示例：
```json
{
  "email": "zhangsan@example.com",
  "password": "securepassword123"
}
```

#### 5.2.3 更新用户信息 (`PUT /api/users/{id}`)

请求体结构：
```json
{
  "email": "用户邮箱（可选）",
  "password": "新密码（可选）",
  "fullName": "用户全名（可选）",
  "profilePicture": "头像URL（可选）",
  "undergraduateSchool": "本科院校（可选）",
  "gpa": 平均成绩（可选）,
  "greScore": GRE总分（可选）,
  "gmatScore": GMAT总分（可选）
}
```

示例：
```json
{
  "fullName": "张三丰",
  "gpa": 3.9,
  "greScore": 325
}
```

### 5.3 文书 API

#### 5.3.1 创建文书 (`POST /api/essays`)

请求体结构：
```json
{
  "title": "文书标题",
  "content": "文书内容",
  "userId": 用户ID,
  "applicationId": 申请ID（可选）,
  "wordLimit": 字数限制（可选）,
  "essayType": "文书类型（可选）"
}
```

示例：
```json
{
  "title": "我的大学申请自述",
  "content": "作为一名热爱计算机科学的高中生...",
  "userId": 1,
  "wordLimit": 500,
  "essayType": "个人陈述"
}
```

#### 5.3.2 优化文书 (`POST /api/essays/{id}/optimize`)

该接口不需要请求体，通过路径参数 `id` 指定要优化的文书。

### 5.4 文书生成 API

#### 5.4.1 生成文书 (`POST /api/essays/generation`)

请求体结构：
```json
{
  "userId": 用户ID,
  "schoolId": 学校ID,
  "essayRequirementId": 文书要求ID,
  
  "major": "专业",
  "degree": "学位",
  "currentSchool": "本科院校",
  "gpa": GPA,
  "gradeScale": "GPA满分制",
  
  "toeflScore": 托福总分,
  "toeflReading": 托福阅读,
  "toeflListening": 托福听力,
  "toeflSpeaking": 托福口语,
  "toeflWriting": 托福写作,
  
  "ieltsScore": 雅思总分,
  "ieltsReading": 雅思阅读,
  "ieltsListening": 雅思听力,
  "ieltsSpeaking": 雅思口语,
  "ieltsWriting": 雅思写作,
  
  "greTotal": GRE总分,
  "greVerbal": GRE语文,
  "greQuantitative": GRE数学,
  "greAnalytical": GRE分析性写作,
  
  "gmatTotal": GMAT总分,
  "gmatVerbal": GMAT语文,
  "gmatQuantitative": GMAT数学,
  "gmatIntegrated": GMAT综合推理,
  "gmatAnalytical": GMAT分析性写作,
  
  "researchExperience": ["研究经历1", "研究经历2"],
  "workExperience": ["工作经历1", "工作经历2"],
  "projectExperience": ["项目经历1", "项目经历2"],
  "volunteerExperience": ["志愿者经历1", "志愿者经历2"],
  "leadershipExperience": ["领导经历1", "领导经历2"],
  "awards": ["奖项1", "奖项2"],
  "publications": ["发表1", "发表2"],
  
  "strengths": ["优势1", "优势2"],
  "weaknesses": ["劣势1", "劣势2"],
  "careerGoals": ["职业目标1", "职业目标2"],
  "personalStatement": "个人陈述",
  "motivationForApplication": "申请动机",
  
  "whyThisSchool": "为什么选择该学校",
  "fitWithProgram": "与项目的匹配度",
  "specificProfessorsOfInterest": ["教授1", "教授2"],
  "specificCoursesOfInterest": ["课程1", "课程2"]
}
```

示例：
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
  
  "gmatTotal": 710,
  
  "researchExperience": ["参与了语义分析研究项目", "完成了机器学习算法的优化研究"],
  "workExperience": ["在科技公司实习6个月", "参与开源项目贡献"],
  "awards": ["本科优秀毕业生", "编程竞赛一等奖"],
  
  "strengths": ["解决问题能力强", "善于团队协作"],
  "careerGoals": ["成为人工智能专家", "开发创新的深度学习应用"],
  "personalStatement": "我的本科研究经历激发了我对人工智能的深入兴趣...",
  "motivationForApplication": "贵校在机器学习领域的卓越研究让我深受启发...",
  
  "whyThisSchool": "作为人工智能领域的顶尖学府...",
  "specificProfessorsOfInterest": ["张教授（机器学习）", "李教授（计算机视觉）"],
  "specificCoursesOfInterest": ["高级深度学习", "计算机视觉与应用"]
}
```

#### 5.4.2 获取文书要求 (`GET /api/essays/generation/requirements`)

此接口通过查询参数传递信息，不需要请求体。

### 5.5 文书润色 API

#### 5.5.1 润色文书 (`POST /api/essays/polish`)

请求体结构：
```json
{
  "essayId": 文书ID（可选，如果是对已有文书润色）,
  "userId": 用户ID,
  "originalContent": "原始文书内容",
  
  "improveStructure": true/false,
  "enhanceLanguage": true/false,
  "checkGrammar": true/false,
  "reduceClichés": true/false,
  "addExamples": true/false,
  
  "focusPoints": ["关注点1", "关注点2"],
  "avoidPoints": ["避免点1", "避免点2"],
  "targetAudience": "目标读者",
  
  "targetWordCount": 目标字数,
  "tonePreference": "语言风格偏好"
}
```

示例：
```json
{
  "userId": 1,
  "originalContent": "我希望能够被贵校录取，因为贵校是一所很好的学校...",
  "improveStructure": true,
  "enhanceLanguage": true,
  "checkGrammar": true,
  "reduceClichés": true,
  
  "focusPoints": ["突出我的领导能力", "强调创新思维"],
  "avoidPoints": ["避免过度谦虚", "不要使用陈词滥调"],
  "targetAudience": "招生委员会",
  
  "targetWordCount": 500,
  "tonePreference": "专业且个人化"
}
```

### 5.6 推荐 API

#### 5.6.1 生成推荐 (`POST /api/recommendations/generate`)

请求体结构：
```json
{
  "userId": 用户ID,
  "gpa": GPA,
  "undergraduateSchool": "本科院校",
  "toeflScore": 托福分数,
  "ieltsScore": 雅思分数,
  "greScore": GRE总分,
  "greVerbal": GRE语文分数,
  "greQuantitative": GRE数学分数,
  "greAnalytical": GRE分析性写作分数,
  "gmatScore": GMAT总分,
  
  "targetMajor": "目标专业",
  "targetDegree": "目标学位（硕士/博士）",
  "locationPreferences": ["地区偏好1", "地区偏好2"],
  "schoolTypePreferences": ["学校类型偏好1", "学校类型偏好2"],
  
  "recommendationType": "推荐类型（SCHOOL/PROGRAM）",
  "count": 推荐数量,
  
  "researchWeight": 研究经历权重,
  "internshipWeight": 实习经历权重,
  "volunteeringWeight": 志愿服务权重,
  "leadershipWeight": 领导经历权重,
  "publicationWeight": 发表论文权重,
  
  "researchExperience": "研究经历摘要",
  "internshipExperience": "实习经历摘要",
  "volunteeringExperience": "志愿服务经历摘要",
  "leadershipExperience": "领导经历摘要",
  "publications": "发表的论文摘要",
  
  "includeScholarshipInfo": true/false,
  "includeTuitionInfo": true/false,
  "includeAdmissionStatistics": true/false,
  "includeMatchExplanation": true/false,
  "includeFacultyInfo": true/false
}
```

示例：
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
  
  "researchExperience": "参与了语义分析研究项目，完成了机器学习算法的优化研究",
  "internshipExperience": "在科技公司实习6个月，负责后端开发",
  "publications": "在国内核心期刊发表论文《机器学习在自然语言处理中的应用》",
  
  "includeScholarshipInfo": true,
  "includeTuitionInfo": true,
  "includeAdmissionStatistics": true,
  "includeMatchExplanation": true,
  "includeFacultyInfo": true
}
```

#### 5.6.2 提交反馈 (`POST /api/recommendations/feedback/{itemId}`)

请求体结构：
```json
{
  "feedback": "反馈内容",
  "applied": true/false
}
```

示例：
```json
{
  "feedback": "这所学校的推荐非常符合我的期望",
  "applied": true
}
```

### 5.7 论坛 API

#### 5.7.1 创建帖子 (`POST /api/forum/posts`)

请求体结构：
```json
{
  "authorId": 作者ID,
  "title": "帖子标题",
  "content": "帖子内容"
}
```

示例：
```json
{
  "authorId": 1,
  "title": "分享我的常青藤申请经验",
  "content": "作为一名刚被哈佛录取的学生，我想分享一下我的申请经验..."
}
```

#### 5.7.2 添加评论 (`POST /api/forum/posts/{id}/comments`)

请求体结构：
```json
{
  "authorId": 作者ID,
  "content": "评论内容"
}
```

示例：
```json
{
  "authorId": 2,
  "content": "感谢分享！请问你的SAT分数是多少？"
}
```

### 5.8 管理员 API

#### 5.8.1 添加新学校 (`POST /api/admin/schools`)

请求体结构：
```json
{
  "name": "学校名称",
  "location": "学校位置",
  "ranking": 排名,
  "acceptanceRate": 录取率,
  "averageGREVerbal": 平均GRE语文,
  "averageGREQuant": 平均GRE数学,
  "averageGREAW": 平均GRE分析性写作,
  "averageGMAT": 平均GMAT,
  "averageGPA": 平均GPA,
  "description": "学校描述",
  "website": "学校网站",
  "imageUrl": "学校图片URL",
  "hasScholarship": 是否提供奖学金,
  "tuitionFee": 学费,
  "admissionRequirements": "录取要求",
  "topPrograms": ["顶尖项目1", "顶尖项目2"]
}
```

示例：
```json
{
  "name": "斯坦福大学",
  "location": "加利福尼亚州",
  "ranking": 2,
  "acceptanceRate": 0.042,
  "averageGREVerbal": 165,
  "averageGREQuant": 168,
  "averageGREAW": 4.8,
  "averageGMAT": 732,
  "averageGPA": 3.96,
  "description": "斯坦福大学是一所位于美国加利福尼亚州的私立研究型大学...",
  "website": "https://www.stanford.edu",
  "imageUrl": "https://example.com/stanford.jpg",
  "hasScholarship": true,
  "tuitionFee": 60000,
  "admissionRequirements": "GRE、托福、本科成绩单、推荐信、个人陈述",
  "topPrograms": ["计算机科学", "商业管理", "电子工程"]
}
```

#### 5.8.2 更新学校信息 (`PUT /api/admin/schools/{id}`)

请求体结构与添加新学校相同，可以只包含需要更新的字段。

示例：
```json
{
  "ranking": 3,
  "acceptanceRate": 0.045,
  "averageGREVerbal": 166
}
```
