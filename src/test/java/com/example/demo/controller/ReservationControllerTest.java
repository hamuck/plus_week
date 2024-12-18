package com.example.demo.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;  // 이 부분 수정
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.example.demo.constants.GlobalConstants;
import com.example.demo.dto.Authentication;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Role;
import com.example.demo.repository.ReservationRepository;
import java.util.Collections;
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
	@MockitoBean
	private ReservationRepository reservationRepository;

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

	//테스트 제대로 진행이 안됨..
	@Test
	@DisplayName("중복 예약시간 예외 발생 확인")
	void createReservationConfilctTest() throws Exception {
		Long itemId = 1L;
		Long userId = 1L;
		LocalDateTime startTime = LocalDateTime.of(2020, 1, 1, 1, 1);
		LocalDateTime endTime = LocalDateTime.of(2020, 1, 1, 1, 1);

		// 이미 중복 예약이 있는 상황으로 설정
		given(reservationRepository.findConflictingReservations(itemId, startTime, endTime))
			.willReturn(Collections.singletonList(new Reservation()));

		Authentication authentication = new Authentication(userId, Role.USER);
		ReservationRequestDto reservationRequestDto = new ReservationRequestDto(itemId, userId, startTime, endTime);
		String requestBody = objectMapper.writeValueAsString(reservationRequestDto);

		mockMvc.perform(post("/reservations")
				.sessionAttr(GlobalConstants.USER_AUTH, authentication)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.message").value("해당 물건은 이미 그 시간에 예약이 있습니다."));
	}

}
