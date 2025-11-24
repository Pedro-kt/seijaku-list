package com.yumedev.seijakulist.data.remote.models.anime_themes

data class AnimeThemesDto(
    val data: AnimeMusicDto
)

data class AnimeMusicDto(
    val openings: List<String>,
    val endings: List<String>
)