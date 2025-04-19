package org.ecommercecv.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.CartDTO;
import org.ecommercecv.dto.response.ApiResponse;
import org.ecommercecv.model.User;
import org.ecommercecv.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "CART_CONTROLLER")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam Long productId,
                                                 @RequestParam Integer quantity){

        Long userId = ((User) userDetails).getId();
        CartDTO cart = cartService.addToCart(userId, productId, quantity);
        if (cart == null) {
            return ResponseEntity.ok(new ApiResponse(400, "Failed to add product to cart", null));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Product added to cart successfully", cart));
    }


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getCart(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = ((User) userDetails).getId();
        CartDTO cart = cartService.getCart(userId);
        if (cart == null) {
            return ResponseEntity.ok(new ApiResponse(404, "Cart not found", null));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Cart retrieved successfully", cart));
    }

    @DeleteMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> clearCart(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = ((User) userDetails).getId();
        if (userId == null) {
            return ResponseEntity.ok(new ApiResponse(400, "User not found", null));
        }
        cartService.clearCart(userId);
        return ResponseEntity.ok(new ApiResponse(200, "Cart cleared successfully", null));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> removeCartItem(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable Long productId){
        Long userId = ((User) userDetails).getId();
        cartService.removeCartItem(userId, productId);
        return ResponseEntity.ok(new ApiResponse(200, "Product removed from cart successfully", null));
    }
}
