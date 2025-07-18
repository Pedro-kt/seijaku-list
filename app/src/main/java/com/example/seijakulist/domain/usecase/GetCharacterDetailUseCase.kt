package com.example.seijakulist.domain.usecase

import android.util.Log
import com.example.seijakulist.data.repository.AnimeRepository
import com.example.seijakulist.domain.models.AnimeCharactersDetail
import com.example.seijakulist.domain.models.CharacterDetail
import javax.inject.Inject

class GetCharacterDetailUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    suspend operator fun invoke(characterId: Int): CharacterDetail {
        Log.d("GetCharacterDetailUseCase", "Llamando a la API para ID: $characterId")
        return animeRepository.getCharacterDetailById(characterId)
    }
}