package com.yumedev.seijakulist.data.remote.models.anime_season_now

import com.yumedev.seijakulist.data.remote.models.AnimeDto

data class AnimeDetailSeasonNowResponseDto(
    val pagination: PaginationSeasonNowDto?,
    val data: List<AnimeDto?>
)


