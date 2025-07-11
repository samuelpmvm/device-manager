package smarcos.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("integration")
class DeviceManagerApplicationTests extends PostgresIntegrationTest {

	@Test
	void contextLoads() {
		Assertions.assertTrue(true);
	}

}
