FROM adoptopenjdk:17-jdk-hotspot
VOLUME /tmp
COPY build/libs/service-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
