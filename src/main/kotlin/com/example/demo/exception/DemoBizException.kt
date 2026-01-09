package com.example.demo.exception

import com.example.demo.api.ErrorMessage

class DemoBizException(
    errorMessage: ErrorMessage
) : DemoException(errorMessage)
