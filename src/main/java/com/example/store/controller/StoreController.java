package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StoreController responsible with product operations. I see this application as a store platform.
 * As result, I set up some rules:
 * All should be able to see all the products
 * Only authenticated users should be able to modify
 * While all products should be available to customers.
 * Only authenticated user can delete their own products.
 * Conversion logic handled in Service.
 * Controllers should be light:
 * - they should not catch exceptions - we have Exception Handler for that
 * - they should not perform conversions from DTO to Entity and the other way around - we have services for that.
 * - because if we add an OPENAPI (Swagger) each controller method can get quite big.
 * ------------------------------------------------------
 * Note1: This can be further improved by having API documentation automatically generated
 * using the OPENAPI framework.
 * Note2: The custom get products by any subset of fields from database can be implemented using GraphQL
 * that gives us the flexibility of writing 1 endpoint while being able to filter the products in any possible
 * combinations. see <a href="https://graphql.org">Graph QL</a>. For this POC we used filter by a subpart of a title as seen below.
 * Note3: This endpoint will return either receive and answer with JSON or XML on demand
 * based on Content-Type & Accept headers
 * application/json for JSON
 * application/xml for XML
 */
@RestController
@RequestMapping("/store")
public class StoreController {
    private final ProductService productService;
    @Autowired
    public StoreController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * GET either all products, or if title is provided, we filter those products with the provided title.
     * @param title String
     * @return List of Products
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<ProductDTO> getAllProducts(@RequestParam(required = false) String title) {
        return productService.getProducts(title);
    }

    /**
     * We return a {@link ProductDTO} by an ID.
     * @param id Long
     * @return {@link ProductDTO} object.
     */
    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * We create a product with the provided information.
     * Example JSON:
     * {
     * 		"title" : "title",
     * 		"description" : "description" ,
     * 		"coverImage" :"coverImage" ,
     *      "price" : 10,
     *      "quantity" : 10,
     * 		"author" :  {
     *             "username" : "valentin"
     *      }
     * }
     * Example XML:
     * <product>
     *     <title>title</title>
     *     <description>description</description>
     *     <coverImage>coverImage</coverImage>
     *     <price>10</price>
     *     <quantity>10</quantity>
     *     <author>
     *         <username>valentin</username>
     *     </author>
     * </product>
     *
     * @param product ProductDTO
     * @return created Product
     */
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ProductDTO createProduct(@RequestBody ProductDTO product) {
        return productService.saveProduct(product);
    }

    /**
     * We update an existent product with id, based on provided product information
     * @param id Long
     * @param product ProductDTO
     * @return updated Product
     */
    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO product) {
        return productService.updateProduct(id, product);
    }

    /**
     * We un-publish a Product defined by id.
     * @param id Long
     */
    @DeleteMapping("/{id}")
    public boolean deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}