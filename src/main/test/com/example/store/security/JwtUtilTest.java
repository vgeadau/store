package com.example.store.security;

import com.example.store.model.MyUserDetails;
import com.example.store.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private final static String USER_NAME = "mr_apricot";
    private final static String INVALID_USER_NAME = "mr_peach";
    private final JwtUtil target = new JwtUtil();

    @Test
    void extractUsername_shouldSucceed() {
        // given
        final String token = getToken();

        // when
        final String result = target.extractUsername(token);

        // then
        assertEquals(USER_NAME, result);
    }

    @Test
    void extractExpiration_shouldSucceed() {
        // given
        final Date currentDate = new Date();
        final String token = getToken();

        // when
        final Date result = target.extractExpiration(token);

        // then expiration date should be after current date as it is defined to last for 10 hours (see TEN_HOURS).
        assertTrue(currentDate.before(result));
    }

    @Test
    void validateToken_withValidToken_shouldReturnTrue() {
        // given
        final String token = getToken();
        final MyUserDetails userDetails = getUserDetails(USER_NAME);

        // when
        final Boolean result = target.validateToken(token, userDetails);

        // then
        assertTrue(result);
    }

    @Test
    void validateToken_withInvalidToken_shouldReturnFalse() {
        // given
        final String token = getToken();
        final MyUserDetails userDetails = getUserDetails(INVALID_USER_NAME);

        // when
        final Boolean result = target.validateToken(token, userDetails);

        // then
        assertFalse(result);
    }

    @Test
    void generateToken_shouldSucceed() {
        // given this method is tested indirectly in above testing methods.

        // when
        final String result = getToken();

        // then so in this particular case it's enough to test that the token resulted is not empty.
        assertFalse(result.isEmpty());
    }

    /**
     * Creates a token using USER_NAME
     * @return a string
     */
    private String getToken() {
        final MyUserDetails myUserDetails = getUserDetails(USER_NAME);
        return target.generateToken(myUserDetails);
    }

    /**
     * Method that constructs MyUserDetails to be used for testing purposes.
     * @param username String
     * @return MyUserDetails object
     */
    private MyUserDetails getUserDetails(String username) {
        final User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        return new MyUserDetails(user);
    }
}
