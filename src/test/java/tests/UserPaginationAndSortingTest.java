package tests;


import clients.UserClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import utils.Config;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DisplayName("User Sort API Test")
public class UserPaginationAndSortingTest extends BaseTest {

    @Test
    @DisplayName("Should return user list with default limit (30)")
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
    @DisplayName("Should return empty list of users when limit is -208")
    public void shouldReturnEmptyListOfUsers_WhenLimitIsMinus208() {
        Response response = UserClient.getUsersWithLimitAndSkip(-208, 0);

        response.then()
                .statusCode(200)
                .body("users", hasSize(0));
    }

    @Test
    @DisplayName("Should return users with custom limit and skip")
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
    @DisplayName("Should return users sorted by firstName ASC")
    public void shouldReturnSortedUsers_WhenSortingASC() {
        Response response = UserClient.getUsersSorted("firstName", Config.SORT_BY_ASC);

        List<String> names = response.jsonPath().getList("users.firstName");
        List<String> sortedNames = new ArrayList<>(names);

        sortedNames.sort(String::compareTo);
        assertEquals(sortedNames, names);
    }

    @Test
    @DisplayName("Shuld return users sorted by ID DESC")
    public void shouldReturnSortedUsers_WhenSortingDESC() {
        Response response = UserClient.getUsersSorted("id", Config.SORT_BY_DESC);
        List<String> names = response.jsonPath().getList("users.id");
        List<String> sortedNames = new ArrayList<>(names);
        sortedNames.sort(Comparator.reverseOrder());

        assertEquals(sortedNames, names);

    }
}
