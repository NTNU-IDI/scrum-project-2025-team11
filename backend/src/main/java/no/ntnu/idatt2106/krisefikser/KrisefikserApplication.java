package no.ntnu.idatt2106.krisefikser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point for the Krisefikser Spring Boot application.
 * <p>
 * Enables component scanning, auto-configuration, and scheduling support.
 * <p>
 * Annotations:
 * <ul>
 *   <li>@EnableScheduling - allows scheduled tasks defined in the application to run.</li>
 *   <li>@SpringBootApplication - denotes this class as the primary Spring Boot configuration class,
 *       combining @Configuration, @EnableAutoConfiguration, and @ComponentScan.</li>
 * </ul>
 */
@EnableScheduling
@SpringBootApplication
public class KrisefikserApplication {

    /**
     * Main method to launch the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(KrisefikserApplication.class, args);
    }

}
