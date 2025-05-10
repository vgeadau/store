package com.example.store.service;

import com.example.store.exception.StoreException;
import com.example.store.util.ErrorMessages;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Custom validations service.
 * At this point list is held in memory, however, this implementation can be extended by adding
 * banning functionality. This is just a POC.
 */
@Service
public class ValidationService {

    public static final List<String> BANNED_LIST = List.of("Diego");

    /**
     * We don't allow user Diego to authenticate.
     * @param username String
     */
    public void performAuthenticateValidations(String username) {
        if (BANNED_LIST.contains(username)) {
            throw new StoreException(ErrorMessages.BANNED_USER);
        }
    }
}
