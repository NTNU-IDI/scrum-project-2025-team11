package no.ntnu.idatt2106.krisefikser.exceptionhandler;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 * <p>
 * Results in an HTTP 404 Not Found response status.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with a formatted message.
     *
     * @param resource the type or name of the resource (e.g., "User", "Household")
     * @param field the field used to search for the resource (e.g., "id", "email")
     * @param value the value of the search field that was not found
     */
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s not found with %s = %s", resource, field, value));
    }
}
