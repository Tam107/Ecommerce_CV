package org.ecommercecv.controller;

import lombok.RequiredArgsConstructor;
import org.ecommercecv.common.OrderStatus;
import org.ecommercecv.dto.OrderDTO;
import org.ecommercecv.dto.response.ApiResponse;
import org.ecommercecv.model.User;
import org.ecommercecv.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {


    private final OrderService orderService;

    @PostMapping("")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> createOrder(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestParam String address,
                                                   @RequestParam String phoneNumber){
        Long userId = ((User) userDetails).getId();
        OrderDTO orderDTO = orderService.createOrder(userId, address, phoneNumber);
//        if (orderDTO == null) {
//            return ResponseEntity.ok(new ApiResponse(400, "Failed to create order", null));
//        }
        return ResponseEntity.ok(new ApiResponse(200, "Order created successfully", orderDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> getAllOrders(@AuthenticationPrincipal UserDetails userDetails){
        List<OrderDTO > orderDTOs = orderService.getAllOrders();
        if (orderDTOs == null || orderDTOs.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(404, "No orders found", null));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Orders retrieved successfully", orderDTOs));
    }

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> getUserOrders(@AuthenticationPrincipal UserDetails userDetails){
        Long userId = ((User) userDetails).getId();
        List<OrderDTO> orderDTOs = orderService.getUserOrders(userId);
        if (orderDTOs == null || orderDTOs.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(404, "No orders found", null));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Orders retrieved successfully", orderDTOs));
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long orderId,
                                                   @RequestParam OrderStatus status){
        OrderDTO orderDTO = orderService.updateOrderStatus(orderId, status);
        if (orderDTO == null) {
            return ResponseEntity.ok(new ApiResponse(400, "Failed to update order status", null));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Order status updated successfully", orderDTO));
    }

}
