package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class ItemsDto(
    val count: Int?,
    val total: Int?,
    @SerializedName("per_page") val perPage: Int?
)
