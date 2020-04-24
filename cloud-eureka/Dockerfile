FROM openjdk:8-jre-alpine

ENV TZ Asiz/Shanghai

RUN mkdir app && mkdir app/lib

ADD build/libs/cloud-eureka-1.0.jar app.jar
ADD build/libs/lib/* lib/

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]