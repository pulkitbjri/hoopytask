package com.example.hoopy.NetworkResponse

import com.example.hoopy.models.User

data class UserGetResponse(
    val data:List <User>,
    val metadata: Metadata
)