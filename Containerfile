FROM gradle:8.14.3-jdk21-alpine as builder

WORKDIR /app

COPY build.gradle gradlew ./
COPY gradle ./gradle

RUN ./gradlew dependencies

COPY src ./src

RUN ./gradlew build --no-daemon -x test -x integrationTest

FROM eclipse-temurin:21.0.9_10-jre-alpine-3.23

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]