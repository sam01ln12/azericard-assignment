FROM openjdk:17-oracle
WORKDIR /app

COPY  . .
COPY  *.*ar .
COPY ./build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/api-gateway.jar
RUN ln -sfn *.*ar app
EXPOSE 8080

CMD ["java", "-jar", "api-gateway.jar"]
