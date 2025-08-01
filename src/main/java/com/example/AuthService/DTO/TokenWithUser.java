package com.example.AuthService.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenWithUser extends Token {

    private OauthUser user;

    public TokenWithUser(String token, OauthUser user) {
        super(token);
        this.user = user;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OauthUser {

        private String login;

        private String password;

    }

}
