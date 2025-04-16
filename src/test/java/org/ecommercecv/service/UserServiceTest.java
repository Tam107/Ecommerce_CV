package org.ecommercecv.service;

import org.ecommercecv.dto.request.LoginRequest;
import org.ecommercecv.dto.response.AuthResponse;
import org.ecommercecv.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void testRegisterAndLogin() {
        // Register
        User user = new User();
        user.setEmail("testjunit@example.com");
        user.setPassword("password123");
        User registered = userService.registerUser(user);
        assertEquals("testjunit@example.com", registered.getEmail());

        // Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testjunit@example.com");
        loginRequest.setPassword("password123");
        AuthResponse response = userService.login(loginRequest);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }
}