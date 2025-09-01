package com.example.seijakulist.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.seijakulist.data.local.entities.UserProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE uid = :uid LIMIT 1")
    fun getUserProfile(uid: String): Flow<UserProfile?>

    @Update
    suspend fun update(userProfile: UserProfile)
}