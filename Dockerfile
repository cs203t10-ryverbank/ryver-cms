FROM openjdk:11-jre
WORKDIR /ryver-cms
COPY . /app
ENTRYPOINT ["java","-jar","/app/ryver-cms/target/cms-0.0.1-SNAPSHOT.jar"]