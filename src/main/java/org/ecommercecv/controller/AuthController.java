package org.ecommercecv.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.request.ChangePasswordRequest;
import org.ecommercecv.dto.request.AuthRequest;
import org.ecommercecv.dto.response.ApiResponse;
import org.ecommercecv.dto.response.AuthResponse;
import org.ecommercecv.model.User;
import org.ecommercecv.service.JwtService;
import org.ecommercecv.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j(topic = "AUTH_CONTROLLER")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        AuthResponse jwtToken = userService.login(authRequest);
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRequest user){
        User newUser = userService.registerUser(user);
        ApiResponse apiResponse = new ApiResponse(201, "User registered successfully", null);
        return ResponseEntity.status(201).body(apiResponse);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (authentication == null || email == null) {
            return ResponseEntity.status(401).body(new ApiResponse(401, "Unauthorized", null));
        }
        userService.changePassword(email, request);
        return ResponseEntity.ok(new ApiResponse(200, "Password changed successfully", null));
    }

}
