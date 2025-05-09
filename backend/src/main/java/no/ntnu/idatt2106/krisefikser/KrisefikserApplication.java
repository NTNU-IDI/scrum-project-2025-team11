package no.ntnu.idatt2106.krisefikser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KrisefikserApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrisefikserApplication.class, args);
	}

}
