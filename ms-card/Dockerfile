FROM openjdk:17-oracle
WORKDIR /app
COPY  . .
COPY  *.*ar .
COPY ./build/libs/ms-card-0.0.1-SNAPSHOT.jar /app/ms-card.jar
RUN ln -sfn *.*ar app
EXPOSE 8380

CMD ["java", "-jar", "ms-card.jar"]