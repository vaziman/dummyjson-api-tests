package tests;

import clients.UserClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.Config;

import static org.hamcrest.Matchers.*;

@Tag("users")
@Slf4j
@DisplayName("User API Tests")
public class UserTest extends BaseTest {



    @Test
    @DisplayName("Should return specific user by valid ID")
    public void shouldReturnUser_WhenIdIsValid() {
        Response response = UserClient.getUserWithId(Config.VALID_USER_ID);

        response.then()
                .statusCode(200)
                .body("id", equalTo(Config.VALID_USER_ID))
                .body("firstName", equalTo(Config.VALID_FIRST_NAME))
                .body("lastName", equalTo(Config.VALID_LAST_NAME));
    }

    @Test
    @DisplayName("Should return 404 when user ID is invalid")
    public void shouldReturn404_WhenUserIdIsInvalid() {
        int invalidId = -1;

        Response response = UserClient.getUserWithId(invalidId);
        response.then()
                .statusCode(404)
                .body("message", equalTo("User with id '" + invalidId + "' not found"));
    }


   @Test
   @DisplayName("Should return users by full name")
    public void shouldReturnUsers_WhenFullNameIsExact() {
        Response response = UserClient.getUsersWithName(Config.VALID_FIRST_NAME);
        response.then()
                .statusCode(200)
                .body("users.firstName", hasItem(equalTo(Config.VALID_FIRST_NAME)));
        log.info("Matched first names: {}", response.jsonPath().getList("users.firstName"));
   }

   @Test
   @DisplayName("Should return users by partial name")
    public void shouldReturnUsers_WhenPartialNameIsGiven() {
        Response response = UserClient.getUsersWithName(Config.NAME_PART);
        response.then()
                .statusCode(200)
                .body("users.firstName", hasItem(containsString("Emily")));
       log.info("Matched partial names: {}", response.jsonPath().getList("users.firstName"));
    }

    @Test
    @DisplayName("Should return users by lowercase full name")
    public void shouldReturnUsers_WhenFirstNameIsLowerCase() {
        Response response = UserClient.getUsersWithName(Config.VALID_NAME_LOWERCASE);
        response.then()
                .statusCode(200)
                .body("users.firstName", hasItem(containsString(Config.VALID_FIRST_NAME)));
        log.info("Matched users: {}", response.jsonPath().getList("users.firstName"));
    }

    @Test
    @DisplayName("Should return users by valid last name")
    public void shouldReturnUsers_WhenLastNameIsIsValid() {
        Response response = UserClient.getUsersWithName(Config.VALID_LAST_NAME);
        response.then()
                .statusCode(200)
                .body("users.lastName", hasItem(containsString(Config.VALID_LAST_NAME)));
        log.info("Matched last names: {}", response.jsonPath().getList("users.lastName"));
    }

    @Test
    @DisplayName("Should return empty list of users when FirstName is invalid")
    public void shouldReturnEmptyList_WhenFirstNameIsInvalid() {
        Response response = UserClient.getUsersWithName(Config.INVALID_FIRST_NAME);
        response.then()
                .statusCode(200)
                .body("users", hasSize(0));
        Integer sizeOfUsersList = response.jsonPath().getList("users").size();
        log.info("List of users: {}", sizeOfUsersList);

    }



}
