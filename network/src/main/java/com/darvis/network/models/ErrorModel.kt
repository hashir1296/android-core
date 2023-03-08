package com.darvis.network.models

import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
data class ErrorModel(
    @SerialName(value = "message") val message: String? = null,
    @SerialName(value = "error") val error: String? = null,
    @SerialName(value = "code") val code: Int?,
    @SerialName(value = "detail") val detail: String?,
)
