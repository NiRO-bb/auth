package com.example.AuthService.Repository;

import com.example.AuthService.DTO.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.Key> {

    void deleteAllByUserLogin(String userLogin);

}
