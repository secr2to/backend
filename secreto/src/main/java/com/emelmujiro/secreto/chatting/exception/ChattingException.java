package com.emelmujiro.secreto.chatting.exception;

import com.emelmujiro.secreto.global.error.ErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;

public class ChattingException extends ApiException {

    public ChattingException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChattingException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
