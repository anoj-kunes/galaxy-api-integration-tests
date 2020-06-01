# Galaxy Api Integration Tests
Integration testing with ticket API provided from galaxy connect.

## Postman API Documentation
API documentation for mock-server deployed on:

- [dev](https://www.getpostman.com/collections/d25297bdc9e7c7537171)


## Release notes:
### 0.0.1
- testing and mocking ticket API from galaxy connect
- using embedded version of mock-server
- initial version with mock-server

### Pre-requisites / Tech
- Java 11
- Gradle 5
- Spring Boot 2.3.0
- Mock-Server 5.10.0
- Lombok

### How to run project
Go to `galaxy-api-tests` and use these commands below:
* use `gradlew clean test` to clean and run tests

### Docs
#### Build pipelines
- TODO

#### Dependencies
- using `mock-server` to mock most of the requests from 3rd party APIs
