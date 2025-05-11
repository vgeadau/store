package com.example.store.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Product entity which is being persisted.
 * </br>
 * Note1: BigDecimal should be used for Finance related calculus in java, however this
 * is a POC (Proof of Concept) and as a result we simplified a bit the implementation using Double.
 */
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    private String title;

    private String description;

    @ManyToOne
    private User author;

    private String coverImage;

    private Double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return Objects.equals(getId(), product.getId())
                && Objects.equals(getTitle(), product.getTitle())
                && Objects.equals(getDescription(), product.getDescription())
                && Objects.equals(getAuthor(), product.getAuthor())
                && Objects.equals(getCoverImage(), product.getCoverImage())
                && Objects.equals(getPrice(), product.getPrice())
                && Objects.equals(getQuantity(), product.getQuantity());

    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getAuthor(),
                getCoverImage(), getPrice(), getQuantity());
    }
}