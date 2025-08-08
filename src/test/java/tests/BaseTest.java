package tests;

import clients.AuthClient;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import models.TokenStorage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.Config;
import org.junit.jupiter.api.TestInfo;

import static io.restassured.RestAssured.config;
import static io.restassured.filter.log.LogDetail.ALL;

@Slf4j
public class BaseTest {

    @BeforeAll
    public static void setUpToken(){
        Response response = AuthClient.login(Config.VALID_USERNAME, Config.VALID_PASSWORD);
        String token = response.jsonPath().getString("accessToken");
        TokenStorage.setToken(token);
        // logging when test fail + hide sensitive info
        config = config()
                .logConfig(LogConfig.logConfig()
                        .blacklistHeader("Authorization", "Cookie", "Set-Cookie"));
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    }

    @BeforeEach
    void logTestStart(TestInfo testInfo) {
        log.info("Starting test: {}", testInfo.getDisplayName());
    }
}
