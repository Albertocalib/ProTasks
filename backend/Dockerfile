FROM openjdk:11-jdk-slim as build
WORKDIR /app
COPY . .
RUN bash mvnw package -DskipTests

FROM openjdk:11-jre-slim
COPY --from=build /app/target/backend-*.jar backend.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/backend.jar"]
