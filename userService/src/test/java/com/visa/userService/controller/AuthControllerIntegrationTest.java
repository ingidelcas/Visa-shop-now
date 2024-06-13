package com.visa.userService.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.visa.lib.DTO.ApiErrorResponse;
import com.visa.userService.model.dto.LoginResponse;
import com.visa.userService.model.dto.LoginAttemptResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class AuthControllerIntegrationTest {

  static final String SIGNUP_URL = "/api/auth/signup";
  static final String LOGIN_URL = "/api/auth/login";
  static final String LOGIN_ATTEMPTS_URL = "/api/auth/loginAttempts";
  private static final int VALIDATION_ERROR_CODE = 400;

  @Autowired
  private WebTestClient webTestClient;

  @Container
  @ServiceConnection
  private static PostgreSQLContainer postgres = new PostgreSQLContainer<>("postgres:13");

  @BeforeEach
  private void cleanup(){
    // logic
  }
  //  Test signup endpoint
  @Test
  public void shouldSignupUser() {
    String request = """
                {
                  "userName": "idelcas",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "12345",
                    "fullName": "idelson Castano",
                    "gender": "male",
                    "phone": "+57 314 374 2593",
                    "username": "idelcas@hotmail.com"
                  }
                }
            """;
    webTestClient
        .post().uri(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isCreated();
  }

  @Test
  public void shouldReturnDuplicate_onExistingEmail() {
//    signup user
    String request = """
                {
                  "userName": "mamo",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "23456",
                    "fullName": "mayra martinez",
                    "gender": "female",
                    "phone": "+57 314 374 2593",
                    "username": "mamo@hotmail.com"
                  }
                }
            """;
    webTestClient
        .post().uri(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isCreated();

//    signup another user with duplicate username
    String requestWithSameEmail = """
        {
                  "userName": "lily",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "65656565",
                    "fullName": "lyanna martinez",
                    "gender": "female",
                    "phone": "+57 314 374 2593",
                    "username": "mamo@hotmail.com"
                  }
                }
        """;
    ApiErrorResponse errorResponse = webTestClient
        .post().uri(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(requestWithSameEmail)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(ApiErrorResponse.class)
        .returnResult()
        .getResponseBody();

    assertThat(errorResponse).isNotNull();
    assertThat(errorResponse.errorCode()).isEqualTo(409);
    assertThat(errorResponse.description()).isEqualTo("User with the username address 'sandra@gmail.com' already exists.");
  }

  @Test
  public void shouldReturnBadRequest_WhenSignupRequestIsNotValid() {
    String request = """
        {
                  "userName": "lily",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "65656565",
                    "fullName": "lyanna martinez",
                    "gender": "female",
                    "phone": "+57 314 374 2593",
                    "username": "mamo@hotmail.com"
                  }
                }
        """;
    ApiErrorResponse errorResponse = webTestClient
        .post().uri(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(ApiErrorResponse.class)
        .returnResult()
        .getResponseBody();

    assertThat(errorResponse).isNotNull();
    assertThat(errorResponse.errorCode()).isEqualTo(VALIDATION_ERROR_CODE);
    assertThat(errorResponse.description()).contains(
        "password: Password must be between 6 and 20 characters",
        "username: Invalid username format",
        "name: Name cannot be blank");
  }

  //  Test login endpoint
  @Test
  public void shouldReturnJWTToken_WhenUserIsRegistered() {
    String signupRequest = """
        {
                  "userName": "lily",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "65656565",
                    "fullName": "lyanna martinez",
                    "gender": "female",
                    "phone": "+57 314 374 2593",
                    "username": "mamo@hotmail.com"
                  }
                }
        """;
    webTestClient
        .post().uri(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(signupRequest)
        .exchange()
        .expectStatus()
        .isCreated();

    String loginRequest = """
        {
                  "userName": "lily",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "65656565",
                    "fullName": "lyanna martinez",
                    "gender": "female",
                    "phone": "+57 314 374 2593",
                    "username": "mamo@hotmail.com"
                  }
                }
        """;
    LoginResponse loginResponse = webTestClient
        .post().uri(LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(loginRequest)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(LoginResponse.class)
        .returnResult()
        .getResponseBody();

    assertThat(loginResponse).isNotNull();
    assertThat(loginResponse.token()).isNotBlank();
  }

  @Test
  public void shouldReturnBadCredential() {
    String signupRequest = """
        {
                  "userName": "lily",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "65656565",
                    "fullName": "lyanna martinez",
                    "gender": "female",
                    "phone": "+57 314 374 2593",
                    "username": "mamo@hotmail.com"
                  }
                }
        """;
    webTestClient
        .post().uri(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(signupRequest)
        .exchange()
        .expectStatus()
        .isCreated();

    String loginRequestWithWrongPassword = """
        {
          "username": "idelcas",
          "password": "12345678910"
        }
        """;
    ApiErrorResponse errorResponse = webTestClient
        .post().uri(LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(loginRequestWithWrongPassword)
        .exchange()
        .expectStatus()
        .isUnauthorized()
        .expectBody(ApiErrorResponse.class)
        .returnResult()
        .getResponseBody();

    assertThat(errorResponse).isNotNull();
    assertThat(errorResponse.errorCode()).isEqualTo(401);
    assertThat(errorResponse.description()).isEqualTo("Invalid username or password");
  }

  @Test
  public void shouldReturnUnauthorized_WhenUserNotRegistered() {
    String request = """
        {
          "username": "idelcas",
          "password": "123456"
        }
        """;
    ApiErrorResponse errorResponse = webTestClient
        .post().uri(LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus()
        .isUnauthorized()
        .expectBody(ApiErrorResponse.class)
        .returnResult()
        .getResponseBody();

    assertThat(errorResponse).isNotNull();
    assertThat(errorResponse.errorCode()).isEqualTo(401);
    assertThat(errorResponse.description()).isEqualTo("User does not exist, username: sara@gmail.com");
  }

  //  Test loginAttempts endpoint
  @Test
  public void shouldReturnLoginAttempts_WhenUserIsRegistered() {
    String signupRequest = """
        {
                  "userName": "lily",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "65656565",
                    "fullName": "lyanna martinez",
                    "gender": "female",
                    "phone": "+57 314 374 2593",
                    "username": "mamo@hotmail.com"
                  }
                }
        """;
    webTestClient
        .post().uri(SIGNUP_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(signupRequest)
        .exchange()
        .expectStatus()
        .isCreated();

    String loginRequest = """
        {
                  "userName": "lily",
                  "password": "123456",
                  "roles": ["admin"],
                  "person": {
                    "dni": "65656565",
                    "fullName": "lyanna martinez",
                    "gender": "female",
                    "phone": "+57 314 374 2593",
                    "username": "mamo@hotmail.com"
                  }
                }
        """;
    LoginResponse loginResponse = webTestClient
        .post().uri(LOGIN_URL)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(loginRequest)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(LoginResponse.class)
        .returnResult()
        .getResponseBody();
    assertThat(loginResponse).isNotNull();
    assertThat(loginResponse.token()).isNotBlank();

    List<LoginAttemptResponse> loginAttemptsResponse = webTestClient
        .get().uri(LOGIN_ATTEMPTS_URL)
        .header("Authorization", "Bearer " + loginResponse.token())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBodyList(LoginAttemptResponse.class)
        .returnResult()
        .getResponseBody();

    assertThat(loginAttemptsResponse).isNotNull();
    assertThat(loginAttemptsResponse).isNotEmpty();

    LoginAttemptResponse firstLoginAttempt = loginAttemptsResponse.get(0);
    assertThat(firstLoginAttempt).isNotNull();
    assertThat(firstLoginAttempt.createdAt()).isNotNull();
    assertThat(firstLoginAttempt.success()).isTrue();
  }

  @Test
  public void shouldReturnUnauthorized_withNoAuthorizationHeader() {
    webTestClient
        .get().uri(LOGIN_ATTEMPTS_URL)
//        .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaW5hQGdtYWlsLmNvbSIsImlhdCI6MTcwMjMwMjE0MCwiZXhwIjoxNzAyMzA1NzQwfQ.P0dlSC385lgtyRAr9Ako_hocxa2CvBV_hPAj-RjNtTw")
        .exchange()
        .expectStatus()
        .isForbidden();

//    String errorResponse = webTestClient
//        .get().uri(LOGIN_ATTEMPTS_URL)
////        .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtaW5hQGdtYWlsLmNvbSIsImlhdCI6MTcwMjMwMjE0MCwiZXhwIjoxNzAyMzA1NzQwfQ.P0dlSC385lgtyRAr9Ako_hocxa2CvBV_hPAj-RjNtTw")
//        .exchange()
//        .expectStatus()
//        .isForbidden()
//        .expectBody(String.class)
//        .returnResult()
//        .getResponseBody();
//
//    assertThat(errorResponse).isNotNull();
//    assertThat(errorResponse).isEqualTo("{\"errorCode\":401,\"description\":\"Access denied: Authorization header is required.\"}");
  }

  @Test
  public void shouldReturnUnauthorized_withEmptyAuthorizationHeader() {
    webTestClient
        .get().uri(LOGIN_ATTEMPTS_URL)
        .header("Authorization", " ")
        .exchange()
        .expectStatus()
        .isForbidden();

//    String errorResponse = webTestClient
//        .get().uri(LOGIN_ATTEMPTS_URL)
//        .header("Authorization", " ")
//        .exchange()
//        .expectStatus()
//        .isUnauthorized()
//        .expectBody(String.class)
//        .returnResult()
//        .getResponseBody();
//
//    assertThat(errorResponse).isNotNull();
//    assertThat(errorResponse).isEqualTo("{\"errorCode\":401,\"description\":\"Access denied: Authorization header is required.\"}");
  }
}
