package com.example.seijakulist.domain.usecase

import android.util.Log
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.AnimeThemes
import com.example.seijakulist.domain.models.CharacterDetail
import javax.inject.Inject

class GetAnimeThemesUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {

    suspend operator fun invoke(malId: Int): AnimeThemes {
        return animeRepository.getAnimeThemesById(malId)
    }

}