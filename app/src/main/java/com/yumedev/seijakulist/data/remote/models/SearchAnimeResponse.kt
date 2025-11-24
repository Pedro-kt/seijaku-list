package com.yumedev.seijakulist.data.remote.models

import com.yumedev.seijakulist.data.remote.models.anime_random.AnimeCardDto
import com.yumedev.seijakulist.domain.models.AnimeCard
import com.google.gson.annotations.SerializedName

data class SearchAnimeResponse(
    val pagination: PaginationDto?,
    val data: List<AnimeCardDto?>
)

data class PaginationDto(
    @SerializedName("last_visible_page") val lastVisiblePage: Int?,
    @SerializedName("has_next_page") val hasNextPage: Boolean?,
    @SerializedName("current_page") val currentPage: Int?,
    val items: ItemsDto?
)