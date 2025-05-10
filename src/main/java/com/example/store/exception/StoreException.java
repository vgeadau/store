package com.example.store.exception;

/**
 * Generic exception for our Store project.
 * As this is a POC both authentication and product management uses this exception.
 * </br>
 * REASON to have a custom exception that extends RuntimeException (unchecked exception)
 * and not checked exceptions such as (I/O Exception or Exception) was:
 * </br>
 * 1 - Clean code / vs verbose
 * </br>
 * 2 - Better suited for business logic errors
 * Should bubble up to the controller layer
 * Are handled globally (e.g. via @ControllerAdvice)
 * Don’t represent programming mistakes or recoverable conditions
 * </br>
 * 3 - Easier integration with Spring’s exception handling
 * Spring naturally favors RuntimeException for propagating errors, especially in @Service, @RestController, etc.
 * </br>
 * 4. Avoids swallowing or over-handling errors
 * Checked exceptions often lead to catch blocks where developers may:
 * Swallow exceptions silently (catch (Exception e) {} )
 * Wrap them repeatedly in new exceptions just to compile
 * Unchecked exceptions encourage you to only catch what you intend to handle.
 * </br>
 * 5. When to use checked exceptions (extends Exception)?
 * When the caller is expected to handle the issue immediately (e.g., IOException, SQLException)
 * For APIs/libraries that require strict error contracts
 */
public class StoreException extends RuntimeException {

    public StoreException(String message) {
        super(message);
    }

    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }
}