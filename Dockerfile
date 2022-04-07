FROM openjdk:11
EXPOSE 3020
ADD target/demo-0.0.1-SNAPSHOT.jar like-service.jar
ENTRYPOINT ["java","-jar","like-service.jar"]