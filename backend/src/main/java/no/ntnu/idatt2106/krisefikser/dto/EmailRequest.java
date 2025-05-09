package no.ntnu.idatt2106.krisefikser.dto;

import lombok.Getter;

/**
 * DTO for sending email requests.
 */
@Getter
public class EmailRequest {
    private final String to;
    private final String subject;
    private final String body;
    private final boolean html;

    public EmailRequest(String to,
                        String subject,
                        String body,
                        boolean html) {
        this.to               = to;
        this.subject          = subject;
        this.body             = body;
        this.html             = html;
    }
}
