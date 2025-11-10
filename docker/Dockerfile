# Этап 1: сборка проекта
FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /build

RUN git clone https://github.com/SavushkinSV/ozon-bot.git . && \
    mvn clean package -DskipTests

# Этап 2: финальный образ
FROM alpine/java:22-jre

WORKDIR /app

COPY --from=build /build/target/oil-bot-0.0.1.jar ./oil-bot.jar

EXPOSE 8080

ENV JAVA_TOOL_OPTIONS="-Xmx512m -XX:MaxMetaspaceSize=512m -XX:CompressedClassSpaceSize=256m"

CMD ["java", "-jar", "kafka-stub.jar", "-b"]