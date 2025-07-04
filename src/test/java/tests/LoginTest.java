package tests;

import clients.AuthClient;
import clients.UserClient;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import models.TokenStorage;
import org.junit.jupiter.api.Test;
import utils.Config;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class LoginTest extends BaseTest {

    @Test
    public void loginShouldReturnToken() {
        Response response = AuthClient.login(Config.VALID_USERNAME, Config.VALID_PASSWORD);
        String token = response.jsonPath().getString("accessToken");
        TokenStorage.setToken(token);
        log.info("Access token: {}", token);
        log.info("Full response: {}", response.asString());

        response.then()
                .statusCode(200)
                .body("accessToken", notNullValue());
    }

    @Test
    public void loginWithInvalidCredentials_shouldReturn401() {
        Response response = AuthClient.login(Config.INVALID_USERNAME, Config.INVALID_PASSWORD);

        response.then().statusCode(400)
                .body("message", equalTo("Invalid credentials"));
    }

    @Test
    public void loginWithEmptyCredentials_shouldReturn400() {
        Response response = AuthClient.login(Config.EMPTY_STRING, Config.EMPTY_STRING);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Username and password required"));
    }

    @Test
    public void GetCurrentAuthUserWithValidToken_shouldReturn200() {
        Response response = UserClient.getCurrentUser(TokenStorage.getToken());

        response.then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("username", equalTo(Config.VALID_USERNAME));

    }

    @Test
    public void GetCurrentAuthUserWithInvalidToken_shouldReturn400() {
        Response response = UserClient.getCurrentUser(Config.EMPTY_STRING);

        response.then()
                .statusCode(401)
                .body("message", equalTo("Invalid/Expired Token!"));
    }
}
