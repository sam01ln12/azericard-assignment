FROM openjdk:17-oracle
WORKDIR /app
COPY  . .
COPY  *.*ar .
COPY ./build/libs/ms-payment-0.0.1-SNAPSHOT.jar /app/ms-payment.jar
RUN ln -sfn *.*ar app
EXPOSE 8480

CMD ["java", "-jar", "ms-payment.jar"]