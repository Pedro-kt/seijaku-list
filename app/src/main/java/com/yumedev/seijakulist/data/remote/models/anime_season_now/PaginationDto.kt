package com.yumedev.seijakulist.data.remote.models.anime_season_now

import com.google.gson.annotations.SerializedName

data class PaginationSeasonNowDto(
    @SerializedName("last_visible_page") val lastVisiblePage: Int?,
    @SerializedName("has_next_page") val hasNextPage: Boolean?,
    @SerializedName("current_page") val currentPage: Int?,
    val items: ItemsSeasonNowDto?
)

data class ItemsSeasonNowDto(
    val count: Int?,
    val total: Int?,
    @SerializedName("per_page") val perPage: Int?
)

