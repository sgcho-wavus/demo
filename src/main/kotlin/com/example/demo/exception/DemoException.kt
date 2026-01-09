package com.example.demo.exception

import com.example.demo.api.ErrorMessage

open class DemoException(
    val errorMessage: ErrorMessage
) : RuntimeException(errorMessage.message)
