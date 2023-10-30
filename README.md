# azericard-assignment
This assignment involves the development of a microservices-based system, comprising four key microservices: User, Card, Payment, and Product. Each microservice has specific functionalities. The system's primary goal is to facilitate user authentication, card management, payments, and product information retrieval.

Used technologies : 

micrometer-tracing
liquibase
jwt

Composite for cards table, where masked pan, cvv and expiration date used as primary key. This desicion was made to prevent adding same card more than once


swagger-url : http://localhost:8080/webjars/swagger-ui/index.html

Command to run microservices : docker-compose up --build