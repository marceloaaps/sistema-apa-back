package com.apa.back;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@ActiveProfiles("test")
class BackApplicationTests {

	@Value("${DB_NAME}")
	private String dbName;

	@Test
	void contextLoads() {
		System.out.println("DB Name: " + dbName);
	}

}
