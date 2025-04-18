package org.ecommercecv;

import org.ecommercecv.dto.ProductDTO;
import org.ecommercecv.dto.request.AuthRequest;
import org.ecommercecv.dto.response.AuthResponse;
import org.ecommercecv.model.User;
import org.ecommercecv.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

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
        validProduct.setPrice(new BigDecimal(15));
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
                AuthRequest authRequest = new AuthRequest(newUser.getEmail(), newUser.getPassword());
                User registeredUser = userService.registerUser(authRequest);
                System.out.println("Success: Registered user with email: " + registeredUser.getEmail());
            } catch (Exception e) {
                System.err.println("Error registering user: " + e.getMessage());
            }

            // Test 2: Login with the registered user
            System.out.println("\nTest 2: Logging in");
            try {
                AuthRequest authRequest = new AuthRequest();
                authRequest.setEmail("testuser@example.com");
                authRequest.setPassword("password123");
                AuthResponse authResponse = userService.login(authRequest);
                System.out.println("Success: Access Token: " + authResponse.getAccessToken());
                System.out.println("Success: Refresh Token: " + authResponse.getRefreshToken());
            } catch (Exception e) {
                System.err.println("Error logging in: " + e.getMessage());
            }
        };
    }
}
