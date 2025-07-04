# RestAssured DummyJSON API Tests

This project demonstrates API test automation using Java, RestAssured, and JUnit 5.  
It covers the testing of various endpoints from the public API [dummyjson.com](https://dummyjson.com), simulating a real-world approach to building test frameworks for backend services.

---

## Features

- Testing of RESTful endpoints for:
  - Authentication (`/auth/login`, `/auth/me`)
  - User operations (`/users`, `/users/{id}`, with pagination, sorting, etc.)
- Request/response modeling with POJOs
- Token handling across test classes
- Logging of test execution
- Best practices for project structure
- Environment-based configuration (through `System.getenv`)

---

## Stack

- Java 17+
- Maven
- RestAssured
- JUnit 5
- Lombok
- SLF4J + Logback

---

## How to Run

### Standard run:

```bash
mvn clean test
Optional: Use custom environment variables
By default, the project uses test data defined in Config.java.
You can override it using environment variables:

BASE_URL

VALID_USERNAME

VALID_PASSWORD

Example:

BASE_URL=https://dummyjson.com \
VALID_USERNAME=emilys \
VALID_PASSWORD=emilyspass \
mvn test

 ## Project Structure

src/test/java
├── clients        # API requests (AuthClient, UserClient)
├── models         # POJOs for request/response
├── tests          # Test classes with JUnit
├── utils          # Config and token storage


## Sample Test Case

@Test
public void loginShouldReturnToken() {
    Response response = AuthClient.login(Config.VALID_USERNAME, Config.VALID_PASSWORD);

    response.then()
        .statusCode(200)
        .body("accessToken", notNullValue());
}

## Notes

No sensitive data is included in this repository.
Tests work out of the box using dummyjson.com — no setup required.

## Author
Developed by Mikhail Belenko
