package com.example.seijakulist.data.remote.models.anime_season_now

import com.example.seijakulist.data.remote.models.AnimeDto

data class AnimeDetailSeasonNowResponseDto(
    val pagination: PaginationSeasonNowDto?,
    val data: List<AnimeDto?>
)


