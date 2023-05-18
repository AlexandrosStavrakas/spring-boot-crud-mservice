package gr.crud.service.controller;

import gr.crud.service.entity.User;
import gr.crud.service.model.UserRequest;
import gr.crud.service.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @LocalServerPort
    private Integer localServerPort;

    private RequestSpecification requestSpecification;

    @Autowired
    private UserRepository userRepository;


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

        userRepository.deleteAll();
        userRepository.saveAll(List.of(
                User.builder().email("test1@mail.com").firstname("firstname1").lastname("lastname1").build(),
                User.builder().email("test2@mail.com").firstname("firstname2").lastname("lastname2").build(),
                User.builder().email("test3@mail.com").firstname("firstname3").lastname("lastname3").build(),
                User.builder().email("test4@mail.com").firstname("firstname4").lastname("lastname4").build(),
                User.builder().email("test5@mail.com").firstname("firstname5").lastname("lastname5").build()
        ));
    }

    @Test
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
    void updateUser_ShouldReturnSuccess() {
        User[] users = RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user")
                .then()
                .extract()
                .as(User[].class);

        RestAssured
                .given(requestSpecification)
                .body(new UserRequest("emailUpdate@email.com", "firstnameTestUpdate", "lastnameTestUpdate"))
                .when()
                .put("/api/user/".concat(users[0].getId().toString()))
                .then()
                .statusCode(OK.value());
    }

    @Test
    void updateUser_ShouldReturnFailValidation() {
        User[] users = RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user")
                .then()
                .extract()
                .as(User[].class);

        RestAssured
                .given(requestSpecification)
                .body(new UserRequest(null, "firstnameTest", "lastnameTest"))
                .when()
                .put("/api/user/".concat(users[0].getId().toString()))
                .then()
                .statusCode(NOT_ACCEPTABLE.value());
    }

    @Test
    void updateUser_ShouldReturnFailExistingMail() {
        User[] users = RestAssured
                .given(requestSpecification)
                .when()
                .get("/api/user")
                .then()
                .extract()
                .as(User[].class);

        RestAssured
                .given(requestSpecification)
                .body(new UserRequest("test5@mail.com", "firstnameTest", "lastnameTest"))
                .when()
                .put("/api/user/".concat(users[0].getId().toString()))
                .then()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void createUser_ShouldReturnSuccess() {
        RestAssured
                .given(requestSpecification)
                .body(new UserRequest("email@email.com", "firstnameTest", "lastnameTest"))
                .when()
                .post("/api/user")
                .then()
                .statusCode(OK.value());
    }

    @Test
    void createUser_ShouldReturnFailValidation() {
        RestAssured
                .given(requestSpecification)
                .body(new UserRequest("emailforfail", "firstnameTest", "lastnameTest"))
                .when()
                .post("/api/user")
                .then()
                .statusCode(NOT_ACCEPTABLE.value());
    }

    @Test
    void createUser_ShouldReturnFailExistingMail() {
        RestAssured
                .given(requestSpecification)
                .body(new UserRequest("test5@mail.com", "firstnameTest", "lastnameTest"))
                .when()
                .post("/api/user")
                .then()
                .statusCode(BAD_REQUEST.value());
    }


    @Test
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
