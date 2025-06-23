FROM gradle:8.7-jdk17 AS builder
WORKDIR /build

# 상대 경로 기준: 현재 Dockerfile과 동일 디렉토리에 build.gradle, settings.gradle이 있다고 가정합니다.
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true

# 전체 소스 복사
COPY . /build
RUN gradle build -x test --parallel

# ---- 애플리케이션 런타임 이미지 ----
FROM openjdk:17-ea-4-jdk-slim
WORKDIR /secreto

# 로그 디렉터리 생성 (nobody가 쓸 수 있게 소유권 변경)
RUN mkdir -p /secreto/logs && chown nobody:nogroup /secreto/logs

# 빌더 이미지에서 jar 파일만 복사 (아래 경로에 jar가 생성됨을 염두, JAR 이름/경로 필요시 맞게 수정 필요)
COPY --from=builder /build/build/libs/secreto-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

USER nobody

ENTRYPOINT ["java", "-jar", "-Djava.security.egd=file:/dev/./urandom", "-Dsun.net.inetaddr.ttl=0", "secreto-0.0.1-SNAPSHOT.jar"]