package com.emelmujiro.secreto.example_template.exception;

import com.emelmujiro.secreto.global.error.ErrorCode;
import com.emelmujiro.secreto.global.exception.ApiException;

public class TestException extends ApiException {

    public TestException(ErrorCode errorCode) {
        super(errorCode);
    }
}

