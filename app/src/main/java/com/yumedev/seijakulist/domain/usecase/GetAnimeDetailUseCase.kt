package com.yumedev.seijakulist.domain.usecase

import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.AnimeDetail
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(

    private val repository: AnimeRepository

) {
    suspend operator fun invoke(animeId: Int) : AnimeDetail {
        return repository.getAnimeDetailsById(animeId)
    }
}