package com.example.AuthService.Service;

import com.example.AuthService.DTO.UserInfo;
import com.example.AuthService.Repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Responsible for saving user info instance.
 */
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository repository;

    /**
     * Adds passed userInfo instance to database.
     *
     * @param userInfo
     */
    public void save(UserInfo userInfo) {
        repository.save(userInfo);
    }

}
