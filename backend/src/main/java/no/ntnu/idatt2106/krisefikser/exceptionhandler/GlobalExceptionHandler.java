package no.ntnu.idatt2106.krisefikser.exceptionhandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
    Map<String,String> errors = fieldErrors.stream()
      .collect(Collectors.toMap(
        FieldError::getField,
        FieldError::getDefaultMessage,
        (msg1,msg2) -> msg1  // in case two errors on one field
      ));

    return new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      "Validation failed",
      errors
    );
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
    return new ErrorResponse(
      HttpStatus.NOT_FOUND.value(),
      ex.getMessage(),
      null
    );
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArg(IllegalArgumentException ex) {
    return new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      ex.getMessage(),
      null
    );
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleAll(Exception ex) {
    // you might want to log ex here
    return new ErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "An unexpected error occurred", 
      null
    );
  }
}
