package no.ntnu.idatt2106.krisefikser.service;

import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.http.HttpHeaders;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RecaptchaService {
    @Value("${recaptcha.secret-key}")
    private String secretKey;

    @Value("${recaptcha.verify-url}")
    private String verifyUrl;

    private final RestTemplate restTemplate;
    public RecaptchaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    Logger logger = Logger.getLogger(RecaptchaService.class.getName());

    /**
     * Validates the reCAPTCHA token received from the client.
     *
     * @param token The reCAPTCHA token to validate.
     * @param expectedAction The expected action for the reCAPTCHA validation.
     * @return true if the reCAPTCHA validation is successful, false otherwise.
     */
    public boolean validateCaptcha(String token, String expectedAction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", secretKey);
        map.add("response", token);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.exchange(verifyUrl, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String responseBody = response.getBody();
            if (responseBody != null) {
                try {
                    // Parse the response body
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

                    boolean success = (Boolean) responseMap.get("success");
                    if (!success) {
                        logger.warning("reCAPTCHA validation failed: " + responseMap.get("error-codes"));
                        return false;
                    }

                    // Validate the score
                    Double score = (Double) responseMap.get("score");
                    String action = (String) responseMap.get("action");

                    if (!expectedAction.equals(action)) {
                        logger.warning("reCAPTCHA action mismatch. Expected: " + expectedAction + ", but got: " + action);
                        return false;
                    }

                    if (score < 0.5) { // Adjust the threshold as needed
                        logger.warning("reCAPTCHA score too low: " + score);
                        return false;
                    }

                    logger.info("reCAPTCHA validation successful with score: " + score);
                    return true;

                } catch (Exception e) {
                    logger.severe("Failed to parse reCAPTCHA response: " + e.getMessage());
                    return false;
                }
            } else {
                logger.warning("Empty response body from reCAPTCHA API");
                return false;
            }
        } else {
            logger.warning("Failed to connect to reCAPTCHA service: " + response.getStatusCode());
            return false;
        }
    }

}
