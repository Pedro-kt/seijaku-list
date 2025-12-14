package com.yumedev.seijakulist.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animes")
data class AnimeEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val imageUrl: String?,
    val userScore: Float, // campo nuevo (cambiado de nombre) v2
    val statusUser: String, // campo nuevo v2
    val userOpiniun: String, // campo nuevo v3
    val totalEpisodes: Int, // campo nuevo v5
    val episodesWatched: Int, // campo nuevo v5
    val rewatchCount: Int, // campo nuevo v6
    val genres: String = "", // campo nuevo v7 - géneros separados por comas
)