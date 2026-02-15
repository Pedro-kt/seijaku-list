package com.yumedev.seijakulist.domain.models

data class AnimeVideos(
    val promos: List<AnimePromo>,
    val episodes: List<AnimeEpisodeVideo>,
    val musicVideos: List<AnimeMusicVideo>
)

data class AnimePromo(
    val title: String?,
    val youtubeId: String?,
    val youtubeUrl: String?,
    val thumbnailUrl: String?
)

data class AnimeEpisodeVideo(
    val malId: Int?,
    val title: String?,
    val episode: String?,
    val url: String?,
    val imageUrl: String?
)

data class AnimeMusicVideo(
    val title: String?,
    val youtubeId: String?,
    val youtubeUrl: String?,
    val thumbnailUrl: String?,
    val songTitle: String?,
    val artist: String?
)
