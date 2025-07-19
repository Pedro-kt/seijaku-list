package com.example.seijakulist.domain.usecase

import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.Anime
import com.example.seijakulist.domain.models.AnimeDetail
import com.example.seijakulist.domain.models.CharacterPictures
import javax.inject.Inject

class GetCharacterPicturesUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(characterId: Int) : List<CharacterPictures>  {
        return animeRepository.getCharacterPicturesById(characterId)
    }
}