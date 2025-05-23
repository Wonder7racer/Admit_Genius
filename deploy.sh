#!/bin/bash

# AdmitGenius 后端部署脚本
# 使用方法: ./deploy.sh [环境] [操作]
# 环境: dev, prod
# 操作: build, run, stop, restart

set -e

# 默认配置
ENVIRONMENT=${1:-dev}
ACTION=${2:-build}
APP_NAME="AdmitGeniusBackEnd"
JAR_FILE="target/${APP_NAME}-0.0.1-SNAPSHOT.jar"
PID_FILE="app.pid"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查Java环境
check_java() {
    if ! command -v java &> /dev/null; then
        log_error "Java 未安装或未在PATH中"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        log_error "需要Java 17或更高版本，当前版本: $JAVA_VERSION"
        exit 1
    fi
    
    log_info "Java版本检查通过: $(java -version 2>&1 | head -n 1)"
}

# 检查Maven环境
check_maven() {
    if ! command -v mvn &> /dev/null; then
        log_error "Maven 未安装或未在PATH中"
        exit 1
    fi
    
    log_info "Maven版本: $(mvn -version | head -n 1)"
}

# 构建应用
build_app() {
    log_info "开始构建应用..."
    
    check_java
    check_maven
    
    # 清理并编译
    log_info "清理项目..."
    mvn clean
    
    log_info "编译项目..."
    mvn compile
    
    # 运行测试（可选）
    if [ "$ENVIRONMENT" = "prod" ]; then
        log_info "运行测试..."
        mvn test
    fi
    
    # 打包
    log_info "打包应用..."
    if [ "$ENVIRONMENT" = "prod" ]; then
        mvn package -Pprod -DskipTests
    else
        mvn package -DskipTests
    fi
    
    if [ -f "$JAR_FILE" ]; then
        log_info "构建成功: $JAR_FILE"
    else
        log_error "构建失败: JAR文件不存在"
        exit 1
    fi
}

# 停止应用
stop_app() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat $PID_FILE)
        if ps -p $PID > /dev/null; then
            log_info "停止应用 (PID: $PID)..."
            kill $PID
            
            # 等待进程结束
            for i in {1..30}; do
                if ! ps -p $PID > /dev/null; then
                    break
                fi
                sleep 1
            done
            
            # 强制杀死进程
            if ps -p $PID > /dev/null; then
                log_warn "强制停止应用..."
                kill -9 $PID
            fi
            
            rm -f $PID_FILE
            log_info "应用已停止"
        else
            log_warn "PID文件存在但进程不存在，清理PID文件"
            rm -f $PID_FILE
        fi
    else
        log_info "应用未运行"
    fi
}

# 启动应用
start_app() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat $PID_FILE)
        if ps -p $PID > /dev/null; then
            log_warn "应用已在运行 (PID: $PID)"
            return
        fi
    fi
    
    if [ ! -f "$JAR_FILE" ]; then
        log_error "JAR文件不存在，请先构建应用"
        exit 1
    fi
    
    log_info "启动应用 (环境: $ENVIRONMENT)..."
    
    # 设置JVM参数
    JVM_OPTS="-Xms512m -Xmx1024m"
    if [ "$ENVIRONMENT" = "prod" ]; then
        JVM_OPTS="$JVM_OPTS -Dspring.profiles.active=prod"
    fi
    
    # 启动应用
    nohup java $JVM_OPTS -jar $JAR_FILE > app.log 2>&1 &
    echo $! > $PID_FILE
    
    # 等待应用启动
    log_info "等待应用启动..."
    for i in {1..60}; do
        if curl -s http://localhost:7077/api/system/health > /dev/null; then
            log_info "应用启动成功 (PID: $(cat $PID_FILE))"
            return
        fi
        sleep 2
    done
    
    log_error "应用启动超时"
    exit 1
}

# 重启应用
restart_app() {
    log_info "重启应用..."
    stop_app
    sleep 2
    start_app
}

# 显示状态
show_status() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat $PID_FILE)
        if ps -p $PID > /dev/null; then
            log_info "应用正在运行 (PID: $PID)"
            
            # 检查健康状态
            if curl -s http://localhost:7077/api/system/health > /dev/null; then
                log_info "健康检查: 通过"
            else
                log_warn "健康检查: 失败"
            fi
        else
            log_warn "PID文件存在但进程不存在"
        fi
    else
        log_info "应用未运行"
    fi
}

# 显示日志
show_logs() {
    if [ -f "app.log" ]; then
        tail -f app.log
    else
        log_error "日志文件不存在"
    fi
}

# 主函数
main() {
    log_info "AdmitGenius 后端部署脚本"
    log_info "环境: $ENVIRONMENT, 操作: $ACTION"
    
    case $ACTION in
        build)
            build_app
            ;;
        run|start)
            start_app
            ;;
        stop)
            stop_app
            ;;
        restart)
            restart_app
            ;;
        status)
            show_status
            ;;
        logs)
            show_logs
            ;;
        deploy)
            build_app
            stop_app
            start_app
            ;;
        *)
            echo "使用方法: $0 [环境] [操作]"
            echo "环境: dev, prod"
            echo "操作: build, run, stop, restart, status, logs, deploy"
            exit 1
            ;;
    esac
}

# 执行主函数
main 