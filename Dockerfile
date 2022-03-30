FROM openjdk:8
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} like-service.jar
EXPOSE 3020
ENTRYPOINT ["java","-jar","/like-service.jar"]