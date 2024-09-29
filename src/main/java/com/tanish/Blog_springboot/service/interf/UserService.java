package com.tanish.Blog_springboot.service.interf;

import com.tanish.Blog_springboot.dto.ApiResponse;
import com.tanish.Blog_springboot.dto.LoginRequest;
import com.tanish.Blog_springboot.dto.RegistrationRequest;
import com.tanish.Blog_springboot.dto.UserDto;
import com.tanish.Blog_springboot.entity.User;

public interface UserService {
    ApiResponse registerUser(RegistrationRequest registrationRequest);
    ApiResponse loginUser(LoginRequest loginRequest);
    ApiResponse getAllUsers();

    ApiResponse<UserDto> getUserProfile();

    ApiResponse<UserDto> getLoginUser();
}
