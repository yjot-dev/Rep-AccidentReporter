package com.yjotdev.accidentreporter.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO para la respuesta del endpoint de creación de token
 */
data class TokenDto(
    @SerializedName("token")
    val token: String = ""
)