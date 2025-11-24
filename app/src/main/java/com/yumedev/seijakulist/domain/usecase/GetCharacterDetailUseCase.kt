package com.yumedev.seijakulist.domain.usecase

import android.util.Log
import com.yumedev.seijakulist.data.repository.AnimeRepository
import com.yumedev.seijakulist.domain.models.AnimeCharactersDetail
import com.yumedev.seijakulist.domain.models.CharacterDetail
import javax.inject.Inject

class GetCharacterDetailUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(characterId: Int): CharacterDetail {
        Log.d("GetCharacterDetailUseCase", "Llamando a la API para ID: $characterId")
        return animeRepository.getCharacterDetailById(characterId)
    }
}