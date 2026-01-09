package com.example.demo

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DemoController(
    private val demoService: DemoService
) {

    @GetMapping("/divide")
    fun divide(
        @RequestParam("a") a: Int,
        @RequestParam("b") b: Int
    ): ResponseEntity<DivideResponse> {
        val result = demoService.divide(a, b)
        return ResponseEntity.ok(DivideResponse(a, b, result, null))
    }

    data class DivideResponse(
        val a: Int,
        val b: Int,
        val result: Float,
        val error: String?
    )
}
