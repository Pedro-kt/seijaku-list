package com.yumedev.seijakulist.domain.usecase

import android.util.Log
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.AnimeThemes
import com.yumedev.seijakulist.domain.models.CharacterDetail
import javax.inject.Inject

class GetAnimeThemesUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {

    suspend operator fun invoke(malId: Int): AnimeThemes {
        return animeRepository.getAnimeThemesById(malId)
    }

}