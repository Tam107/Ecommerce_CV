package org.ecommercecv.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.ProductDTO;
import org.ecommercecv.dto.ProductListDTO;
import org.ecommercecv.dto.response.ApiResponse;
import org.ecommercecv.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT_CONTROLLER")
public class ProductController {

    private final ProductService productService;


    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(@RequestPart("product") @Valid ProductDTO productDTO,
                                                     @RequestPart(value = "image", required = false) MultipartFile image)
            throws IOException, IOException {
        log.info("Creating new product: {}", productDTO.getName());
        ProductDTO newProduct = productService.createProduct(productDTO, image);
        return ResponseEntity.ok(new ApiResponse(201, "Product created successfully", newProduct));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id,
                                                     @RequestPart("product") @Valid ProductDTO productDTO,
                                                     @RequestPart(value = "image", required = false) MultipartFile image)
            throws IOException {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO, image);
        if (updatedProduct == null) {
            return ResponseEntity.ok(new ApiResponse(404, "Product not found", null));
        }

        return ResponseEntity.ok(new ApiResponse(200, "Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        if (productService.getProductById(id) == null) {
            return ResponseEntity.ok(new ApiResponse(404, "Product not found", null));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Product deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.ok(new ApiResponse(404, "Product not found", null));
        }
        return ResponseEntity.ok(new ApiResponse(200,
                "Product retrieved successfully",
                product));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse> getAllProducts(@PageableDefault(size = 10) Pageable pageable) {
        Page<ProductListDTO> products = productService.getAllProducts(pageable);
        if (products.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(404, "No products found", null));
        }

        return ResponseEntity.ok(new ApiResponse(200,
                "Products retrieved successfully",
                productService.getAllProducts(pageable)));
    }
}
