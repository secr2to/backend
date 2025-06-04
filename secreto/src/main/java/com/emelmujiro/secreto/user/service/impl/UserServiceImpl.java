package com.emelmujiro.secreto.user.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emelmujiro.secreto.global.dto.response.SuccessResponseDto;
import com.emelmujiro.secreto.user.dto.request.DeleteUserRequestDto;
import com.emelmujiro.secreto.user.dto.request.UpdateSearchIdRequestDto;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.error.UserErrorCode;
import com.emelmujiro.secreto.user.exception.UserException;
import com.emelmujiro.secreto.user.repository.UserRepository;
import com.emelmujiro.secreto.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public SuccessResponseDto updateSearchId(UpdateSearchIdRequestDto dto) {
		userRepository.findBySearchId(dto.getSearchId())
			.ifPresent(u -> {
				throw new UserException(UserErrorCode.DUPLICATE_SEARCH_ID);
			});

		User user = findUser(dto.getUserId());
		return SuccessResponseDto.of(user.updateSearchId(dto.getSearchId()));
	}

	@Override
	@Transactional
	public SuccessResponseDto delete(DeleteUserRequestDto dto) {
		User user = findUser(dto.getUserId());
		return SuccessResponseDto.of(user.delete());
	}

	User findUser(Long id) {
		return userRepository.findActiveById(id)
			.orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
	}
}
