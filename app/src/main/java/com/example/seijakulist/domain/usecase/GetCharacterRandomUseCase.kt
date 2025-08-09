package com.example.seijakulist.domain.usecase

import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.CharacterDetail
import javax.inject.Inject

class GetCharacterRandomUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(): CharacterDetail {
        return animeRepository.getCharacterRandom()
    }
}