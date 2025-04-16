package org.ecommercecv;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.ecommercecv.dto.OrderDTO;
import org.ecommercecv.dto.ProductDTO;
import org.ecommercecv.mapper.OrderMapper;
import org.ecommercecv.model.Order;
import org.ecommercecv.model.OrderItem;
import org.ecommercecv.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.validation.Validator;
import java.util.Arrays;

@SpringBootApplication
public class EcommerceCvApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceCvApplication.class, args);
//        testProductDTO();
    }

//    example for mapping DTO to entity

//    @Autowired
//    private OrderMapper orderMapper;

//    public OrderDTO getOrder(Long id) {
//        Order order = new Order();
//        order.setId(id);
//        User user = new User();
//        order.setUser(user);
//        order.setItems(Arrays.asList(new OrderItem()));
//        return orderMapper.toDto(order);
//    }

    private static void testProductDTO(){

        ProductDTO validProduct = new ProductDTO();
        validProduct.setName("Valid Product");
        validProduct.setId(1l);
        validProduct.setDescription("This is a valid product description.");
        validProduct.setPrice(100L);
        validProduct.setQuantity(10);
        validProduct.setComments(null);
        validProduct.setImage("valid_image.png");

        System.out.println("Lombok toString: "+validProduct.toString());
        System.out.println("get Product "+ validProduct.getName());
    }
}
