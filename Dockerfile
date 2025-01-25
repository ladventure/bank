# 使用 OpenJDK 基础镜像
FROM 079114de2be1
# 设置工作目录
WORKDIR /bank
# 复制项目的 JAR 文件到容器中
COPY target/bank-0.0.1-SNAPSHOT.jar bank.jar
# 暴露应用程序的端口
EXPOSE 8080
# 运行应用程序
CMD ["java", "-jar", "bank.jar"]