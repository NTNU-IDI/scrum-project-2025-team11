package no.ntnu.idatt2106.krisefikser.exceptionhandler;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
  private int status;
  private String message;
  private Map<String,String> fieldErrors;  // Optional, can be null if not needed
}
