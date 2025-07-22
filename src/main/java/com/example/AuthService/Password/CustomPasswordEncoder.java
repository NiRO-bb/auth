package com.example.AuthService.Password;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

/**
 * Represents password encoder for SpringSecurity.
 * Implements encode and matches methods of PasswordEncoder interface.
 * Uses sha256 algorithm for encoding.
 */
public class CustomPasswordEncoder implements PasswordEncoder {

    private final String SYMBOLS = "0123456789abcdef";
    private final int SALT_LENGTH;
    private final int COST_FACTOR;

    public CustomPasswordEncoder() {
        this(16, 10);
    }

    public CustomPasswordEncoder(int costFactor, int saltLength) {
        if (costFactor < 12 || costFactor > 30) {
            throw new IllegalArgumentException("Passed cost factor value must be in [12, 30]");
        }
        if (saltLength < 5 || saltLength > 20) {
            throw new IllegalArgumentException("Passed salt length value must be in [5, 20]");
        }
        COST_FACTOR = costFactor;
        SALT_LENGTH = saltLength;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Entered password must not be null");
        }
        String password = hashPassword(rawPassword.toString(), getSalt(encodedPassword));
        return password.equals(encodedPassword);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Entered password must not be null");
        }
        return hashPassword(rawPassword.toString(), getSalt());
    }

    /**
     * Encodes given (password + salt), then replaces end of hash with salt.
     * Thus, becomes possible to get salt when decoding.
     *
     * @param password
     * @param salt
     * @return hashed salting password
     */
    private String hashPassword(String password, String salt) {
        String saltedPassword = getSha(password + salt);
        long count = (long) Math.pow(2, COST_FACTOR);
        for (long i = 0; i < count; i++) {
            saltedPassword = getSha(saltedPassword);
        }
        return saltedPassword.substring(0, saltedPassword.length() - SALT_LENGTH) + salt;
    }

    /**
     * Returns password value that hashed using SHA256.
     *
     * @param password string must be hashed
     * @return hashed password
     */
    private String getSha(String password) {
        return DigestUtils.sha256Hex(password);
    }

    /**
     * Generates random salt using alphanumeric set (SYMBOLS).
     * Salt length depends on SALT_LENGTH class field value.
     *
     * @return generated salt
     */
    private String getSalt() {
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < SALT_LENGTH) {
            Character c = SYMBOLS.charAt(random.nextInt(SYMBOLS.length()));
            salt.append(c);
        }
        return salt.toString();
    }

    /**
     * Retrieves salt from passed encoded password.
     * For success execution, given password must be encoded with this class 'hashPassword' method.
     *
     * @param encodedPassword contains salt
     * @return extracted salt
     */
    private String getSalt(String encodedPassword) {
        return encodedPassword.substring(encodedPassword.length() - SALT_LENGTH);
    }

}
