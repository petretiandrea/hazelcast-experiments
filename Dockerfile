FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /build
COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM eclipse-temurin:17-jdk-alpine as runtime

WORKDIR /app
COPY --from=build /build/build/libs/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT [ "java","-jar","/app/app.jar" ]