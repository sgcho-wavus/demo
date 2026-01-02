package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloKotlin {
    
    @GetMapping("/hello-kotlin")
    fun hello(): String {
        return "Hello from Kotlin!"
    }

}