package com.emelmujiro.secreto.example_template.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TestException extends RuntimeException {

    public TestException(String msg) {
        super(msg);
    }
}

