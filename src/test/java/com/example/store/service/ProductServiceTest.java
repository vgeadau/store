package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.UserDTO;
import com.example.store.exception.StoreException;
import com.example.store.model.Product;
import com.example.store.model.User;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.UserRepository;
import com.example.store.util.ErrorMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProductService}.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private final static String TITLE = "some title";
    private final static Long ID = 1L;
    private final static Long INVALID_ID = 0L;
    private final static String DIFFERENT_USERNAME = "different username";

    @Mock
    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);

    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Mock
    private MyUserDetailsService myUserDetailsService = Mockito.mock(MyUserDetailsService.class);

    @Mock
    private MapperService mapperService = Mockito.mock(MapperService.class);

    @InjectMocks
    private ProductService target;

    @Test
    public void getProducts_withoutTitle_shouldSucceed() {
        // given
        final List<Product> products = new ArrayList<>();
        final List<ProductDTO> productDTOs = new ArrayList<>();

        when(productRepository.findAll()).thenReturn(products);
        when(mapperService.getProductsDtoList(products)).thenReturn(productDTOs);

        // when
        final List<ProductDTO> result = target.getProducts(null);

        // then
        verify(productRepository).findAll();
        verify(mapperService).getProductsDtoList(products);
        verifyNoMoreInteractions(productRepository, mapperService);
        verifyNoInteractions(userRepository, myUserDetailsService);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getProducts_withTitle_shouldSucceed() {
        // given
        final List<Product> products = new ArrayList<>();
        final List<ProductDTO> productDTOs = new ArrayList<>();

        when(productRepository.findByTitleContaining(TITLE)).thenReturn(products);
        when(mapperService.getProductsDtoList(products)).thenReturn(productDTOs);

        // when
        final List<ProductDTO> result = target.getProducts(TITLE);

        // then
        verify(productRepository).findByTitleContaining(TITLE);
        verify(mapperService).getProductsDtoList(products);
        verifyNoMoreInteractions(productRepository, mapperService);
        verifyNoInteractions(userRepository, myUserDetailsService);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getProductById_shouldSucceed() {
        // given
        final Product product = buildProduct();
        final ProductDTO productDto = buildProductDto();

        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        when(mapperService.getProductDto(product)).thenReturn(productDto);

        // when
        final ProductDTO result = target.getProductById(ID);

        // then
        verify(productRepository).findById(ID);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(userRepository, myUserDetailsService);

        assertEquals(productDto, result);
    }

    @Test
    public void getProductById_withInvalidId_shouldFail() {
        // given
        when(productRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        // when
        final StoreException exception = assertThrows(StoreException.class, () -> target.getProductById(INVALID_ID));

        // then
        verify(productRepository).findById(INVALID_ID);
        verifyNoMoreInteractions(productRepository);
        verifyNoInteractions(userRepository, myUserDetailsService);

        assertEquals(ErrorMessages.PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void saveProduct_withNullUser_shouldFail() {
        // given
        final ProductDTO product = buildProductDto();
        product.setAuthor(null);

        // when
        final StoreException exception = assertThrows(StoreException.class, () -> target.saveProduct(product));

        // then
        verifyNoInteractions(productRepository, userRepository, myUserDetailsService);

        assertEquals(ErrorMessages.INVALID_USER, exception.getMessage());
    }

    @Test
    public void saveProduct_withUserWithoutIdPseudonymAndUsername_shouldFail() {
        // given
        final ProductDTO product = buildProductDto();
        product.setAuthor(new UserDTO());

        // when
        final StoreException exception = assertThrows(StoreException.class,
                () -> target.saveProduct(product));

        // then
        verifyNoInteractions(productRepository, userRepository, myUserDetailsService);

        assertEquals(ErrorMessages.PRODUCT_HAS_INVALID_USER, exception.getMessage());
    }

    @Test
    public void saveProduct_withUserId_shouldSucceed() {
        // given
        final ProductDTO product = buildProductDto();
        product.setAuthor(new UserDTO());
        product.getAuthor().setId(ID);
        final Product productEntity = buildProduct();

        final User persistedUser = buildUser();

        when(userRepository.findById(ID)).thenReturn(Optional.of(persistedUser));
        when(mapperService.getProduct(product)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(mapperService.getProductDto(productEntity)).thenReturn(product);

        // when
        final ProductDTO result = target.saveProduct(product);

        // then
        verify(userRepository).findById(ID);
        verify(productRepository).save(productEntity);
        verify(mapperService).getProduct(product);
        verify(mapperService).getProductDto(productEntity);
        verifyNoMoreInteractions(userRepository, productRepository, mapperService);
        verifyNoInteractions(myUserDetailsService);

        assertEquals(product, result);
    }

    @Test
    public void saveProduct_withUsername_should_succeed() {
        // given
        final ProductDTO product = buildProductDto();
        product.setAuthor(new UserDTO());
        final String username = "username";
        product.getAuthor().setUsername(username);
        final Product productEntity = buildProduct();

        final User persistedUser = buildUser();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(persistedUser));
        when(mapperService.getProduct(product)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(mapperService.getProductDto(productEntity)).thenReturn(product);

        // when
        final ProductDTO result = target.saveProduct(product);

        // then
        verify(userRepository).findByUsername(username);
        verify(productRepository).save(productEntity);
        verify(mapperService).getProduct(product);
        verify(mapperService).getProductDto(productEntity);
        verifyNoMoreInteractions(userRepository, productRepository, mapperService);
        verifyNoInteractions(myUserDetailsService);

        assertEquals(product, result);
    }

    @Test
    public void saveProduct_withPseudonym_should_succeed() {
        // given
        final ProductDTO product = buildProductDto();
        product.setAuthor(new UserDTO());
        final String pseudonym = "pseudonym";
        product.getAuthor().setPseudonym(pseudonym);
        final Product productEntity = buildProduct();

        final User persistedUser = buildUser();

        when(userRepository.findByPseudonym(pseudonym)).thenReturn(Optional.of(persistedUser));
        when(mapperService.getProduct(product)).thenReturn(productEntity);
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        when(mapperService.getProductDto(productEntity)).thenReturn(product);


        // when
        final ProductDTO result = target.saveProduct(product);

        // then
        verify(userRepository).findByPseudonym(pseudonym);
        verify(productRepository).save(productEntity);
        verify(mapperService).getProduct(product);
        verify(mapperService).getProductDto(productEntity);
        verifyNoMoreInteractions(userRepository, productRepository, mapperService);
        verifyNoInteractions(myUserDetailsService);

        assertEquals(product, result);
    }

    /**
     * getProductById and getPersistedAuthor are already fully covered by previous unit tests.
     * As a result, this is the only relevant test for updateProduct method.
     */
    @Test
    public void updateProduct_should_succeed() {
        // given
        final ProductDTO product = buildProductDto();
        final Product savedProduct = buildProduct();

        when(productRepository.findById(ID)).thenReturn(Optional.of(savedProduct));
        when(userRepository.findByUsername(product.getAuthor().getUsername())).thenReturn(Optional.of(savedProduct.getAuthor()));
        when(productRepository.save(savedProduct)).thenReturn(savedProduct);
        when(mapperService.getProductDto(savedProduct)).thenReturn(product);

        // when
        final ProductDTO result = target.updateProduct(ID, product);

        // then
        verify(productRepository).findById(ID);
        verify(userRepository).findByUsername(product.getAuthor().getUsername());
        verify(productRepository).save(savedProduct);
        verify(mapperService).getProductDto(savedProduct);
        verifyNoMoreInteractions(productRepository, userRepository, mapperService);
        verifyNoInteractions(myUserDetailsService);

        assertEquals(product, result);
    }

    @Test
    public void deleteProduct_should_succeed() {
        // given
        final Product product = buildProduct();
        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        when(myUserDetailsService.getCurrentUsername()).thenReturn(product.getAuthor().getUsername());

        // when
        boolean result = target.deleteProduct(ID);

        // then
        verify(productRepository).findById(ID);
        verify(myUserDetailsService).getCurrentUsername();
        verify(productRepository).deleteById(ID);

        assertTrue(result);
    }

    @Test
    public void deleteProduct_withForDifferentAuthor_should_fail() {
        // given
        final Product product = buildProduct();
        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        when(myUserDetailsService.getCurrentUsername()).thenReturn(DIFFERENT_USERNAME);

        // when
        final StoreException exception = assertThrows(StoreException.class,
                () -> target.deleteProduct(ID));

        // then
        assertEquals(ErrorMessages.REMOVE_NOT_ALLOWED, exception.getMessage());
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

    /**
     * builds an object used for testing.
     * @return ProductDTO
     */
    private ProductDTO buildProductDto() {
        final ProductDTO result = new ProductDTO();
        result.setId(2L);
        result.setAuthor(buildUserDto());
        result.setDescription("description");
        result.setPrice(10d);
        result.setQuantity(10L);
        result.setCoverImage("coverImage");
        result.setTitle("title");
        return result;
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
