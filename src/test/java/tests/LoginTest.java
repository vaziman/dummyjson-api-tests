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

//        String token = response.jsonPath().getString("accessToken");
//        log.info("Access token: {}", token);
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

    @DisplayName("TC011 Login with XSS payload in username")
    @Test
    public void loginWithXSSPayload_shouldReturn200() {
    Response response = AuthClient.login(Config.ALERT_SCRIPT, Config.VALID_PASSWORD);

    response.then()
            .statusCode(400)
            .body("message", equalTo("Invalid credentials"));
    }

    @DisplayName("TC012 Login using invalid HTTP method")
    @Test
    public void loginWithInvalidHTTPMethod_shouldReturn405() {
        Response response = AuthClient.loginWithGetInsteadOfPost(Config.VALID_USERNAME, Config.VALID_PASSWORD);

        response.then()
                .statusCode(401)
                .body("message", equalTo("Access Token is required"));
    }


    @DisplayName("TC013 Login with Content-Type missing or invalid")
    @Test
    public void loginWithContentTypeMissingOrInvalid_shouldReturn400() {
        Response response = AuthClient.loginWrongContentType(Config.VALID_USERNAME, Config.VALID_PASSWORD);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Username and password required"));
    }

    @DisplayName("TC014 Get current user without Authorization header")
    @Test
    public void getUserWithoutAuthorizationHeader_shouldReturn401() {
        Response response = AuthClient.getCurrentUserWithoutAuthHeader();
        response.then()
                .statusCode(401)
                .body("message", equalTo("Access Token is required"));
    }

    @DisplayName("TC015 Get current user with token in wrong format ")
    @Test
    public void getUserWithWrongFormat_shouldReturn401() {
        Response response = AuthClient.getCurrentUserWithWrongAuthHeader();
        response.then()
                .statusCode(401)
                .body("message", equalTo("Invalid/Expired Token!"));

    }

    @DisplayName("TC016 Login with whitespace-only username/password")
    @Test
    public void loginWithWhitespaceOnlyUsernamePassword_shouldReturn400() {
        Response response = AuthClient.login(Config.WHITESPACE, Config.WHITESPACE);
        response.then()
                .statusCode(400)
                .body("message", equalTo("Invalid credentials"));
    }
}
