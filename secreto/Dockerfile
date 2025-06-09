FROM gradle:7.4-jdk17 AS builder

USER root

# 홈 및 빌드 디렉터리와 모든 하위 디렉토리 생성 후 권한 재설정
RUN mkdir -p /home/gradle/build && \
    chown -R gradle:gradle /home/gradle

USER gradle
WORKDIR /home/gradle/build

# (디버깅용) 디렉토리 권한/구조 확인
RUN ls -al /home/gradle
RUN ls -al /home/gradle/build

# 의존성 캐싱
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# 전체 소스 복사 및 빌드
COPY . .
RUN gradle build -x test --no-daemon --parallel

FROM openjdk:17-slim
WORKDIR /app
COPY --from=builder /home/gradle/build/build/libs/docker-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
USER nobody
ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "-Dsun.net.inetaddr.ttl=0", "docker-0.0.1-SNAPSHOT.jar"]