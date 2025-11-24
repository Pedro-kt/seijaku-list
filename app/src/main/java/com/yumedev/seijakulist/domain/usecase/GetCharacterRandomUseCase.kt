package com.yumedev.seijakulist.domain.usecase

import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.CharacterDetail
import javax.inject.Inject

class GetCharacterRandomUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(): CharacterDetail {
        return animeRepository.getCharacterRandom()
    }
}