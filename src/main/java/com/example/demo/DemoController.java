package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DemoController {
	
	private final DemoService demoService;
	
    @GetMapping("/divide")
    public ResponseEntity<DivideResponse> divide(@RequestParam(value="a") int a, @RequestParam(value="b") int b) {
        var result = demoService.divide(a, b);
        return ResponseEntity.ok(new DivideResponse(a, b, result, null));
    }
    
    public record DivideResponse(int a, int b, float result, String error) {}
}
