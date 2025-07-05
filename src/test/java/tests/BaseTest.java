package tests;

import clients.AuthClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import models.TokenStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.Config;
import org.junit.jupiter.api.TestInfo;

@Slf4j
public class BaseTest {

    @BeforeAll
    public static void setUpToken(){
        Response response = AuthClient.login(Config.VALID_USERNAME, Config.VALID_PASSWORD);
        String token = response.jsonPath().getString("accessToken");
        TokenStorage.setToken(token);
    }

    @BeforeEach
    void logTestStart(TestInfo testInfo) {
        log.info("Starting test: {}", testInfo.getDisplayName());
    }
}
