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

@Tag("auth")
@Slf4j
public class LoginTest extends BaseTest {


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


    @DisplayName("TC002 Login with invalid credentials")
    @Test
    public void loginWithInvalidCredentials_shouldReturn400() {
        Response response = AuthClient.login(Config.INVALID_USERNAME, Config.INVALID_PASSWORD);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Invalid credentials"));
    }


    @DisplayName("TC003 Login with empty credentials")
    @Test
    public void loginWithEmptyCredentials_shouldReturn400() {
        Response response = AuthClient.login(Config.EMPTY_STRING, Config.EMPTY_STRING);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Username and password required"));
    }


    @DisplayName("TC004 Get current user with valid token")
    @Test
    public void GetCurrentAuthUserWithValidToken_shouldReturn200() {
        Response response = UserClient.getCurrentUser(TokenStorage.getToken());

        response.then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("username", equalTo(Config.VALID_USERNAME));

    }


    @DisplayName("TC005 Get current user with invalid/expired token")
    @Test
    public void GetCurrentAuthUserWithInvalidToken_shouldReturn400() {
        Response response = UserClient.getCurrentUser(Config.EMPTY_STRING);

        response.then()
                .statusCode(401)
                .body("message", equalTo("Invalid/Expired Token!"));
    }

    @DisplayName("TC006 Login with missing username field")
    @Test
    public void loginWithMissingUsername_shouldReturn400() {
        Response response = AuthClient.login(Config.EMPTY_STRING, Config.INVALID_PASSWORD);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Username and password required"));
    }

    @DisplayName("TC007 Login with missing password field")
    @Test
    public void loginWithMissingPassword_shouldReturn400() {
        Response response = AuthClient.login(Config.VALID_USERNAME, Config.EMPTY_STRING);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Username and password required"));
    }

    @DisplayName("TC008 Login with incorrect request body structure")
    @Test
    public void loginWithIncorrectRequest_shouldReturn400() {
        Response response = AuthClient.invalidJsonForLogin(Config.VALID_USERNAME, Config.VALID_PASSWORD);

        response.then()
                .statusCode(400)
                .body("message", equalTo("Username and password required"));
    }

    @DisplayName("TC009 Login with extra fields in request body")
    @Test
    public void loginWithExtraFields_shouldReturn200() {
        Response response = AuthClient.loginWithExtraFields(Config.VALID_USERNAME, Config.VALID_PASSWORD, Config.INVALID_FIRST_NAME);

        response.then()
                .statusCode(200)
                .body("id", equalTo(1));
    }

    @DisplayName("TC010 Login with SQL injection in username")
    @Test
    public void loginWithSQLInjection_shouldReturn400() {
        Response response = AuthClient.loginWithSqlInjection(Config.VALID_PASSWORD);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Invalid credentials"));
    }
}
