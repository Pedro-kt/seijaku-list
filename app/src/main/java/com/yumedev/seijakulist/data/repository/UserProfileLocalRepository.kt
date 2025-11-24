package com.yumedev.seijakulist.data.repository

import com.yumedev.seijakulist.data.local.dao.UserProfileDao
import com.yumedev.seijakulist.data.local.entities.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserProfileLocalRepository @Inject constructor(
    private val userProfileDao: UserProfileDao
) {
    suspend fun insertUserProfile(userProfile: UserProfile) {
        withContext(Dispatchers.IO) {
            userProfileDao.insertUserProfile(userProfile)
        }
    }
    suspend fun updateUserProfile(userProfile: UserProfile) {
        userProfileDao.update(userProfile)
    }
    fun getUserProfile(uid: String) = userProfileDao.getUserProfile(uid)

    suspend fun updateTop5AnimeIds(uid: String, animeIds: List<Int>) {
        withContext(Dispatchers.IO) {
            val idsString = animeIds.joinToString(",")
            userProfileDao.updateTop5AnimeIds(uid, idsString)
        }
    }
}