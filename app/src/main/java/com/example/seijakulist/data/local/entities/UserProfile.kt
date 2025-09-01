package com.example.seijakulist.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val uid: String,
    val username: String?,
    val profilePictureUrl: String?
)
