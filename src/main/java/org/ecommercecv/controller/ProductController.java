package org.ecommercecv.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.ProductDTO;
import org.ecommercecv.dto.response.ApiResponse;
import org.ecommercecv.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT_CONTROLLER")
public class ProductController {

    private final ProductService productService;


    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createProduct(@RequestPart("product") @Valid ProductDTO productDTO,
                                                     @RequestPart(value = "image", required = false) MultipartFile image)
            throws IOException, IOException {
        ProductDTO newProduct = productService.createProduct(productDTO, image);
        return ResponseEntity.ok(new ApiResponse(201, "Product created successfully", newProduct));
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Long id,
                                                     @RequestPart("product") @Valid ProductDTO productDTO,
                                                     @RequestPart(value = "image", required = false) MultipartFile image)
            throws IOException {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO, image);

        return ResponseEntity.ok(new ApiResponse(200, "Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse(200, "Product deleted successfully", null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(new ApiResponse(200,
                "Product retrieved successfully",
                product));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllProducts(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse(200,
                "Products retrieved successfully",
                productService.getAllProducts(pageable)));
    }
}
