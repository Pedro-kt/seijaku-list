package com.example.seijakulist.util

import android.net.Uri

object YoutubeUtils {
    fun buildSearchUrl(query: String): String {
        val encodedQuery = Uri.encode(query)
        return "https://www.youtube.com/results?search_query=$encodedQuery"
    }
}