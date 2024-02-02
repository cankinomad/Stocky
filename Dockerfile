FROM amazoncorretto:19
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


#==========Auth Service==============
#docker build --build-arg JAR_FILE=auth-service/build/libs/auth-service-v.0.0.1.jar -t aktasberk/stockylite-auth:v.0.1 .

#==========User Service==============
#docker build --build-arg JAR_FILE=user-service/build/libs/user-service-v.0.0.1.jar -t aktasberk/stocky-user:v.0.3 .

#==========Category Service==============
#docker build --build-arg JAR_FILE=category-service/build/libs/category-service-v.0.0.1.jar -t aktasberk/stockylite-category:v.0.1 .

#==========Product Service==============
#docker build --build-arg JAR_FILE=product-service/build/libs/product-service-v.0.0.1.jar -t aktasberk/stockylite-product:v.0.1 .


#==========Storage Service==============
#docker build --build-arg JAR_FILE=storage-service/build/libs/storage-service-v.0.0.1.jar -t aktasberk/stocky-storage:v.0.1 .

#==========Unit Service==============
#docker build --build-arg JAR_FILE=unit-service/build/libs/unit-service-v.0.0.1.jar -t aktasberk/stocky-unit:v.0.5 .

#==========Api-Gateway Service==============
#docker build --build-arg JAR_FILE=api-gateway-service/build/libs/api-gateway-service-v.0.0.1.jar -t aktasberk/stockylite-api-gateway:v.0.1 .


#==========Config Server==============
#docker build --build-arg JAR_FILE=config-server/build/libs/config-server-v.0.0.1.jar -t aktasberk/stocky-l-config-server:v.0.6 .
