package com.yumedev.seijakulist.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.yumedev.seijakulist.data.local.entities.AnimeEntity
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

fun FirestoreAnimeData.toAnimeEntity() = AnimeEntity(
    malId = malId,
    title = title,
    imageUrl = imageUrl,
    userScore = userScore,
    statusUser = statusUser,
    userOpiniun = userOpinion,
    totalEpisodes = totalEpisodes,
    episodesWatched = episodesWatched,
    rewatchCount = rewatchCount,
    genres = genres,
    startDate = startDate,
    endDate = endDate
)

data class FirestoreAnimeData(
    val malId: Int = 0,
    val title: String = "",
    val imageUrl: String? = null,
    val statusUser: String = "",
    val episodesWatched: Int = 0,
    val rewatchCount: Int = 0,
    val userScore: Float = 0f,
    val userOpinion: String = "",
    val startDate: Long? = null,
    val endDate: Long? = null,
    val totalEpisodes: Int = 0,
    val genres: String = ""
)

class FirestoreAnimeRepository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun animesCollection(uid: String) =
        db.collection("users").document(uid).collection("animes")

    private fun currentUid(): String? = auth.currentUser?.uid

    suspend fun syncAnimeToFirestore(anime: AnimeEntity) {
        val uid = currentUid() ?: return
        val data = mapOf(
            "malId" to anime.malId,
            "title" to anime.title,
            "imageUrl" to anime.imageUrl,
            "statusUser" to anime.statusUser,
            "episodesWatched" to anime.episodesWatched,
            "totalEpisodes" to anime.totalEpisodes,
            "rewatchCount" to anime.rewatchCount,
            "userScore" to anime.userScore,
            "userOpinion" to anime.userOpiniun,
            "startDate" to anime.startDate,
            "endDate" to anime.endDate,
            "genres" to anime.genres
        )
        animesCollection(uid)
            .document(anime.malId.toString())
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun deleteAnimeFromFirestore(malId: Int) {
        val uid = currentUid() ?: return
        animesCollection(uid)
            .document(malId.toString())
            .delete()
            .await()
    }

    suspend fun fetchAllAnimesFromFirestore(): List<FirestoreAnimeData> {
        val uid = currentUid() ?: return emptyList()
        val snapshot = animesCollection(uid).get().await()
        return snapshot.documents.mapNotNull { doc ->
            val malId = (doc.getLong("malId") ?: return@mapNotNull null).toInt()
            FirestoreAnimeData(
                malId = malId,
                title = doc.getString("title") ?: "",
                imageUrl = doc.getString("imageUrl"),
                statusUser = doc.getString("statusUser") ?: "",
                episodesWatched = (doc.getLong("episodesWatched") ?: 0L).toInt(),
                totalEpisodes = (doc.getLong("totalEpisodes") ?: 0L).toInt(),
                rewatchCount = (doc.getLong("rewatchCount") ?: 0L).toInt(),
                userScore = (doc.getDouble("userScore") ?: 0.0).toFloat(),
                userOpinion = doc.getString("userOpinion") ?: "",
                startDate = doc.getLong("startDate"),
                endDate = doc.getLong("endDate"),
                genres = doc.getString("genres") ?: ""
            )
        }
    }
}
