-- AdmitGenius 数据库初始化脚本
-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS admitgenius_db;
USE admitgenius_db;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_picture VARCHAR(255),
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    current_school VARCHAR(255),
    major VARCHAR(255),
    gpa FLOAT,
    grade_scale VARCHAR(10),
    
    -- 标准化考试成绩
    toefl_total INT,
    toefl_reading INT,
    toefl_listening INT,
    toefl_speaking INT,
    toefl_writing INT,
    
    ielts_total FLOAT,
    ielts_reading FLOAT,
    ielts_listening FLOAT,
    ielts_speaking FLOAT,
    ielts_writing FLOAT,
    
    gre_combined INT,
    gre_verbal INT,
    gre_quantitative INT,
    gre_analytical FLOAT,
    
    gmat_total INT,
    gmat_verbal INT,
    gmat_quantitative INT,
    gmat_integrated INT,
    gmat_analytical FLOAT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 学校表
CREATE TABLE IF NOT EXISTS schools (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    ranking INT,
    acceptance_rate FLOAT,
    average_gpa FLOAT,
    average_gre_verbal INT,
    average_gre_quantitative INT,
    average_gre_analytical FLOAT,
    average_gmat INT,
    average_toefl INT,
    average_ielts FLOAT,
    description TEXT,
    website VARCHAR(255),
    image_url VARCHAR(255),
    has_scholarship BOOLEAN DEFAULT FALSE,
    tuition_fee DECIMAL(10,2),
    admission_requirements TEXT,
    top_programs JSON,
    research_focus JSON,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 学校项目表
CREATE TABLE IF NOT EXISTS school_programs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    degree_type ENUM('BACHELOR', 'MASTER', 'PHD') NOT NULL,
    department VARCHAR(255),
    duration_years INT,
    tuition_fee DECIMAL(10,2),
    admission_requirements TEXT,
    description TEXT,
    website VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE
);

-- 文书要求表
CREATE TABLE IF NOT EXISTS essay_requirements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id BIGINT,
    program_id BIGINT,
    essay_type VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    prompt TEXT NOT NULL,
    word_limit INT,
    is_required BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE,
    FOREIGN KEY (program_id) REFERENCES school_programs(id) ON DELETE CASCADE
);

-- 文书表
CREATE TABLE IF NOT EXISTS essays (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    school_id BIGINT,
    essay_requirement_id BIGINT,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    word_limit INT,
    essay_type VARCHAR(255),
    status ENUM('DRAFT', 'COMPLETED', 'REVIEWED') DEFAULT 'DRAFT',
    version INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE SET NULL,
    FOREIGN KEY (essay_requirement_id) REFERENCES essay_requirements(id) ON DELETE SET NULL
);

-- 推荐表
CREATE TABLE IF NOT EXISTS recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    request_data JSON,
    total_recommendations INT DEFAULT 0,
    recommendation_type ENUM('SCHOOL', 'PROGRAM') DEFAULT 'SCHOOL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 推荐项表
CREATE TABLE IF NOT EXISTS recommendation_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recommendation_id BIGINT NOT NULL,
    school_id BIGINT,
    program_id BIGINT,
    match_score FLOAT,
    `rank` INT,
    match_reason TEXT,
    feedback TEXT,
    is_applied BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recommendation_id) REFERENCES recommendations(id) ON DELETE CASCADE,
    FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE,
    FOREIGN KEY (program_id) REFERENCES school_programs(id) ON DELETE SET NULL
);

-- 申请统计表
CREATE TABLE IF NOT EXISTS application_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id BIGINT NOT NULL,
    program_id BIGINT,
    admission_year INT,
    total_applications INT DEFAULT 0,
    total_admissions INT DEFAULT 0,
    admission_rate FLOAT,
    average_gpa FLOAT,
    average_gre_verbal INT,
    average_gre_quantitative INT,
    average_gmat INT,
    average_toefl INT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE,
    FOREIGN KEY (program_id) REFERENCES school_programs(id) ON DELETE CASCADE
);

-- 论坛帖子表
CREATE TABLE IF NOT EXISTS forum_posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    like_count INT DEFAULT 0,
    comment_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 评论表
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES forum_posts(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 帖子点赞表
CREATE TABLE IF NOT EXISTS post_likes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_post_user_like (post_id, user_id),
    FOREIGN KEY (post_id) REFERENCES forum_posts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 文档表
CREATE TABLE IF NOT EXISTS documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    filename VARCHAR(255) NOT NULL,
    file_type VARCHAR(50),
    file_path VARCHAR(500),
    file_size BIGINT,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 专家评审表
CREATE TABLE IF NOT EXISTS expert_reviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    essay_id BIGINT NOT NULL,
    reviewer_id BIGINT,
    review_content TEXT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    suggestions TEXT,
    review_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (essay_id) REFERENCES essays(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 创建索引以提高查询性能
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_essays_user_id ON essays(user_id);
CREATE INDEX idx_essays_school_id ON essays(school_id);
CREATE INDEX idx_recommendations_user_id ON recommendations(user_id);
CREATE INDEX idx_recommendation_items_recommendation_id ON recommendation_items(recommendation_id);
CREATE INDEX idx_forum_posts_author_id ON forum_posts(author_id);
CREATE INDEX idx_comments_post_id ON comments(post_id);
CREATE INDEX idx_post_likes_post_id ON post_likes(post_id);
CREATE INDEX idx_school_programs_school_id ON school_programs(school_id);

-- 插入一些示例数据

-- 插入管理员用户
INSERT INTO users (full_name, email, password, role) VALUES 
('系统管理员', 'admin@admitgenius.com', '$2a$10$rH4hD8iC5cxrCm.9YpMXvOq8LJe8AyaBF5nYDbGNM/UX/tKiL6LYy', 'ADMIN');
-- 密码: admin123

-- 插入示例学校数据
INSERT INTO schools (name, location, ranking, acceptance_rate, average_gpa, average_gre_verbal, average_gre_quantitative, average_gmat, description, website, has_scholarship, tuition_fee) VALUES
('斯坦福大学', '加利福尼亚州', 2, 0.042, 3.96, 165, 168, 732, '斯坦福大学是一所位于美国加利福尼亚州的私立研究型大学，以其在科技和创新领域的卓越成就而闻名。', 'https://www.stanford.edu', TRUE, 60000.00),
('麻省理工学院', '马萨诸塞州', 1, 0.067, 3.97, 162, 167, 730, 'MIT是世界领先的科学技术研究和教育机构，在工程、计算机科学和商业领域享有盛誉。', 'https://www.mit.edu', TRUE, 58000.00),
('哈佛大学', '马萨诸塞州', 3, 0.045, 3.98, 163, 166, 728, '哈佛大学是美国历史最悠久的高等教育机构之一，在法律、医学、商业等多个领域都享有世界声誉。', 'https://www.harvard.edu', TRUE, 62000.00),
('加州大学伯克利分校', '加利福尼亚州', 22, 0.17, 3.87, 158, 164, 710, 'UC Berkeley是一所世界顶尖的公立研究型大学，在计算机科学、工程和商业领域表现卓越。', 'https://www.berkeley.edu', TRUE, 45000.00),
('卡内基梅隆大学', '宾夕法尼亚州', 25, 0.15, 3.85, 159, 166, 715, 'CMU在计算机科学、工程和艺术领域享有世界声誉，特别以其技术创新著称。', 'https://www.cmu.edu', TRUE, 59000.00);

-- 插入示例项目数据
INSERT INTO school_programs (school_id, name, degree_type, department, duration_years, tuition_fee, description) VALUES
(1, '计算机科学硕士', 'MASTER', '计算机科学系', 2, 60000.00, '专注于人工智能、机器学习和软件工程的综合性项目'),
(1, '工商管理硕士', 'MASTER', '商学院', 2, 75000.00, '培养商业领导者的综合性MBA项目'),
(2, '计算机科学博士', 'PHD', '电气工程与计算机科学系', 5, 58000.00, '世界领先的计算机科学研究项目'),
(2, '电气工程硕士', 'MASTER', '电气工程与计算机科学系', 2, 58000.00, '电气工程和信号处理的前沿研究'),
(3, '法学博士', 'PHD', '法学院', 3, 70000.00, '美国顶尖的法学教育项目'),
(4, '数据科学硕士', 'MASTER', '数据科学学院', 2, 45000.00, '专注于大数据分析和机器学习的应用项目'),
(5, '人机交互硕士', 'MASTER', '计算机科学学院', 2, 59000.00, '结合技术与设计的创新性项目');

-- 插入示例文书要求
INSERT INTO essay_requirements (school_id, program_id, essay_type, title, prompt, word_limit, is_required) VALUES
(1, 1, '个人陈述', 'Statement of Purpose', 'Please describe your motivation for pursuing a Master''s degree in Computer Science and your career goals.', 500, TRUE),
(1, 2, '个人陈述', 'Statement of Purpose', 'Describe your leadership experience and how you plan to contribute to the MBA community.', 600, TRUE),
(2, 3, '研究陈述', 'Research Statement', 'Outline your research interests and how they align with MIT''s research strengths.', 1000, TRUE),
(3, 5, '个人陈述', 'Personal Statement', 'Explain why you want to study law and how it fits with your career aspirations.', 750, TRUE),
(4, 6, '学术兴趣', 'Statement of Academic Interest', 'Describe your interest in data science and relevant experience.', 500, TRUE);

COMMIT;
