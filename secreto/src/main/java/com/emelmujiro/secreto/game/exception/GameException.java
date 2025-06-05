package com.emelmujiro.secreto.game.exception;

import com.emelmujiro.secreto.global.error.ErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;

public class GameException extends ApiException {
    public GameException(ErrorCode errorCode) {
        super(errorCode);
    }

    public GameException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
