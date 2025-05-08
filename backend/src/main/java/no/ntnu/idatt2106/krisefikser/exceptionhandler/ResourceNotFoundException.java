package no.ntnu.idatt2106.krisefikser.exceptionhandler;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String resource, String field, Object value) {
    super(String.format("%s not found with %s = %s", resource, field, value));
  }
}

