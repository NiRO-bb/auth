package com.example.AuthService.Repository;

import com.example.AuthService.DTO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByLoginOrEmail(String login, String email);

}
