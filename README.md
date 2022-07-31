# Numbers API

## How to guide

### Run tests
- To run test require mongodb configured in application.yaml file (info below)
```shell
gradle clean test
```

### Build jar
```shell
gradle clean build
```

### Run program
- This project assume that **mongodb** is running and properly configure in application.yaml. 
- Use below command to set up local docker instance of mongodb
```shell
docker container run --name mongodb -p 27017:27017 -d mongodb

** or use the docker-compose.yml file to create docker instance
```
- Configure application.yaml with docker config
```yaml
spring:
  data:
    mongodb:
      database: numbers
      uri: mongodb://localhost:27017/numbers
```

### Swagger
- This project is documented using swagger. to access swagger run the program and access below page
```
http://localhost:8080/swagger-ui/
```
- there are two version of api accessible in swagger namely v1 and v2. you can access them by selecting the definition

