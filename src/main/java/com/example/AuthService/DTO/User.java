package com.example.AuthService.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents 'service_user' database table and UserDetails for Spring Security.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "service_user")
public class User implements UserDetails, Serializable {

    @Id
    @Schema(example = "cool_user123")
    private String login;

    @Schema(example = "SuperSecretPassword",
            description = "Returned in hashed form when responding")
    private String password;

    @Schema(example = "pochta@mail.com",
            description = "Not necessary when logging")
    private String email;

    @Column(name = "has_google_reg")
    @Schema(name = "has_google_reg")
    @JsonIgnore
    private boolean isGoogleReg = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonManagedReference("userReference")
    @JsonIgnore
    private List<UserRole> roles = new ArrayList<>();

    @Schema(example = "[\"USER\", \"ADMIN\"]",
            accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty("roles")
    public List<String> getRolesOnly() {
        return roles.stream()
                .map(userRole -> userRole.getKey().getRoleId())
                .collect(Collectors.toList());
    }

    public String desc() {
        return String.format("{ \"login\":\"%s\", \"email\":\"%s\" }", login, email);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(userRole -> userRole.getRole()).toList();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return login;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}
