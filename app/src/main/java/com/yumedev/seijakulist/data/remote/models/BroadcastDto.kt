package com.yumedev.seijakulist.data.remote.models

import com.google.gson.annotations.SerializedName

data class BroadcastDto(
    val day: String?,
    val time: String?,
    val timezone: String?,
    @SerializedName("string") val broadcastString: String?
)
