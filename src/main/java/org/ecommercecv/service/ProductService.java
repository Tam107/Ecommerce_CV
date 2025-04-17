package org.ecommercecv.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.ProductDTO;
import org.ecommercecv.mapper.ProductMapper;
import org.ecommercecv.model.Product;
import org.ecommercecv.repository.ProductRepository;
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



    private String saveImage(MultipartFile image) throws IOException{
        String fileName = UUID.randomUUID().toString()+ "_" + image.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR, fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return fileName;
    }
}
