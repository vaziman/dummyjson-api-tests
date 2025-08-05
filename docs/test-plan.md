# API Test Plan for DummyJSON using Java + RestAssured

## 1. Objective

The objective of this test plan is to define the strategy and scope for API testing of the [DummyJSON](https://dummyjson.com) platform using Java and RestAssured. The goal is to ensure that the API endpoints behave as expected, handle different inputs correctly, and return consistent and valid responses.

---

## 2. Scope of Testing

### In Scope
- Functional testing of all REST API endpoints
- Verification of HTTP methods: GET, POST, PUT, PATCH, DELETE
- Validation of:
    - Request and response structures
    - Response codes and status messages
    - Data types and field values
    - Search, filter, pagination, and sorting mechanisms
- Authentication (e.g., `/auth/login`)
- Idempotency for PUT/DELETE methods

### Out of Scope
- UI testing
- Performance/load/stress testing
- Security testing

---

## 3. Test Approach

API tests will be implemented in Java using the **RestAssured** library, structured in a modular and maintainable way using:
- JUnit 5 as the test runner
- Maven for build management
- Allure for test reporting
- JSON Schema validation (optional)
- Test data driven from code or external sources (e.g., JSON files)

---

## 4. Test Types

-  Functional Testing
-  Positive & Negative Testing
-  Input Validation Testing
-  Status Code Verification
-  Response Structure & Field Type Validation
-  Edge Case Testing (empty strings, nulls, large numbers, invalid formats)
-  Idempotency Testing
-  Authentication/Authorization Flow (where applicable)

---

## 5. Tools & Technologies

| Tool            | Purpose                      |
|-----------------|------------------------------|
| Java            | Programming language         |
| RestAssured     | API test automation          |
| JUnit 5         | Test framework               |
| Maven           | Build and dependency manager |
| Allure          | Reporting                    |
| GitHub/Git      | Version control              |
| IntelliJ IDEA   | IDE                          |
| Postman (opt.)  | Initial manual API testing   |

---

## 6. Test Environment

- **Base URL:** `https://dummyjson.com`
- **Authentication:** Only required for `/auth/login`
- **Data:** Dummy/fake data provided by the API; no persistence expected
- **Environment:** Public API (no deployment or configuration required)
- **Rate Limiting:** Not enforced

---

## 7. Entry and Exit Criteria

### Entry Criteria
- API is publicly available
- Endpoint documentation is accessible
- Java and RestAssured setup is completed
- Framework and project structure are initialized

### Exit Criteria
- All high-priority endpoints are covered by automated tests
- All tests are executed and results reviewed
- No critical/blocker issues found in test execution
- Test coverage is documented (mapping test cases to endpoints)

---


## 8. Deliverables

- Test Plan (this document)
- Structured and version-controlled RestAssured test suite
- Test coverage documentation (by endpoint and method)
- Allure Test Report
- Bug/issue reports (if any)
- README for setup and execution

---


## 9. References

- [DummyJSON Documentation](https://dummyjson.com)
- [RestAssured GitHub](https://github.com/rest-assured/rest-assured)
- [Allure Framework](https://docs.qameta.io/allure/)

---
