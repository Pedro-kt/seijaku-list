package com.example.seijakulist.data.remote.models.character_pictures

import com.google.gson.annotations.SerializedName

data class CharacterPicturesDto(
    @SerializedName("jpg") val characterPictures: CharacterPicturesUrl?
)

data class CharacterPicturesUrl(
    @SerializedName("image_url") val imageUrlCharacterPicture: String?
)
