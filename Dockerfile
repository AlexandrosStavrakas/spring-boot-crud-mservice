FROM openjdk:17.0.1
VOLUME /tmp
COPY build/libs/service-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
