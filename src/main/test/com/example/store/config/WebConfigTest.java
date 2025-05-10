package com.example.store.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.converter.HttpMessageConverter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link WebConfig}.
 * </br>
 * Here are some reasons why not to clutter unit test methods with "public" access modifier:
 * </br>
 * - Since JUnit 4, 5 test methods can be package private.
 * - Older version requires public but modern don't (see below code).
 * - Cleaner code
 * - Avoid misleading (which implies that the tested method is located in a different package).
 * </br>
 * "final" keyword used in local variables is also used to achieve cleaner code, as lack of usage
 * implies that the local variable is being reused.
 * Therefor all local variables which are not reused should have final keyword.
 * </br>
 * Strategy chosen for unit testing is GWT because it offers more clarity than AAA (Arrange–Act–Assert)
 * - GWT (Given-When-Then) is used primarily in Behavior-Driven Development (BDD) style tests.
 * - Given: Setup preconditions or context
 * - When: Execute the operation being tested
 * - Then: Verify the result or outcome
 * Another big advantage is that tested method can be located instantly from the name of the tested method.
 */
@ExtendWith(MockitoExtension.class)
class WebConfigTest {
    /**
     * Class being tested (named as target).
     */
    private final WebConfig target = new WebConfig();

    @Test
    void createXmlHttpMessageConverter_shouldSucceed() {
        // when
        final HttpMessageConverter<Object> result = target.createXmlHttpMessageConverter();

        // then
        assertNotNull(result);
    }

    @Test
    void createJsonHttpMessageConverter_shouldSucceed() {
        // when
        final HttpMessageConverter<Object> result = target.createJsonHttpMessageConverter();

        // then
        assertNotNull(result);
    }
}
