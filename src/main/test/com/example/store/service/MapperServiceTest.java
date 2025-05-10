package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.UserDTO;
import com.example.store.model.Product;
import com.example.store.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link MapperService}.
 * A much more elegant solution for a simplified code could be Spock framework that uses GROOVY.
 * But we will use Mockito, and JUnit Jupiter.
 */
@ExtendWith(MockitoExtension.class)
public class MapperServiceTest {

    private final static Long ID = 1L;

    private final MapperService target = new MapperService();

    @Test
    public void getProductsDtoList_shouldSucceed() {
        // given
        final Product product = buildProduct();
        final List<Product> products = List.of(product);

        // when
        final List<ProductDTO> result = target.getProductsDtoList(products);

        // then
        assertEquals(products.size(), result.size());
        assertEquals(products.get(0).getAuthor().getUsername(), result.get(0).getAuthor().getUsername());
        assertEquals(products.get(0).getAuthor().getId(), result.get(0).getAuthor().getId());
        assertEquals(products.get(0).getAuthor().getPseudonym(), result.get(0).getAuthor().getPseudonym());
        assertEquals(products.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(products.get(0).getId(), result.get(0).getId());
        assertEquals(products.get(0).getPrice(), result.get(0).getPrice());
        assertEquals(products.get(0).getQuantity(), result.get(0).getQuantity());
        assertEquals(products.get(0).getDescription(), result.get(0).getDescription());
        assertEquals(products.get(0).getCoverImage(), result.get(0).getCoverImage());
    }

    @Test
    public void getProductDto_shouldSucceed() {
        // given
        final Product product = buildProduct();

        // when
        final ProductDTO result = target.getProductDto(product);

        // then
        assertEquals(product.getAuthor().getUsername(), result.getAuthor().getUsername());
        assertEquals(product.getAuthor().getId(), result.getAuthor().getId());
        assertEquals(product.getAuthor().getPseudonym(), result.getAuthor().getPseudonym());
        assertEquals(product.getTitle(), result.getTitle());
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getQuantity(), result.getQuantity());
        assertEquals(product.getDescription(), result.getDescription());
        assertEquals(product.getCoverImage(), result.getCoverImage());
    }

    @Test
    public void getUserDto_shouldSucceed() {
        // given
        User user = buildUser();

        // when
        UserDTO result = target.getUserDto(user);

        // then
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getPseudonym(), result.getPseudonym());
    }

    @Test
    public void getProduct_shouldSucceed() {
        // given
        ProductDTO productDto = buildProductDto();

        // when
        Product result = target.getProduct(productDto);

        // then
        assertEquals(productDto.getAuthor().getUsername(), result.getAuthor().getUsername());
        assertEquals(productDto.getAuthor().getId(), result.getAuthor().getId());
        assertEquals(productDto.getAuthor().getPseudonym(), result.getAuthor().getPseudonym());
        assertEquals(productDto.getTitle(), result.getTitle());
        assertEquals(productDto.getId(), result.getId());
        assertEquals(productDto.getPrice(), result.getPrice());
        assertEquals(productDto.getQuantity(), result.getQuantity());
        assertEquals(productDto.getDescription(), result.getDescription());
        assertEquals(productDto.getCoverImage(), result.getCoverImage());
    }

    @Test
    public void getUser_should_succeed() {
        // given
        UserDTO userDto = buildUserDto();

        // when
        User result = target.getUser(userDto);

        // then
        assertEquals(userDto.getUsername(), result.getUsername());
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getPseudonym(), result.getPseudonym());
    }


    /**
     * builds an object used for testing.
     * @return ProductDTO
     */
    private ProductDTO buildProductDto() {
        final ProductDTO product = new ProductDTO();
        product.setId(2L);
        product.setAuthor(buildUserDto());
        product.setDescription("description");
        product.setPrice(10d);
        product.setQuantity(10L);
        product.setCoverImage("coverImage");
        product.setTitle("title");
        return product;
    }

    /**
     * builds an object used for testing.
     * @return User
     */
    private UserDTO buildUserDto() {
        final UserDTO user = new UserDTO();
        user.setUsername("user");
        user.setPseudonym("pseudonym");
        user.setId(ID);
        return user;
    }

    /**
     * builds an object used for testing.
     * @return Product
     */
    private Product buildProduct() {
        final Product product = new Product();
        product.setAuthor(buildUser());
        product.setDescription("description");
        product.setPrice(10d);
        product.setQuantity(10L);
        product.setCoverImage("coverImage");
        product.setTitle("title");
        return product;
    }

    /**
     * builds an object used for testing.
     * @return User
     */
    private User buildUser() {
        final User user = new User();
        user.setUsername("user");
        user.setPseudonym("pseudonym");
        user.setId(ID);
        return user;
    }
}
