package com.yumedev.seijakulist.data.remote.models.recent_recommendations

import com.google.gson.annotations.SerializedName
import com.yumedev.seijakulist.data.remote.models.ImagesDto

data class RecentAnimeRecommendationsResponseDto(
    @SerializedName("data") val data: List<RecentRecommendationEntryDto>
)

data class RecentRecommendationEntryDto(
    @SerializedName("entry") val entry: List<RecentRecommendationAnimeDto>,
    @SerializedName("content") val content: String?,
    @SerializedName("user") val user: RecentRecommendationUserDto?
)

data class RecentRecommendationAnimeDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("images") val images: ImagesDto?
)

data class RecentRecommendationUserDto(
    @SerializedName("url") val url: String?,
    @SerializedName("username") val username: String?
)