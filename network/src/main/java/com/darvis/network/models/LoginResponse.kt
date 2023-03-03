package com.darvis.network.models

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
data class LoginResponse(
    @SerialName(value = "code")
    val code: Int?,
    @SerialName(value = "data")
    val data: MyData?,
    @SerialName(value = "message")
    val message: String?
) {
    @kotlinx.serialization.Serializable
    data class MyData(
        @SerialName(value = "access_token")
        val accessToken: String?,
        @SerialName(value = "expiry")
        val expiry: Int?,
        @SerialName(value = "refresh_token")
        val refreshToken: String?,
        @SerialName(value = "token_type")
        val tokenType: String?,
        @SerialName(value = "user_role")
        val userRole: String?
    )
}
