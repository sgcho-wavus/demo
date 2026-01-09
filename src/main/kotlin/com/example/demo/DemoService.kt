package com.example.demo

import com.example.demo.api.ErrorCode
import com.example.demo.api.ErrorMessage
import com.example.demo.exception.DemoBizException
import org.springframework.stereotype.Service

@Service
class DemoService {

    fun divide(a: Int, b: Int): Float {
        return try {
            (a.toFloat() / b.toFloat())
        } catch (e: Exception) {
            throw DemoBizException(ErrorMessage.from(ErrorCode.BAD_REQUEST, "0으로 나누기 오류"))
        }
    }
}
