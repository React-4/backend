# 1. OpenJDK 이미지를 기반으로 설정
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 생성
WORKDIR /usr/src/app

# 3. 애플리케이션 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 4. 애플리케이션이 사용할 포트 설정 (예: 8080)
EXPOSE 8080

# 5. 애플리케이션 시작 명령어
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
