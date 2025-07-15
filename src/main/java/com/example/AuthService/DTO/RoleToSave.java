package com.example.AuthService.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Serves as request DTO for passing user login and user role array.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleToSave {

    private String login;
    private String[] roles;

    public String desc() {
        StringBuilder builder = new StringBuilder();
        for (String role : roles) {
            builder.append(format(role)).append(", ");
        }
        if (!builder.isEmpty()) {
            builder.delete(builder.lastIndexOf(","), builder.length() - 1);
        }
        return String.format("{ \"login\":\"%s\", \"roles\":[ %s ] }", login, builder);
    }

    private String format(String string) {
        return String.format("\"%s\"", string);
    }

}
