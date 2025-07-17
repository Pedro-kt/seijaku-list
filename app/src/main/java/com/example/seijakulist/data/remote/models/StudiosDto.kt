package com.example.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class StudiosDto(
    @SerializedName("mal_id") val idStudio: Int?,
    @SerializedName("name") val nameStudio: String?
)
