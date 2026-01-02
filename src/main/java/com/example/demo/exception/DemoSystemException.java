package com.example.demo.exception;

import com.example.demo.api.ErrorMessage;

public class DemoSystemException extends DemoException {
    public DemoSystemException(ErrorMessage errorMessage) {
        super(errorMessage);
    }
}
