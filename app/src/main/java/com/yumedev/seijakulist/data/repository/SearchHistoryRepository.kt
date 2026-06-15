package com.yumedev.seijakulist.data.repository

import com.yumedev.seijakulist.data.local.dao.SearchHistoryDao
import com.yumedev.seijakulist.data.local.entities.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchHistoryRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) {

    suspend fun insertSearch(search: SearchHistoryEntity) {
        // Eliminar búsquedas duplicadas del mismo query antes de insertar
        searchHistoryDao.deleteSearchByQuery(search.query)
        // Insertar la nueva búsqueda
        searchHistoryDao.insertSearch(search)
        // Mantener solo las últimas 5 búsquedas
        searchHistoryDao.deleteOldSearches()
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
