package clients;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.LoginRequest;
import utils.Config;

import static io.restassured.RestAssured.given;

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
}
