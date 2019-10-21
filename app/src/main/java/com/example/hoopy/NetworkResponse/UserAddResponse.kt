package com.example.hoopy.NetworkResponse

import com.example.hoopy.models.User

data class UserAddResponse(
    val data: User,
    val metadata: Metadata
)