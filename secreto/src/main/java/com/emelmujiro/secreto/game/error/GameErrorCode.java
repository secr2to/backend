package com.emelmujiro.secreto.game.error;

import com.emelmujiro.secreto.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GameErrorCode implements ErrorCode {

    INVALID_COLOR(HttpStatus.BAD_REQUEST, "유효하지 않은 옷 색상 또는 피부 색상입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
