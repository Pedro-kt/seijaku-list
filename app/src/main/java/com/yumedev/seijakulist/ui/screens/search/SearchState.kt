package com.yumedev.seijakulist.ui.screens.search

import com.yumedev.seijakulist.domain.models.AnimeCard

/**
 * Consolidated state for SearchScreen
 * Replaces 14 individual StateFlows with a single state object
 * Improves performance by reducing recompositions
 */
data class SearchState(
    // Filter inputs
    val searchQuery: String = "",
    val selectedFilter: String? = "Anime",
    val selectedGenreId: String? = null,
    val selectedQuickFilter: String? = null,
    val selectedFormat: String? = null,
    val mediaType: String = "Anime", // Tracks whether we're searching Anime or Manga (persists when filter changes to "Géneros")

    // Search results
    val animeList: List<AnimeCard> = emptyList(),
    val previewResults: List<AnimeCard> = emptyList(),

    // Loading states
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val errorMessage: String? = null,

    // Pagination
    val currentPage: Int = 1,
    val hasMorePages: Boolean = true,

    // Discovery data
    val recentSearches: List<String> = emptyList(),
    val trendingAnimes: List<String> = emptyList()
) {
    /**
     * Check if any filters are active
     */
    val hasActiveFilters: Boolean
        get() = selectedQuickFilter != null ||
                selectedFormat != null ||
                selectedGenreId != null ||
                searchQuery.isNotBlank()

    /**
     * Check if search is ready to execute
     */
    val canPerformSearch: Boolean
        get() = selectedQuickFilter != null ||
                (searchQuery.isNotBlank() && (selectedFilter == "Anime" || selectedFilter == "Manga")) ||
                (selectedFilter == "Géneros" && selectedGenreId != null)
}
