package com.example.AuthService.DTO;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Represents role of user.
 * <p>
 * Implements GrantedAuthority for Spring Security using.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    private String id;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference("roleReference")
    private List<UserRole> users;

    @Override
    public String getAuthority() {
        return id;
    }

}
