package com.example.demo.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//패스워드 암호화 테스트 코드
public class PasswordEncoderTest {

	//암호화 작동 테스트(기존 비밀번호와 암호화 값 비교)
	@Test
	void testEncode(){
		String password = "password1234";

		String encode = PasswordEncoder.encode(password);

		assertNotNull(encode,"encode not null");
		assertNotEquals(encode,password,"encode not equal");
	}

	//복호화 작동 테스트(복호화 값과 기존 비밀번호 값 비교)
	@Test
	void testMatches(){
		String password = "password1234";
		String encode = PasswordEncoder.encode(password);

		boolean isMatch = PasswordEncoder.matches("password1234", encode);

		assertTrue(isMatch,"encode match");
	}
}
