package tests;

import clients.UserClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.Config;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("PaginationAndSorting")
@Slf4j
@DisplayName("User Sort API Test")
public class UserPaginationAndSortingTest extends BaseTest {

    @Test
    @DisplayName("TC017 Get all users (with default limit 30)")
    public void shouldReturnUsers_WhenLimitIsDefault30() {
        Response response = UserClient.getAllUsers();

        response.then()
                .statusCode(200)
                .body("total", equalTo(Config.TOTAL_USERS))
                .body("limit", equalTo(Config.DEFAULT_LIMIT));

        Integer actualLimit = response.jsonPath().getInt("limit");
        log.info("User list returned with limit: {}",actualLimit);
    }

    @Test
    @DisplayName("TC019 Get users with invalid limit -208")
    public void shouldReturnEmptyListOfUsers_WhenLimitIsMinus208() {
        Response response = UserClient.getUsersWithLimitAndSkip(-208, 0);

        response.then()
                .statusCode(200)
                .body("users", hasSize(0));
    }

    @Test
    @DisplayName("TC018 Get users with custom limit/skip")
    public void shouldReturnUsers_WhenLimitAndSkipAreProvided() {
        Response response = UserClient.getUsersWithLimitAndSkip(50, 10);

        response.then()
                .statusCode(200)
                .body("limit", equalTo(Config.CUSTOM_LIMIT))
                .body("skip", equalTo(Config.CUSTOM_SKIP_VALUE));
        Integer actualLimit = response.jsonPath().getInt("limit");
        Integer actualSkip = response.jsonPath().getInt("skip");

        log.info("User list returned with limit: {} and skip: {}", actualLimit,actualSkip);
    }

    @Test
    @DisplayName("TC020 Get user by valid ID")
    public void shouldReturnUser_WhenIdIsValid() {
        Response response = UserClient.getUserWithId(Config.VALID_USER_ID);

        response.then()
                .statusCode(200)
                .body("id", equalTo(Config.VALID_USER_ID))
                .body("firstName", equalTo(Config.VALID_FIRST_NAME))
                .body("lastName", equalTo(Config.VALID_LAST_NAME));
    }


    @Test
    @DisplayName("TC021 Get user by invalid ID")
    public void shouldReturn404_WhenUserIdIsInvalid() {
        int invalidId = -1;

        Response response = UserClient.getUserWithId(invalidId);
        response.then()
                .statusCode(404)
                .body("message", equalTo("User with id '" + invalidId + "' not found"));
    }


    @Test
    @DisplayName(" TC022 Search user by full first name")
    public void shouldReturnUsers_WhenFullNameIsExact() {
        Response response = UserClient.getUsersWithName(Config.VALID_FIRST_NAME);
        response.then()
                .statusCode(200)
                .body("users.firstName", hasItem(equalTo(Config.VALID_FIRST_NAME)));
//        log.info("Matched first names: {}", response.jsonPath().getList("users.firstName"));
    }

    @Test
    @DisplayName("TC023 Search by partial name")
    public void shouldReturnUsers_WhenPartialNameIsGiven() {
        Response response = UserClient.getUsersWithName(Config.NAME_PART);
        response.then()
                .statusCode(200)
                .body("users.firstName", hasItem(containsString("Emily")));
        log.info("Matched partial names: {}", response.jsonPath().getList("users.firstName"));
    }

    @Test
    @DisplayName("TC024 Search by lowercase name")
    public void shouldReturnUsers_WhenFirstNameIsLowerCase() {
        Response response = UserClient.getUsersWithName(Config.VALID_NAME_LOWERCASE);
        response.then()
                .statusCode(200)
                .body("users.firstName", hasItem(containsString(Config.VALID_FIRST_NAME)));
        log.info("Matched users: {}", response.jsonPath().getList("users.firstName"));
    }

    @Test
    @DisplayName("TC025 Search with no match")
    public void shouldReturnEmptyList_WhenFirstNameIsInvalid() {
        Response response = UserClient.getUsersWithName(Config.INVALID_FIRST_NAME);
        response.then()
                .statusCode(200)
                .body("users", hasSize(0));
        Integer sizeOfUsersList = response.jsonPath().getList("users").size();
        log.info("List of users: {}", sizeOfUsersList);
    }

    @DisplayName("TC026 Sort users ASC ")
    @Test
    public void shouldReturnSortedUsers_WhenSortingASC() {
        Response response = UserClient.getUsersSorted("firstName", Config.SORT_BY_ASC);

        List<String> names = response.jsonPath().getList("users.firstName");
        List<String> sortedNames = new ArrayList<>(names);

        response.then()
                .statusCode(200)
                .body("total", equalTo(Config.TOTAL_USERS));

        sortedNames.sort(String::compareTo);
        assertEquals(sortedNames, names);
    }


    @DisplayName("TC027 Sort users DESC")
    @Test
    public void shouldReturnSortedUsers_WhenSortingDESC() {
        Response response = UserClient.getUsersSorted("id", Config.SORT_BY_DESC);
        List<String> names = response.jsonPath().getList("users.id");
        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort(Comparator.reverseOrder());

        assertEquals(sortedNames, names);

    }
}
