package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.UserDTO;
import com.example.store.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link StoreController}.
 * Note some improvement here could be to extract build methods into interface, and make
 * the test class implement this interfaces with default methods. This will reduce the number of lines,
 * since some of the builder methods are common for different tests.
 * Example of such an interface:
 * </br>
 *  public interface BirdDataProvider {
 *      default Bird buildBird() {
 *          final Bird result = new Bird(1, "name", "color", 12.3d, 12.4d);
 *          return result;
 *      }
 *  }
 */
@ExtendWith(MockitoExtension.class)
class StoreControllerTest {

    private static final String TITLE = "some title";

    private static final Long ID = 1L;

    @Mock
    private ProductService productService = Mockito.mock(ProductService.class);

    @InjectMocks
    private StoreController target;

    @Test
    void getAllProducts_shouldSucceed() {
        // given
        when(productService.getProducts(TITLE)).thenReturn(new ArrayList<>());

        // when
        final List<ProductDTO> result = target.getAllProducts(TITLE);

        // then
        verify(productService).getProducts(TITLE);
        verifyNoMoreInteractions(productService);

        assertTrue(result.isEmpty());
    }

    @Test
    void getProductById_shouldSucceed() {
        // given
        final ProductDTO productDTO = buildProductDto();

        when(productService.getProductById(ID)).thenReturn(productDTO);

        // when
        final ProductDTO result = target.getProductById(ID);

        // then
        verify(productService).getProductById(ID);
        verifyNoMoreInteractions(productService);

        assertEquals(productDTO, result);
    }

    @Test
    public void createProduct_shouldSucceed() {
        // given
        final ProductDTO product = buildProductDto();

        when(productService.saveProduct(product)).thenReturn(product);

        // when
        ProductDTO result = target.createProduct(product);

        // then
        verify(productService).saveProduct(product);
        verifyNoMoreInteractions(productService);

        assertEquals(product, result);
    }

    @Test
    public void updateProduct_shouldSucceed() {
        // given
        ProductDTO product = buildProductDto();

        when(productService.updateProduct(ID, product)).thenReturn(product);

        // when
        ProductDTO result = target.updateProduct(ID, product);

        // then
        verify(productService).updateProduct(ID, product);
        verifyNoMoreInteractions(productService);

        assertEquals(product, result);
    }

    @Test
    public void deleteProduct_shouldSucceed() {
        // given
        when(productService.deleteProduct(ID)).thenReturn(true);

        // when
        boolean result = target.deleteProduct(ID);

        // then
        verify(productService).deleteProduct(ID);
        verifyNoMoreInteractions(productService);

        assertTrue(result);
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
     * @return UserDTO
     */
    private UserDTO buildUserDto() {
        final UserDTO user = new UserDTO();
        user.setUsername("user");
        user.setPseudonym("pseudonym");
        user.setId(ID);
        return user;
    }
}
