package com.emelmujiro.secreto.game.controller;

import com.emelmujiro.secreto.game.dto.CreateSystemCharacterRequestDto;
import com.emelmujiro.secreto.game.service.GameService;
import com.emelmujiro.secreto.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/games")
@RequiredArgsConstructor
@RestController
public class GameController {

    private final GameService gameService;

    @PostMapping("/system-character")
    public ResponseEntity<ApiResponse<Object>> createSystemCharacter(@RequestBody CreateSystemCharacterRequestDto params) {

        gameService.createSystemCharacter(params);

        return ApiResponse.builder()
                .data(null)
                .status(HttpStatus.CREATED)
                .message("시스템 캐릭터 색상 정보를 등록하였습니다.")
                .success();
    }
}
