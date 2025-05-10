package com.example.store.service;

import com.example.store.exception.StoreException;
import com.example.store.util.ErrorMessages;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Custom validations service.
 */
@Service
public class ValidationService {

    public static final List<String> BANNED_LIST = List.of("Massador Diego");

    /**
     * One required validations is that we do not allow _Darth Vader_ to authenticate.
     * @param username String
     */
    public void performAuthenticateValidations(String username) {
        if (BANNED_LIST.contains(username)) {
            throw new StoreException(ErrorMessages.BANNED_USER);
        }
    }
}
