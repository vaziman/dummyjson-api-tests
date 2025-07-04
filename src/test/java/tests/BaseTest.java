package tests;

import clients.AuthClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import models.TokenStorage;
import org.junit.jupiter.api.BeforeAll;
import utils.Config;

@Slf4j
public class BaseTest {

    @BeforeAll
    public static void setUpToken(){
        Response response = AuthClient.login(Config.VALID_USERNAME, Config.VALID_PASSWORD);
        String token = response.jsonPath().getString("accessToken");
        TokenStorage.setToken(token);
    }
}
