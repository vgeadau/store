package com.example.store.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * User entity which is being persisted.
 * </br>
 * Note: we consider username, pseudonym unique.
 */
@Entity
@SuppressWarnings("unused")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String pseudonym;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(String pseudonym) {
        this.pseudonym = pseudonym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getId(), user.getId())
                && Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getPassword(), user.getPassword())
                && Objects.equals(getPseudonym(), user.getPseudonym());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), getPseudonym());
    }
}