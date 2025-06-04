package com.emelmujiro.secreto.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.dto.SecurityContextUser;
import com.emelmujiro.secreto.auth.util.SecurityContextUtil;
import com.emelmujiro.secreto.global.response.ApiResponse;
import com.emelmujiro.secreto.user.dto.request.UpdateSearchIdRequestDto;
import com.emelmujiro.secreto.user.message.UserMessage;
import com.emelmujiro.secreto.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@GetMapping("/basic")
	public ResponseEntity<?> getBasicUserInfo() {
		SecurityContextUser principal = SecurityContextUtil.getPrincipal();
		return ApiResponse.builder()
			.data(principal.toLoginResponse())
			.success();
	}

	@PatchMapping("/search-id")
	public ResponseEntity<?> updateSearchId(@RequestBody UpdateSearchIdRequestDto updateSearchIdRequest) {
		return ApiResponse.builder()
			.data(userService.updateSearchId(updateSearchIdRequest))
			.message(UserMessage.UPDATE_SEARCH_ID_SUCCESS.getMessage())
			.success();
	}
}
