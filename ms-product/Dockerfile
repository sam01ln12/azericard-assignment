FROM openjdk:17-oracle
WORKDIR /app
COPY  . .
COPY  *.*ar .
COPY ./build/libs/ms-product-0.0.1-SNAPSHOT.jar /app/ms-product.jar
RUN ln -sfn *.*ar app
EXPOSE 8280

CMD ["java", "-jar", "ms-product.jar"]
