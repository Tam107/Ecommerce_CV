package org.ecommercecv.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.request.ChangePasswordRequest;
import org.ecommercecv.dto.request.AuthRequest;
import org.ecommercecv.dto.request.EmailConfirmRequest;
import org.ecommercecv.dto.response.ApiResponse;
import org.ecommercecv.dto.response.AuthResponse;
import org.ecommercecv.exception.ResourceNotFoundException;
import org.ecommercecv.model.User;
import org.ecommercecv.service.JwtService;
import org.ecommercecv.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j(topic = "AUTH_CONTROLLER")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthRequest authRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        AuthResponse jwtToken = userService.login(authRequest);
        if (jwtToken == null) {
            return ResponseEntity.ok(new ApiResponse(401, "Invalid email or password", null));
        }
        return ResponseEntity.ok(new ApiResponse(201, "Login successfully", jwtToken));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRequest user){
        User newUser = userService.registerUser(user);
        ApiResponse apiResponse = new ApiResponse(201, "User registered successfully", null);
        if (newUser == null) {
            return ResponseEntity.ok(new ApiResponse(400, "Email already in use", null));
        }
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        if (authentication == null || email == null) {
            return ResponseEntity.ok(new ApiResponse(401, "Unauthorized", null));
        }
        userService.changePassword(email, request);
        return ResponseEntity.ok(new ApiResponse(200, "Password changed successfully", null));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<ApiResponse> confirmEmail(@RequestBody EmailConfirmRequest request){
        try {
            userService.confirmEmail(request.getEmail(), request.getConfirmationCode());
            return ResponseEntity.ok(new ApiResponse(200, "Email confirmed successfully", null));
        }catch (BadCredentialsException e){
            return ResponseEntity.ok(new ApiResponse(400, "Invalid confirmation code", null));
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.ok(new ApiResponse(404, "User not found", null));
        }
    }

    @GetMapping("/user-role")
    public ResponseEntity<ApiResponse> getUserRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userService.getUserByEmail(email);
        if (user !=null){
            String role = String.valueOf(user.getRole());
            return ResponseEntity.ok(new ApiResponse(200, "User role retrieved successfully", role));
        }
        return ResponseEntity.ok(new ApiResponse(404, "User not found", null));
    }

}
