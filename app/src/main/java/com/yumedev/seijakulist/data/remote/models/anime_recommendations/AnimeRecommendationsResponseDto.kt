package com.yumedev.seijakulist.data.remote.models.anime_recommendations

import com.google.gson.annotations.SerializedName
import com.yumedev.seijakulist.data.remote.models.GenreDto
import com.yumedev.seijakulist.data.remote.models.ImagesDto

data class AnimeRecommendationsResponseDto(
    @SerializedName("data") val data: List<AnimeRecommendationEntryDto>
)

data class AnimeRecommendationEntryDto(
    @SerializedName("entry") val entry: AnimeRecommendationItemDto,
    @SerializedName("votes") val votes: Int
)

data class AnimeRecommendationItemDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String?,
    @SerializedName("images") val images: ImagesDto?,
    @SerializedName("score") val score: Float?,
    @SerializedName("year") val year: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("genres") val genres: List<GenreDto?>?,
    @SerializedName("episodes") val episodes: Int?,
)
