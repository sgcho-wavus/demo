package com.example.demo.exception

import com.example.demo.api.ErrorMessage

class DemoSystemException(
    errorMessage: ErrorMessage
) : DemoException(errorMessage)
