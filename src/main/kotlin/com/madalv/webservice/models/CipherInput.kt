package com.madalv.webservice.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CipherInput(
    @SerialName("text") val text: String,
    @SerialName("key") val key: Int,
    @SerialName("type") val type: String
)