package org.ecommercecv.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.ProductDTO;
import org.ecommercecv.dto.ProductListDTO;
import org.ecommercecv.exception.ResourceNotFoundException;
import org.ecommercecv.mapper.ProductMapper;
import org.ecommercecv.model.Product;
import org.ecommercecv.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PRODUCT_SERVICE")
public class ProductService {

    private  final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private static final String UPLOAD_DIR = "src/main/resources/static/images";


    /** Long id;

      String name;

     String description;

      Long price;

      Integer quantity;

      String image;

     private List<CommentDTO> comments;*/
    @Transactional(rollbackFor = Exception.class)
    public ProductDTO createProduct(ProductDTO productDTO, MultipartFile image) throws IOException {
        Product product = productMapper.toEntity(productDTO);
        if (image != null && !image.isEmpty()){
            String fileName = saveImage(image);
            product.setImage("/images/" + fileName);
        }
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile image) throws IOException {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
        if (image !=null && !image.isEmpty()){
            String fileName = saveImage(image);
            existingProduct.setImage("/images/" + fileName);
        }
        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id){
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public ProductDTO getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productMapper.toDto(product);
    }

//    prevent conflict returning comment, exclude comment from product
    public Page<ProductListDTO> getAllProducts(Pageable pageable){
        return productRepository.findAllWithoutComments(pageable);
    }
    
    private String saveImage(MultipartFile image) throws IOException{
        String fileName = UUID.randomUUID().toString()+ "_" + image.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR, fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return fileName;
    }
}
