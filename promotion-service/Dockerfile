FROM eclipse-temurin:23-jdk-alpine AS build
WORKDIR /app

COPY . .

RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]