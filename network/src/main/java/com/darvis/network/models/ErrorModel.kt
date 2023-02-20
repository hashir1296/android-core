package com.darvis.network.models

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ErrorModel(
    @SerialName(value = "message") val message: String? = null,
    @SerialName(value = "error") val error: String? = null,
    @SerialName(value = "code") val code: Int?,
    @SerialName(value = "detail") val detail: Int?,
)
