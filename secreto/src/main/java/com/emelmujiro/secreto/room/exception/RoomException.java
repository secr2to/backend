package com.emelmujiro.secreto.room.exception;

import com.emelmujiro.secreto.global.error.ErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;

public class RoomException extends ApiException {
    public RoomException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RoomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
