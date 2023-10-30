FROM openjdk:17-oracle
WORKDIR /app
COPY  . .
COPY  *.*ar .
COPY ./build/libs/ms-user-0.0.1-SNAPSHOT.jar /app/ms-user.jar
RUN ln -sfn *.*ar app
EXPOSE 8180

CMD ["java", "-jar", "ms-user.jar"]