# REST Assured Spring Boot API Testing Framework

A lightweight API testing framework using REST Assured with Spring Boot for GoRest API.

## What You Need

- Java 17
- Maven

## Quick Setup

### 1. Set Token (First time only)

**Locally:**
Set environment variable `GOREST_TOKEN` with your token from [gorest.co.in](https://gorest.co.in)

**GitHub Actions:**
Add `GOREST_TOKEN` to repo Secrets (Settings → Secrets → Actions)

### 2. Run Tests

**Locally (on your computer):**
```
mvn clean test
```

**On GitHub Actions (automatic):**
- Push your code to GitHub
- Tests run automatically
- View results in Actions tab

You should see 7 tests pass!

## What's Tested

The framework tests basic User CRUD operations:
- Get all users
- Create a user
- Get user by ID
- Update a user
- Partially update a user
- Delete a user
- Complete user workflow (create → read → update → delete)

## Project Structure

```
rest-assured-springboot/
├── .github/workflows/
│   ├── api-tests.yml
│   └── scheduled-tests.yml
├── src/
│   ├── main/java/
│   │   ├── constants/
│   │   ├── endpoints/
│   │   └── models/
│   │       ├── dto/
│   │       └── helpers/
│   └── test/
│       ├── java/tests/
│       └── resources/testdata/
├── pom.xml
└── README.md
```

## Tech Stack

- Java 17
- Spring Boot 3.1.5 (dependency management)
- REST Assured 5.3.2 (API testing)
- JUnit 5 (testing framework)
- Lombok (code generation)
- Jackson (JSON handling)


