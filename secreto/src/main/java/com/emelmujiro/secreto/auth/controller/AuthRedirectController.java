package com.emelmujiro.secreto.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthRedirectController {

	@GetMapping("/redirect")
	public String redirect(@RequestParam("tempId") String tempId, @RequestParam("isNewUser") boolean isNewUser) {
		log.info("tempId={}", tempId);
		return "/login-success";
	}
}