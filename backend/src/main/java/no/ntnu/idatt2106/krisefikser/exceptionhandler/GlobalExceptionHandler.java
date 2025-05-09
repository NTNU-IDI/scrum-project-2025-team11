package no.ntnu.idatt2106.krisefikser.exceptionhandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that intercepts application-wide exceptions
 * and converts them into standardized HTTP error responses.
 */
@RestControllerAdvice(basePackages = "no.ntnu.idatt2106.krisefikser")
public class GlobalExceptionHandler {

    /**
     * Handles validation errors thrown when a @Valid annotated request body fails.
     *
     * @param ex the exception containing binding errors
     * @return an ErrorResponse with HTTP 400 and details of each invalid field
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        Map<String, String> errors = fieldErrors.stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (msg1, msg2) -> msg1
            ));
        return new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
            errors
        );
    }

    /**
     * Handles cases where a requested resource is not found.
     *
     * @param ex the ResourceNotFoundException
     * @return an ErrorResponse with HTTP 404 and the exception message
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
        return new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            null
        );
    }

    /**
     * Handles illegal argument exceptions thrown by service or controller logic.
     *
     * @param ex the IllegalArgumentException
     * @return an ErrorResponse with HTTP 400 and the exception message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArg(IllegalArgumentException ex) {
        return new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            null
        );
    }

    /**
     * Catches all other unhandled exceptions, excluding security exceptions,
     * and returns an HTTP 500 response. Security-related exceptions are re-thrown
     * to let Spring Security handle them.
     *
     * @param ex the thrown exception
     * @return an ErrorResponse with HTTP 500 and a generic error message
     * @throws Exception rethrows security exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAll(Exception ex) throws Exception {
        if (ex instanceof AuthenticationException || ex instanceof AccessDeniedException) {
            throw ex;
        }
        return new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            null
        );
    }
}
