package org.ecommercecv.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.common.Role;
import org.ecommercecv.dto.ChangePasswordRequest;
import org.ecommercecv.exception.ResourceNotFoundException;
import org.ecommercecv.model.User;
import org.ecommercecv.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER_SERVICE")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        log.info("Registering new user: {}", user.getEmail());
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new IllegalStateException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
//        user.setConfirmationCode(generateConfirmationCode());
//        user.setEmailConfirmation(false);
        
        return userRepository.save(user);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private String generateConfirmationCode() {
        Random random = new Random();
        int code  = 100000 + random.nextInt(900000);
        return String.valueOf(code);

    }

    public void changePassword(String email, ChangePasswordRequest request){
        User user = getUserByEmail(email);
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new BadCredentialsException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
