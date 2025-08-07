package clients;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.LoginRequest;
import utils.Config;

import static io.restassured.RestAssured.given;
import static utils.Config.SQL_INJECTION;

public class AuthClient {

    public static Response login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when()
                .post("/auth/login");
    }

    public static Response invalidJsonForLogin(String username, String password) {
        String invalidJson = "{ \"userName\": \"" + username + "\", \"pass\": \"" + password + "\" }";

        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .body(invalidJson)
                .when()
                .post("/auth/login");
    }

    public static Response loginWithExtraFields(String username, String password, String extrafiled) {
        String jsonWithExtraField = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\", \"extrafield\": \"" +extrafiled + "\" }";
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .body(jsonWithExtraField)
                .when()
                .post("/auth/login");
    }

    public static Response loginWithSqlInjection( String password){
        String jsonWithInjection = "{ \"username\": \"" + SQL_INJECTION + "\", \"password\": \"" + password + "\" }";

        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(jsonWithInjection)
                .when()
                .post("/auth/login");
    }
}
