package org.ecommercecv.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.ecommercecv.common.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private OrderStatus status;

    private LocalDateTime createdAt;
    private List<OrderItemDTO> orderItems;
}
