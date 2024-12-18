package com.example.demo.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordEncoderTest {

	@Test
	void testEncode(){
		String password = "password1234";

		String encode = PasswordEncoder.encode(password);

		assertNotNull(encode,"encode not null");
		assertNotEquals(encode,password,"encode not equal");
	}

	@Test
	void testMatches(){
		String password = "password1234";
		String encode = PasswordEncoder.encode(password);

		boolean isMatch = PasswordEncoder.matches("password1234", encode);

		assertTrue(isMatch,"encode match");
	}
}
