package com.emelmujiro.secreto.chatting.error;

import com.emelmujiro.secreto.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChattingErrorCode implements ErrorCode {

    NOT_EXIST_CHATTING_ROOM(HttpStatus.BAD_REQUEST, "해당 채팅방이 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
