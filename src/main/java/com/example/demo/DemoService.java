package com.example.demo;

import org.springframework.stereotype.Service;

import com.example.demo.api.ErrorCode;
import com.example.demo.api.ErrorMessage;
import com.example.demo.exception.DemoBizException;

@Service
public class DemoService {
	
	public float divide (int a, int b) {
		try {
			return a / b;
		}
		catch (Exception e) {
			throw new DemoBizException(ErrorMessage.from(ErrorCode.BAD_REQUEST, "0으로 나누기 오류"));
		}
	}
}
