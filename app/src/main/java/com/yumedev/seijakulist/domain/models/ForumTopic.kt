package com.yumedev.seijakulist.domain.models

data class ForumTopic(
    val malId: Int,
    val url: String,
    val title: String,
    val date: String,
    val authorUsername: String,
    val comments: Int,
    val lastCommentAuthor: String?,
    val lastCommentDate: String?
)
