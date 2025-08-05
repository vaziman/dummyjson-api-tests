package tests;

import clients.AuthClient;
import clients.UserClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import models.TokenStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.Config;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
public class LoginTest extends BaseTest {

    @Tag("auth")
    @DisplayName("TC001 Login with valid credentials")
    @Test
    public void loginWithValidCredentials_shouldReturnAccessToken() {
        Response response = AuthClient.login(Config.VALID_USERNAME, Config.VALID_PASSWORD);

        response.then()
                .statusCode(200)
                .body("accessToken", notNullValue());

        String token = response.jsonPath().getString("accessToken");
        log.info("Access token: {}", token);
    }

    @Tag("auth")
    @DisplayName("TC002 Login with invalid credentials")
    @Test
    public void loginWithInvalidCredentials_shouldReturn400() {
        Response response = AuthClient.login(Config.INVALID_USERNAME, Config.INVALID_PASSWORD);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Invalid credentials"));
    }

    @Tag("auth")
    @DisplayName("TC003 Login with empty credentials")
    @Test
    public void loginWithEmptyCredentials_shouldReturn400() {
        Response response = AuthClient.login(Config.EMPTY_STRING, Config.EMPTY_STRING);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Username and password required"));
    }

    @Tag("auth")
    @DisplayName("TC004 Get current user with valid token")
    @Test
    public void GetCurrentAuthUserWithValidToken_shouldReturn200() {
        Response response = UserClient.getCurrentUser(TokenStorage.getToken());

        response.then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("username", equalTo(Config.VALID_USERNAME));

    }

    @Tag("auth")
    @DisplayName("TC005 Get current user with invalid/expired token")
    @Test
    public void GetCurrentAuthUserWithInvalidToken_shouldReturn400() {
        Response response = UserClient.getCurrentUser(Config.EMPTY_STRING);

        response.then()
                .statusCode(401)
                .body("message", equalTo("Invalid/Expired Token!"));
    }
}
