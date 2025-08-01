package com.example.AuthService.Utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Serves as supporting class for authentication and authorization processes.
 * Contains some static methods whose purpose is provide specific values.
 */
@Component
public class AuthUtil {

    /**
     * Used during password generation.
     */
    private final String SYMBOLS = "0123456789abcdefjhijklmnopqrstuvwxyz";

    /**
     * Retrieves email of authenticated user from stored OAuth2AuthenticationToken.
     *
     * @return extracted value as string
     */
    public String getEmail() {
        return getToken().getPrincipal().getAttribute("email");
    }

    /**
     * Retrieves given name of authenticated user from stored OAuth2AuthenticationToken.
     *
     * @return extracted value as string
     */
    public String getGivenName() {
        return getToken().getPrincipal().getAttribute("given_name");
    }

    /**
     * Retrieves family name of authenticated user from stored OAuth2AuthenticationToken.
     *
     * @return extracted value as string
     */
    public String getFamilyName() {
        return getToken().getPrincipal().getAttribute("family_name");
    }

    /**
     * Generates random password of passed length.
     *
     * @param length length of generated password
     * @return password
     */
    public String generatePassword(int length) {
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        }
        return password.toString();
    }

    /**
     * Creates unique login by adding some number to end of passed login.
     *
     * @param login
     * @param postfix number added to the login end
     * @return new login
     */
    public String updateLogin(String login, int postfix) {
        if (postfix == 0) {
            return login;
        }
        return login + postfix;
    }

    /**
     * Retrieves token from SecurityContextHolder.
     *
     * @return authentication token
     */
    private OAuth2AuthenticationToken getToken() {
        return (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

}
