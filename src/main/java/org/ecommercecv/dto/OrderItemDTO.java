package org.ecommercecv.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDTO {
    private Long id;

    private Long productId;

    @Positive(message = "quantity cannot be negative")
    private Integer quantity;

    @Positive(message = "price cannot be negative")
    private BigDecimal price;
}
