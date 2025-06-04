package com.emelmujiro.secreto.user.service;

import com.emelmujiro.secreto.global.dto.response.SuccessResponseDto;
import com.emelmujiro.secreto.user.dto.request.UpdateSearchIdRequestDto;

public interface UserService {

	SuccessResponseDto updateSearchId(UpdateSearchIdRequestDto dto);

}
