package com.yumedev.seijakulist.data.remote.models.anime_forum

import com.google.gson.annotations.SerializedName

data class AnimeForumResponseDto(
    @SerializedName("data") val data: List<ForumTopicDto>
)

data class ForumTopicDto(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("url") val url: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("date") val date: String?,
    @SerializedName("author_username") val authorUsername: String?,
    @SerializedName("comments") val comments: Int,
    @SerializedName("last_comment") val lastComment: LastCommentDto?
)

data class LastCommentDto(
    @SerializedName("author_username") val authorUsername: String?,
    @SerializedName("date") val date: String?
)
