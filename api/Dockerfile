FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
COPY gradlew ./
COPY .editorconfig ./
RUN chmod +x ./gradlew

RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew build --parallel --no-daemon
RUN mkdir -p build/extracted && (java -Djarmode=tools -jar build/libs/api.jar extract --layers --launcher --destination build/extracted)

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp

ARG EXTRACTED=/workspace/app/build/extracted

RUN apk update && \
    apk --no-cache add curl tzdata && \
    rm -rf /var/cache/apk/*

ENV TZ=Asia/Seoul
RUN cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime && \
    echo "Asia/Seoul" > /etc/timezone

RUN addgroup -g 1001 -S spring && \
    adduser -u 1001 -S spring -G spring

USER spring:spring

COPY --from=build --chown=spring:spring ${EXTRACTED}/dependencies/ ./
COPY --from=build --chown=spring:spring ${EXTRACTED}/spring-boot-loader/ ./
COPY --from=build --chown=spring:spring ${EXTRACTED}/snapshot-dependencies/ ./
COPY --from=build --chown=spring:spring ${EXTRACTED}/application/ ./

ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]