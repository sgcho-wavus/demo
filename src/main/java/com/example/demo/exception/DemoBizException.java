package com.example.demo.exception;

import com.example.demo.api.ErrorMessage;

public class DemoBizException extends DemoException {
    public DemoBizException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
