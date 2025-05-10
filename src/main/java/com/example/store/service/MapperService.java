package com.example.store.service;

import com.example.store.dto.UserDTO;
import com.example.store.model.Product;
import com.example.store.dto.ProductDTO;
import com.example.store.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This class could be refactored to use ObjectMapper. But in scope of this POC, I choose keeping this project
 * light and avoid adding new dependencies, so we do all the transformations manually here.
 */
@Service
public class MapperService {

    /**
     * Method that converts a list of Product entities into a list of ProductDTO
     * (to be used API output).
     * @param productList List
     * @return List
     */
    public List<ProductDTO> getProductsDtoList(List<Product> productList) {
        final List<ProductDTO> result = new ArrayList<>();
        for (Product product:productList) {
            ProductDTO convertedProduct = getProductDto(product);
            result.add(convertedProduct);
        }
        return result;
    }

    /**
     * Method that converts a Product entity into a ProductDTO
     * (to be used as API output).
     * @param product Product
     * @return ProductDTO
     */
    public ProductDTO getProductDto(Product product) {
        final ProductDTO result = new ProductDTO();
        final UserDTO authorDto = getUserDto(product.getAuthor());
        result.setAuthor(authorDto);
        result.setId(product.getId());
        result.setDescription(product.getDescription());
        result.setTitle(product.getTitle());
        result.setPrice(product.getPrice());
        result.setCoverImage(product.getCoverImage());
        result.setQuantity(product.getQuantity());
        return result;
    }

    /**
     * Method that converts User entity into UserDTO
     * (to be used as API output).
     * Observation: This method purposely avoids setting the password, as this field should NOT be updated
     * or changed while performing CRUD operations on Products.
     * @param user the User entity
     * @return the User DTO
     */
    public UserDTO getUserDto(User user) {
        final UserDTO result = new UserDTO();
        result.setId(user.getId());
        result.setPseudonym(user.getPseudonym());
        result.setUsername(user.getUsername());
        return result;
    }

    /**
     * Method that converts an DTO (Received from API) into an entity object to be persisted.
     * @param productDto the Product DTO
     * @return the Product entity
     */
    public Product getProduct(ProductDTO productDto) {
        final Product result = new Product();
        final User user = getUser(productDto.getAuthor());
        result.setAuthor(user);
        result.setId(productDto.getId());
        result.setDescription(productDto.getDescription());
        result.setTitle(productDto.getTitle());
        result.setPrice(productDto.getPrice());
        result.setCoverImage(productDto.getCoverImage());
        result.setQuantity(productDto.getQuantity());
        return result;
    }

    /**
     * Method that converts an DTO (Received from API) into an entity object to be persisted.
     * @param userDto the User DTO
     * @return the User entity
     */
    public User getUser(UserDTO userDto) {
        final User result = new User();
        result.setId(userDto.getId());
        result.setPseudonym(userDto.getPseudonym());
        result.setUsername(userDto.getUsername());
        return result;
    }
}
