package models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class TokenStorage {

    @Getter
    private static String token;

    public static void setToken(String token) {
        TokenStorage.token = token;
    }

}
