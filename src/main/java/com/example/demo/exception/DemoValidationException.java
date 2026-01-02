package com.example.demo.exception;

import com.example.demo.api.ErrorMessage;

public class DemoValidationException extends DemoException {
    public DemoValidationException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
