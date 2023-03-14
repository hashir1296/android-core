package com.darvis.androidcore


import androidx.annotation.Keep
import kotlinx.serialization.SerialName

@Keep
@kotlinx.serialization.Serializable
data class PostMockResponseIObject(
    @SerialName(value = "body") val body: String?,
    @SerialName(value = "id") val id: Int?,
    @SerialName(value = "title") val title: String?,
    @SerialName(value = "userId") val userId: Int?
)