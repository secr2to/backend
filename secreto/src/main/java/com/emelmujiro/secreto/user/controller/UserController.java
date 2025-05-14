package com.emelmujiro.secreto.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emelmujiro.secreto.auth.dto.SecurityContextUser;
import com.emelmujiro.secreto.auth.util.SecurityContextUtil;
import com.emelmujiro.secreto.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

	@GetMapping("/basic")
	public ResponseEntity<?> getBasicUserInfo() {
		SecurityContextUser principal = SecurityContextUtil.getPrincipal();
		return ApiResponse.builder()
			.data(principal.toLoginResponse())
			.success();
	}
}
