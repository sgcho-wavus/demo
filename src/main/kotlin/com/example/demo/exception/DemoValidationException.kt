package com.example.demo.exception

import com.example.demo.api.ErrorMessage

class DemoValidationException(
    errorMessage: ErrorMessage
) : DemoException(errorMessage)
