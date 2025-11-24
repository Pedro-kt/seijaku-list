package com.yumedev.seijakulist.domain.usecase

import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.Anime
import com.yumedev.seijakulist.domain.models.AnimeDetail
import com.yumedev.seijakulist.domain.models.CharacterPictures
import javax.inject.Inject

class GetCharacterPicturesUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(characterId: Int) : List<CharacterPictures>  {
        return animeRepository.getCharacterPicturesById(characterId)
    }
}