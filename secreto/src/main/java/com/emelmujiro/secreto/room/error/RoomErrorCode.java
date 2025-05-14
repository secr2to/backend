package com.emelmujiro.secreto.room.error;

import com.emelmujiro.secreto.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoomErrorCode implements ErrorCode {

    NOT_EXIST_ROOM_USER(HttpStatus.BAD_REQUEST, "해당 방 유저가 존재하지 않습니다."),
    USER_ROOM_INVALID(HttpStatus.BAD_REQUEST, "해당 유저는 해당 방에 속해있지 않습니다."),
    INVALID_ROOM_STATUS(HttpStatus.BAD_REQUEST, "해당 방 상태는 정의되어 있지 않습니다."),
    ROOMUSER_ROOM_INVALID(HttpStatus.BAD_REQUEST, "해당 방에는 해당 식별키를 가진 방 유저가 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
