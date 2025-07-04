package tests;

import clients.UserClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.in;

public class UserTest extends BaseTest {

    @Test
    public void shouldReturnListOfUsersWithShowingLimit30() {
        Response response = UserClient.getAllUsers();

        response.then()
                .statusCode(200)
                .body("total", equalTo(208))
                .body("limit", equalTo(30));
    }

    @Test
    public void shouldReturnListOfUsersWithShowingLimit50AndSkip10() {
        Response response = UserClient.getUsersWithLimitAndSkip(50, 10);

        response.then()
                .statusCode(200)
                .body("limit", equalTo(50))
                .body("skip", equalTo(10));

    }

    @Test
    public void shouldReturnUserWithValidId() {
        Response response = UserClient.getUserWithId(60);

        response.then()
                .statusCode(200)
                .body("id", equalTo(60))
                .body("firstName", equalTo("Lillian"))
                .body("lastName", equalTo("Simmons"));
    }

    @Test
    public void shouldReturnUserWithInvalidId() {
        int invalidId = -1;
        Response response = UserClient.getUserWithId(invalidId);
        response.then()
                .statusCode(404)
                .body("message", equalTo("User with id '" + invalidId + "' not found"));
    }
}
