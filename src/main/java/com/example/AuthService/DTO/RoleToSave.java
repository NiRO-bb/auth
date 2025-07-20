package com.example.AuthService.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(example = "cool_user123")
    private String login;
    @Schema(example = "[\"USER\", \"ADMIN\"]")
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
