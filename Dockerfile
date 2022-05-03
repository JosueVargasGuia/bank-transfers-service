FROM openjdk:11
EXPOSE  8093
WORKDIR /app
ADD     ./target/*.jar /app/bank-transfers-service.jar
ENTRYPOINT ["java","-jar","/app/bank-transfers-service.jar"]