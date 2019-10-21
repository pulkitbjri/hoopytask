package com.example.hoopy.NetworkResponse

data class FileUploadResponse(
    val metadata: Metadata,
    val urls: List<String>
)