FROM eclipse-temurin:17-jre-jammy

RUN useradd -ms /bin/bash appuser
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY --chown=appuser:appuser ${JAR_FILE} app.jar

USER appuser
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
