package com.example.AuthService.DTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Represents 'user_role' database table.
 * <p>
 * Couples User ans Role classes that have ManyToMany relationship.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role")
public class UserRole {

    @EmbeddedId
    @JsonUnwrapped
    private Key key;

    @ManyToOne
    @JoinColumn(name = "user_login")
    @MapsId("userLogin")
    @JsonBackReference("userReference")
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @MapsId("roleId")
    @JsonBackReference("roleReference")
    private Role role;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Embeddable
    public static class Key implements Serializable {

        @Column(name = "user_login")
        private String userLogin;

        @Column(name = "role_id")
        private String roleId;

    }

}
