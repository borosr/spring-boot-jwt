# Spring Boot Security JWT with Java 11

[![Build Status](https://travis-ci.org/borosr/spring-boot-jwt.svg?branch=master)](https://travis-ci.org/borosr/spring-boot-jwt)

An example available in [https://borosr-spring-boot-app.herokuapp.com/](https://borosr-spring-boot-app.herokuapp.com/)

## Usage

### Start server
```bash
./mvnw spring-boot:run
```

### 1. Authenticate

#### curl
```bash
curl -X POST http://localhost:8080/oauth/token \
  -H 'Authorization: Basic ${btoa("clientId:secret")}' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=password&username=john&password=123'
```
Response looks like: `{ access_token: string, token_type: string, refresh_token: string, expires_in: number, scope: string, jti: string }`

### 2. Get data from secure page

```bash
curl -X GET http://localhost:8080/secure/ \
  -H 'Authorization: Bearer ${access_token_from_response}'
```

### 3. Check token
Path: `/oauth/check_token` endpoint response token's data

```bash
curl -X POST http://localhost:8080/oauth/check_token \
  -H 'Authorization: Basic ${btoa("clientId:secret")}' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'token=${access_token_from_response}'
```

### 4. Revoke token
Path: `/oauth/token/revoke/`
```bash
curl -X DELETE http://localhost:8080/oauth/token/revoke/ \
  -H 'Authorization: Bearer ${access_token_from_response}'
```

### Test user:
`username: john`
`password: 123`
`clientId: fooClientIdPassword`
`secret: secret`

## Run tests
```bash
./mvnw clean test
```