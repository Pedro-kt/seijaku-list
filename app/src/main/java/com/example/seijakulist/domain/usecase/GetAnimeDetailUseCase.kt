package com.example.seijakulist.domain.usecase

import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.AnimeDetail
import javax.inject.Inject

class GetAnimeDetailUseCase @Inject constructor(

    private val repository: AnimeRepository

) {
    suspend operator fun invoke(animeId: Int) : AnimeDetail {
        return repository.getAnimeDetailsById(animeId)
    }
}