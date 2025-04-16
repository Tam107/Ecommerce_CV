package org.ecommercecv;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.ecommercecv.dto.OrderDTO;
import org.ecommercecv.dto.ProductDTO;
import org.ecommercecv.dto.request.LoginRequest;
import org.ecommercecv.dto.response.AuthResponse;
import org.ecommercecv.mapper.OrderMapper;
import org.ecommercecv.model.Order;
import org.ecommercecv.model.OrderItem;
import org.ecommercecv.model.User;
import org.ecommercecv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.xml.validation.Validator;
import java.util.Arrays;

@SpringBootApplication
public class EcommerceCvApplication {


    public static void main(String[] args) {
        SpringApplication.run(EcommerceCvApplication.class, args);
//        testProductDTO();
    }

//    example for mapping DTO to entity

//    @Autowired
//    private OrderMapper orderMapper;

//    public OrderDTO getOrder(Long id) {
//        Order order = new Order();
//        order.setId(id);
//        User user = new User();
//        order.setUser(user);
//        order.setItems(Arrays.asList(new OrderItem()));
//        return orderMapper.toDto(order);
//    }

    private static void testProductDTO() {

        ProductDTO validProduct = new ProductDTO();
        validProduct.setName("Valid Product");
        validProduct.setId(1l);
        validProduct.setDescription("This is a valid product description.");
        validProduct.setPrice(100L);
        validProduct.setQuantity(10);
        validProduct.setComments(null);
        validProduct.setImage("valid_image.png");

        System.out.println("Lombok toString: " + validProduct.toString());
        System.out.println("get Product " + validProduct.getName());
    }


    @Bean
    public CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {
            System.out.println("=== Testing UserService with CommandLineRunner ===");

            // Test 1: Register a new user
            System.out.println("Test 1: Registering a new user");
            try {
                User newUser = new User();
                newUser.setEmail("testuser@example.com");
                newUser.setPassword("password123");
                User registeredUser = userService.registerUser(newUser);
                System.out.println("Success: Registered user with email: " + registeredUser.getEmail());
            } catch (Exception e) {
                System.err.println("Error registering user: " + e.getMessage());
            }

            // Test 2: Login with the registered user
            System.out.println("\nTest 2: Logging in");
            try {
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setEmail("testuser@example.com");
                loginRequest.setPassword("password123");
                AuthResponse authResponse = userService.login(loginRequest);
                System.out.println("Success: Access Token: " + authResponse.getAccessToken());
                System.out.println("Success: Refresh Token: " + authResponse.getRefreshToken());
            } catch (Exception e) {
                System.err.println("Error logging in: " + e.getMessage());
            }
        };
    }
}
