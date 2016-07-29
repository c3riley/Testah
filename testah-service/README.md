# Testah-Service

Testah-service is a restful web service that provides a DAL for persisted test results and metadata. This service also will manage resources for tests for things such as environment availability, VM's, and other devices that tests consume. 

# Development

## Technology Stack
Tomcat
Java 8
Spring Boot
Gradle
Spring Boot Gradle Plugin ( http://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-gradle-plugin.html)


## Getting Started

### Building testah-service
To build the service, there are a few things you can do. 

If you want to just build the project and not run tests, you can run the 'clean assemble' tasks.
```
./gradlew clean assemble
```
If you want to just run the tests, then can you use the 'clean check' tasks.
```
./gradlew clean check
```
To build the project, run the tests, and create an executable jar use the 'clean build' tasks.
```
./gradlew clean build
```

### Running testah-serivce

To launch the service in place, you can use the 'bootRun' task. 
```
./gradlew bootRun
```

To launch the service on a server, run the executable jar. 

### Debugging testah-service

## Todo
## Known Issues