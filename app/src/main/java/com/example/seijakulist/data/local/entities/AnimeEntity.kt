package com.example.seijakulist.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animes")
data class AnimeEntity(
    @PrimaryKey val malId: Int,
    val title: String,
    val imageUrl: String?,
    val score: Float
)