package org.ecommercecv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductLitDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @PositiveOrZero(message = "Quantity must be positive or zero")
    private Integer quantity;

    private String image;
}
