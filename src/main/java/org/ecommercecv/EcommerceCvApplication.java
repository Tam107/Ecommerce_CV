package org.ecommercecv;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.ecommercecv.dto.ProductDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.xml.validation.Validator;

@SpringBootApplication
public class EcommerceCvApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceCvApplication.class, args);
//        testProductDTO();
    }

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
