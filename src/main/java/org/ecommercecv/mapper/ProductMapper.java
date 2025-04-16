package org.ecommercecv.mapper;

import org.ecommercecv.dto.CommentDTO;
import org.ecommercecv.dto.ProductDTO;
import org.ecommercecv.model.Comment;
import org.ecommercecv.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "image", source = "image")
    ProductDTO toDto(Product product);

    @Mapping(target = "image", source = "image")
    Product toEntity(ProductDTO productDTO);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "productId", source = "product.id")
    CommentDTO toDto(Comment comment);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product.id", source = "productId")
    Comment toEntity(CommentDTO commentDTO);
}
