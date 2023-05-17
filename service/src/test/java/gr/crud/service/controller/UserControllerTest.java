package gr.crud.service.controller;

import gr.crud.service.entity.User;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private Integer localServerPort;

    private RequestSpecification requestSpecification;


    @BeforeEach
    public void setUpAbstractIntegrationTest() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        requestSpecification = new RequestSpecBuilder()
                .setPort(localServerPort)
                .addHeader(
                        HttpHeaders.CONTENT_TYPE,
                        MediaType.APPLICATION_JSON_VALUE
                )
                .build();
    }

    @Test
    @Order(-1)
    void getAllUsers_ShouldReturnSuccessAndList() {
        List<User> users = RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList("");

        Assertions.assertThat(users).hasSize(5);
    }

    @Test
    @Order(-1)
    void getUserById_ShouldReturnSuccess() {
        User[] users = RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user")
                .then()
                .extract()
                .as(User[].class);

        User foundUser = RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user/".concat(users[0].getId().toString()))
                .then()
                .extract()
                .as(User.class);

        Assertions.assertThat(users[0]).isEqualTo(foundUser);
    }

    @Test
    @Order(-1)
    void getUserById_ShouldReturnFail() {
        List<UUID> existingUuids = Arrays.stream(RestAssured
                        .given(requestSpecification)
                        .when()
                        .get("/api/user")
                        .then()
                        .extract()
                        .as(User[].class))
                        .map(User::getId)
                        .toList();

        UUID uuid = UUID.randomUUID();
        while (existingUuids.contains(uuid)) {
            uuid = UUID.randomUUID();
        }

        RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user/".concat(uuid.toString()))
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    @Order(500)
    void deleteUserById_ShouldReturnSuccess() {
        User[] users = RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user")
                .then()
                .extract()
                .as(User[].class);

        Assertions.assertThat(users).hasSize(5);

        RestAssured
                .given(requestSpecification)
                .when()
                .delete("/api/user/".concat(users[0].getId().toString()))
                .then()
                .statusCode(OK.value());

        users = RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user")
                .then()
                .extract()
                .as(User[].class);

        Assertions.assertThat(users).hasSize(4);
    }

    @Test
    @Order(-1)
    void deleteUserById_ShouldReturnFail() {
        List<UUID> existingUuids = Arrays.stream(RestAssured
                        .given(requestSpecification)
                        .when()
                        .get("/api/user")
                        .then()
                        .extract()
                        .as(User[].class))
                .map(User::getId)
                .toList();

        UUID uuid = UUID.randomUUID();
        while (existingUuids.contains(uuid)) {
            uuid = UUID.randomUUID();
        }

        RestAssured
                .given(requestSpecification)
                .when()
                .delete("/api/user/".concat(uuid.toString()))
                .then()
                .statusCode(NOT_FOUND.value());
    }
}
