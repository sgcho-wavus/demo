package com.example.demo.exception;

import com.example.demo.api.ErrorMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DemoException extends RuntimeException {
	private final ErrorMessage errorMessage;
}
