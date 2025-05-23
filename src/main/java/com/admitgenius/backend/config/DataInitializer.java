package com.admitgenius.backend.config;

import com.admitgenius.backend.model.*;
import com.admitgenius.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SchoolRepository schoolRepository;
    
    @Autowired
    private SchoolProgramRepository schoolProgramRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeDefaultAdmin();
        initializeSampleSchools();
    }

    private void initializeDefaultAdmin() {
        // 检查是否已存在管理员账户
        if (!userRepository.existsByEmail("admin@admitgenius.com")) {
            User admin = new User();
            admin.setEmail("admin@admitgenius.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("系统管理员");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            
            userRepository.save(admin);
            System.out.println("默认管理员账户已创建: admin@admitgenius.com / admin123");
        }
    }

    private void initializeSampleSchools() {
        // 检查是否已有学校数据
        if (schoolRepository.count() == 0) {
            // 创建示例学校
            School stanford = createSchool(
                "斯坦福大学", "加利福尼亚州", 2, 0.042, 3.96, 
                165, 168, 4.5, 732, 
                "斯坦福大学是一所位于美国加利福尼亚州的私立研究型大学，以其在科技和创新领域的卓越成就而闻名。",
                "https://www.stanford.edu", true, 60000.00, false
            );
            
            School mit = createSchool(
                "麻省理工学院", "马萨诸塞州", 1, 0.067, 3.97,
                162, 167, 4.4, 730,
                "MIT是世界领先的科学技术研究和教育机构，在工程、计算机科学和商业领域享有盛誉。",
                "https://www.mit.edu", true, 58000.00, false
            );
            
            School harvard = createSchool(
                "哈佛大学", "马萨诸塞州", 3, 0.045, 3.98,
                163, 166, 4.5, 728,
                "哈佛大学是美国历史最悠久的高等教育机构之一，在法律、医学、商业等多个领域都享有世界声誉。",
                "https://www.harvard.edu", true, 62000.00, true
            );
            
            School berkeley = createSchool(
                "加州大学伯克利分校", "加利福尼亚州", 22, 0.17, 3.87,
                158, 164, 4.2, 710,
                "UC Berkeley是一所世界顶尖的公立研究型大学，在计算机科学、工程和商业领域表现卓越。",
                "https://www.berkeley.edu", true, 45000.00, false
            );
            
            School cmu = createSchool(
                "卡内基梅隆大学", "宾夕法尼亚州", 25, 0.15, 3.85,
                159, 166, 4.3, 715,
                "CMU在计算机科学、工程和艺术领域享有世界声誉，特别以其技术创新著称。",
                "https://www.cmu.edu", true, 59000.00, false
            );
            
            // 保存学校
            stanford = schoolRepository.save(stanford);
            mit = schoolRepository.save(mit);
            harvard = schoolRepository.save(harvard);
            berkeley = schoolRepository.save(berkeley);
            cmu = schoolRepository.save(cmu);
            
            // 创建示例项目
            createSamplePrograms(stanford, mit, harvard, berkeley, cmu);
            
            System.out.println("示例学校数据已初始化");
        }
    }

    private School createSchool(String name, String location, Integer ranking, Double acceptanceRate,
                               Double averageGPA, Integer averageGREVerbal, Integer averageGREQuant,
                               Double averageGREAW, Integer averageGMAT, String description,
                               String website, Boolean hasScholarship, Double tuitionFee, Boolean isIvyLeague) {
        School school = new School();
        school.setName(name);
        school.setLocation(location);
        school.setRanking(ranking);
        school.setAcceptanceRate(acceptanceRate);
        school.setAverageGPA(averageGPA);
        school.setAverageGREVerbal(averageGREVerbal);
        school.setAverageGREQuant(averageGREQuant);
        school.setAverageGREAW(averageGREAW);
        school.setAverageGMAT(averageGMAT);
        school.setDescription(description);
        school.setWebsite(website);
        school.setHasScholarship(hasScholarship);
        school.setTuitionFee(tuitionFee);
        school.setIsIvyLeague(isIvyLeague);
        school.setTopPrograms(Arrays.asList("计算机科学", "工程学", "商业管理"));
        return school;
    }

    private void createSamplePrograms(School stanford, School mit, School harvard, School berkeley, School cmu) {
        // 斯坦福项目
        createProgram(stanford, "计算机科学硕士", SchoolProgram.DegreeLevel.MASTER, "计算机科学系", 2, 60000.00,
                "专注于人工智能、机器学习和软件工程的综合性项目");
        createProgram(stanford, "工商管理硕士", SchoolProgram.DegreeLevel.MASTER, "商学院", 2, 75000.00,
                "培养商业领导者的综合性MBA项目");
        
        // MIT项目
        createProgram(mit, "计算机科学博士", SchoolProgram.DegreeLevel.PHD, "电气工程与计算机科学系", 5, 58000.00,
                "世界领先的计算机科学研究项目");
        createProgram(mit, "电气工程硕士", SchoolProgram.DegreeLevel.MASTER, "电气工程与计算机科学系", 2, 58000.00,
                "电气工程和信号处理的前沿研究");
        
        // 哈佛项目
        createProgram(harvard, "法学博士", SchoolProgram.DegreeLevel.PHD, "法学院", 3, 70000.00,
                "美国顶尖的法学教育项目");
        
        // 伯克利项目
        createProgram(berkeley, "数据科学硕士", SchoolProgram.DegreeLevel.MASTER, "数据科学学院", 2, 45000.00,
                "专注于大数据分析和机器学习的应用项目");
        
        // CMU项目
        createProgram(cmu, "人机交互硕士", SchoolProgram.DegreeLevel.MASTER, "计算机科学学院", 2, 59000.00,
                "结合技术与设计的创新性项目");
    }

    private void createProgram(School school, String name, SchoolProgram.DegreeLevel degreeLevel, 
                              String department, Integer duration, Double tuitionFee, String admissionRequirements) {
        SchoolProgram program = new SchoolProgram();
        program.setSchool(school);
        program.setName(name);
        program.setDegreeLevel(degreeLevel);
        program.setDepartment(department);
        program.setDuration(duration);
        program.setTuitionFee(tuitionFee);
        program.setAdmissionRequirements(admissionRequirements);
        
        schoolProgramRepository.save(program);
    }
} 