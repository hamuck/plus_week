package com.example.demo.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.entity.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private ReservationService reservationService;

	@Test
	@DisplayName("reservation 생성 확인")
	void createReservationTest() throws Exception {
		Long itemId = 1L;
		Long userId = 1L;
		LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 1, 1);
		LocalDateTime endTime = LocalDateTime.of(2020, 1, 1, 1, 1);

		ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
			itemId, userId, startTime, endTime
		);

		Authentication authentication = new Authentication(userId, Role.USER);

		String requestBody = objectMapper.writeValueAsString(reservationRequestDto);

		mockMvc.perform(post("/reservations")
				.sessionAttr(GlobalConstants.USER_AUTH, authentication)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isOk());

	}

	@Test
	@DisplayName("reservation 조회")
	void getReservationTest() throws Exception {
		Long userId = 1L;
		Authentication authentication = new Authentication(userId, Role.USER);

		mockMvc.perform(get("/reservations")
			.sessionAttr(GlobalConstants.USER_AUTH,authentication))
		.andExpect(status().isOk());
	}

	@Test
	@DisplayName("reservation 검색 조회")
	void searchReservationTest() throws Exception {
		Long itemId = 1L;
		Long userId = 1L;
		Authentication authentication = new Authentication(userId, Role.USER);

		mockMvc.perform(get("/reservations/search")
			.sessionAttr(GlobalConstants.USER_AUTH,authentication)
			.param("itemId", itemId.toString())
			.param("userId", userId.toString()))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("상태 변경")
	void updateReservationTest() throws Exception {

		Long reservationId = 1L;
		Long userId = 1L;
		String status = "APPROVED";

		Authentication authentication = new Authentication(userId, Role.USER); // 인증 정보 생성

		given(reservationService.updateReservationStatus(reservationId, status))
			.willReturn(ResponseEntity.ok().build()); // 예시: boolean 반환

		mockMvc.perform(patch("/reservations/{reservationId}/update-status", reservationId) // 경로 변수 사용
				.sessionAttr(GlobalConstants.USER_AUTH, authentication) // 세션에 인증 정보 포함
				.contentType(MediaType.APPLICATION_JSON) // JSON 콘텐츠 타입 설정
				.content("{\"status\":\"" + status + "\"}")) // 상태를 JSON 형태로 전달
			.andExpect(status().isOk()); // 200 OK 응답을 기대
	}

}
