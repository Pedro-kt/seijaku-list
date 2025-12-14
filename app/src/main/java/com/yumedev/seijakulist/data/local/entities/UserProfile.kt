package com.yumedev.seijakulist.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val uid: String,
    val username: String?,
    val profilePictureUrl: String?,
    val bio: String? = null,
    val top5AnimeIds: String? = null // IDs separados por comas: "1,2,3,4,5"
)
