package com.example.AuthService.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Serves as response DTO for JWT passing.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    private String token;

}
