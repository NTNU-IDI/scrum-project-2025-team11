package no.ntnu.idatt2106.krisefikser;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import no.ntnu.idatt2106.krisefikser.config.TestSecurityConfig;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@SpringBootTest
class KrisefikserApplicationTests {

	@Test
	void contextLoads() {
	}

}
