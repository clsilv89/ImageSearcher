package com.caiosilva.tagsearcher.data.model


data class ErrorResponse(
    val errorDescription: String,
    val detailMessage: String,
    val causes: Map<String, String> = emptyMap(),
    val message: String
)