package com.yumedev.seijakulist.data.repository

import com.yumedev.seijakulist.data.local.dao.SearchHistoryDao
import com.yumedev.seijakulist.data.local.entities.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHistoryRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) {

    suspend fun insertSearch(search: SearchHistoryEntity) {
        searchHistoryDao.insertSearch(search)
        searchHistoryDao.deleteOldSearches() // Mantener solo las últimas 5
    }

    fun getRecentSearches(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getRecentSearches()
    }

    suspend fun deleteAllSearches() {
        searchHistoryDao.deleteAllSearches()
    }

    suspend fun deleteSearchByQuery(query: String) {
        searchHistoryDao.deleteSearchByQuery(query)
    }
}
