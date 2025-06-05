package com.emelmujiro.secreto.room.error;

import com.emelmujiro.secreto.global.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RoomErrorCode implements ErrorCode {

    NOT_EXIST_ROOM_USER(HttpStatus.BAD_REQUEST, "해당 방 유저가 존재하지 않습니다."),
    NOT_EXIST_ROOM(HttpStatus.BAD_REQUEST, "해당 방이 존재하지 않습니다."),
    NOT_EXIST_ROOM_CODE(HttpStatus.BAD_REQUEST, "해당 코드를 가진 방이 존재하지 않습니다."),
    USER_ROOM_INVALID(HttpStatus.BAD_REQUEST, "해당 유저는 해당 방에 속해있지 않습니다."),
    INVALID_ROOM_STATUS(HttpStatus.BAD_REQUEST, "해당 방 상태는 정의되어 있지 않습니다."),
    ROOMUSER_ROOM_INVALID(HttpStatus.BAD_REQUEST, "해당 방에는 해당 식별키를 가진 방 유저가 없습니다."),
    INVAILD_AUTHORITY(HttpStatus.NOT_ACCEPTABLE, "해당 방 유저는 수행 권한이 없습니다."),
    ALREADY_IN_ROOM(HttpStatus.NOT_ACCEPTABLE, "이미 입장한 방입니다."),
    NOT_ENOUGH_USER_TO_START_ROOM(HttpStatus.NOT_ACCEPTABLE, "최소 3인 이상부터 게임을 시작할 수 있습니다."),
    ALREADY_STARTED_OR_TERMINATED(HttpStatus.BAD_REQUEST, "이미 시작한 방이거나 끝난 방입니다."),
    ALREADY_ACCEPTED_ROOM_USER(HttpStatus.NOT_ACCEPTABLE, "이미 수락된 유저입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
