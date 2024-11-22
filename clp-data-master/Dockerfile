FROM openjdk:8-jdk-alpine
COPY ./heimao-back/heimao-back-0.0.1-SNAPSHOT.jar /heimao/back/heimao.jar
WORKDIR /heimao/back
RUN mkdir /project/heimao/upload/bulletin
RUN mkdir /project/heimao/upload/csv
RUN mkdir /project/heimao/upload/image
RUN mkdir /project/heimao/upload/temp-task-result
EXPOSE 9123
ENTRYPOINT ["java", "-jar", "/heimao.jar"]
