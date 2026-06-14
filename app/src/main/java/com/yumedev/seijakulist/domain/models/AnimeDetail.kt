package com.yumedev.seijakulist.domain.models

import com.yumedev.seijakulist.data.remote.models.AnimeProducerDto
import com.yumedev.seijakulist.data.remote.models.DemographicDto
import com.yumedev.seijakulist.data.remote.models.GenreDto
import com.yumedev.seijakulist.data.remote.models.StudiosDto

data class AnimeDetail(
    val malId: Int,
    val title: String,
    val titleEnglish: String,
    val titleJapanese: String,
    val images: String,
    val trailer: TrailerInfo?,
    val typeAnime: String,
    val source: String,
    val episodes: Int,
    val status: String,
    val aired: String,
    val broadcast: BroadcastInfo?,
    val duration: String,
    val rating: String,
    val score: Float,
    val scoreBy: Int,
    val rank: Int,
    val synopsis: String,
    val background: String,
    val season: String,
    val year: Any,
    val producers: List<AnimeProducerDto?>,
    val studios: List<StudiosDto?>,
    val genres: List<GenreDto?>,
    val demographics: List<DemographicDto?>
)

data class TrailerInfo(
    val youtubeId: String?,
    val url: String?,
    val embedUrl: String?,
    val thumbnailUrl: String?
)

data class BroadcastInfo(
    val day: String?,
    val time: String?,
    val timezone: String?,
    val fullString: String?
)
