package clients;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Config;

import static io.restassured.RestAssured.given;

public class UserClient {

    public static Response getCurrentUser(String token) {
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/auth/me");
    }

    public static Response getAllUsers() {
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get("/users");
    }

    public static Response getUsersWithLimitAndSkip(int limit, int skip) {
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get("/users?limit=" + limit + "&skip=" + skip);
    }

    public static Response getUserWithId(int id) {
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get("/users/" + id);
    }

    public static Response getUsersWithName(String name) {
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get("/users/search?q=" + name);
    }

    public static Response getUsersSorted(String sortedBy, String order) {
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .when()
                .get("/users?sortBy=" + sortedBy + "&order=" + order);

    }
}
