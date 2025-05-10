package com.example.store.service;

import com.example.store.dto.ProductDTO;
import com.example.store.dto.UserDTO;
import com.example.store.exception.StoreException;
import com.example.store.model.Product;
import com.example.store.model.User;
import com.example.store.repository.ProductRepository;
import com.example.store.repository.UserRepository;
import com.example.store.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Products Service class.
 */
@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final MyUserDetailsService myUserDetailsService;

    private final MapperService mapperService;

    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository,
                       MyUserDetailsService myUserDetailsService, MapperService mapperService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.myUserDetailsService = myUserDetailsService;
        this.mapperService = mapperService;
    }

    /**
     * Gets products.
     * - in order to respect S from SOLID, we prefer to have a single point of exit.
     * - this is also helping in debugging as we need of one breakpoint instead of many.
     * @param title String
     * @return List
     */
    public List<ProductDTO> getProducts(String title) {
        List<Product> products;
        if (Objects.isNull(title)) {
            products = getAllProducts();
        } else {
            products = searchProductsByTitle(title);
        }
        return mapperService.getProductsDtoList(products);
    }

    /**
     * Gets a product by provided id.
     * @param id Long
     * @return Product
     */
    public ProductDTO getProductById(Long id) {
        final Product product = getPersistedProductById(id);
        return mapperService.getProductDto(product);
    }

    /**
     * Gets a product by provided id.
     * Lambda and stream collections have poor performance as for each dot in the chain
     * list.stream().filter().map() the memory occupies more and more resources.
     * And when the list is big, this could cause serious problems.
     * Classical FOR is preferred over stream.
     * Another weak point of stream is debugging, they are much harder to debug.
     * And although it brings clarity for those that enjoys functional programming, that clarity cost week performance,
     * several times increase in memory usage and hard to debug.
     * @param id the id of the product
     * @return Product
     */
    private Product getPersistedProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new StoreException(ErrorMessages.PRODUCT_NOT_FOUND));
    }

    /**
     * Creates a Product record.
     * @param productDto the product
     * @return the persisted product
     */
    public ProductDTO saveProduct(ProductDTO productDto) {
        final Product product = mapperService.getProduct(productDto);
        product.setAuthor(getPersistedAuthor(productDto.getAuthor()));
        final Product persistedProduct = productRepository.save(product);
        return mapperService.getProductDto(persistedProduct);
    }

    /**
     * Updates a Product record.
     * A builder pattern can be useful (via lombok for example)
     * in this scenario where we construct object, but this is a POC.
     * @param id Long
     * @param productDto ProductDTO
     * @return ProductDTO
     */
    public ProductDTO updateProduct(Long id, ProductDTO productDto) {
        final Product existingProduct = getPersistedProductById(id);
        existingProduct.setTitle(productDto.getTitle());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setAuthor(getPersistedAuthor(productDto.getAuthor()));
        existingProduct.setCoverImage(productDto.getCoverImage());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setQuantity(productDto.getQuantity());
        final Product updatedProduct = productRepository.save(existingProduct);
        return mapperService.getProductDto(updatedProduct);
    }

    /**
     * Deletes a product record.
     * @param id Long
     */
    public boolean deleteProduct(Long id) {
        final Product existingProduct = getPersistedProductById(id);
        final String username = myUserDetailsService.getCurrentUsername();
        if (username.equals(existingProduct.getAuthor().getUsername())) {
            productRepository.deleteById(id);
            // as we reached this point we can consider delete was successfully done.
            return true;
        } else {
            // you are attempting to remove products from other stores (which belong to different users).
            throw new StoreException(ErrorMessages.REMOVE_NOT_ALLOWED);
        }
    }

    /**
     * Method that return the persisted author using its username.
     * @param userDto UserDTO
     * @return User object
     */
    private User getPersistedAuthor(UserDTO userDto) {
        if (Objects.isNull(userDto)) {
            throw new StoreException(ErrorMessages.INVALID_USER);
        }

        Optional<User> author = Optional.empty();
        if (!Objects.isNull(userDto.getUsername())) {
            author = userRepository.findByUsername(userDto.getUsername());
        } else if (!Objects.isNull(userDto.getPseudonym())) {
            author = userRepository.findByPseudonym(userDto.getPseudonym());
        } else if (!Objects.isNull(userDto.getId())) {
            author = userRepository.findById(userDto.getId());
        }

        if (author.isPresent()) {
            return author.get();
        } else {
            throw new StoreException(ErrorMessages.PRODUCT_HAS_INVALID_USER);
        }
    }

    /**
     * Lists all products.
     * @return List
     */
    private List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Filter a list of products by a part of the title.
     * @param title String - the part of the title
     * @return List of products matching the search criteria
     */
    private List<Product> searchProductsByTitle(String title) {
        return productRepository.findByTitleContaining(title);
    }
}
