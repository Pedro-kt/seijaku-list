package com.example.seijakulist.data.repository

import com.example.seijakulist.data.local.dao.UserProfileDao
import com.example.seijakulist.data.local.entities.UserProfile
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

}