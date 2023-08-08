FROM gradle:8.2-jdk17 AS build
WORKDIR /app
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
COPY src src
RUN ./gradlew clean installDist

FROM openjdk:17-alpine
COPY --from=build /app/build/install/bullish-store .
EXPOSE 8080
ENTRYPOINT ["./bin/bullish-store"]