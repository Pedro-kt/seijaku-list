package com.yumedev.seijakulist.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.yumedev.seijakulist.data.local.entities.MangaEntity
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

fun FirestoreMangaData.toMangaEntity() = MangaEntity(
    malId = malId,
    title = title,
    titleEnglish = null,
    titleJapanese = null,
    imageUrl = imageUrl,
    bannerImage = null,
    typeManga = typeManga,
    source = null,
    chapters = chapters,
    volumes = volumes,
    status = "",
    published = null,
    score = null,
    scoreBy = null,
    rank = null,
    synopsis = null,
    background = null,
    genres = genres,
    demographics = null,
    authors = null,
    serializations = null,
    userScore = userScore,
    statusUser = statusUser,
    userOpinion = userOpinion,
    chaptersRead = chaptersRead,
    volumesRead = volumesRead,
    rereadCount = rereadCount,
    startDate = startDate,
    endDate = endDate,
    plannedPriority = plannedPriority,
    plannedNote = plannedNote,
    lastModified = lastModified
)

data class FirestoreMangaData(
    val malId: Int = 0,
    val title: String = "",
    val imageUrl: String? = null,
    val typeManga: String = "",
    val statusUser: String = "",
    val chaptersRead: Int = 0,
    val volumesRead: Int = 0,
    val chapters: Int? = null,
    val volumes: Int? = null,
    val rereadCount: Int = 0,
    val userScore: Float = 0f,
    val userOpinion: String = "",
    val startDate: Long? = null,
    val endDate: Long? = null,
    val genres: String = "",
    val plannedPriority: String? = null,
    val plannedNote: String? = null,
    val lastModified: Long = 0L
)

class FirestoreMangaRepository @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun mangasCollection(uid: String) =
        db.collection("users").document(uid).collection("mangas")

    private fun currentUid(): String? = auth.currentUser?.uid

    suspend fun syncMangaToFirestore(manga: MangaEntity) {
        val uid = currentUid() ?: return
        val data = mapOf(
            "malId" to manga.malId,
            "title" to manga.title,
            "imageUrl" to manga.imageUrl,
            "typeManga" to manga.typeManga,
            "statusUser" to manga.statusUser,
            "chaptersRead" to manga.chaptersRead,
            "volumesRead" to manga.volumesRead,
            "chapters" to manga.chapters,
            "volumes" to manga.volumes,
            "rereadCount" to manga.rereadCount,
            "userScore" to manga.userScore,
            "userOpinion" to manga.userOpinion,
            "startDate" to manga.startDate,
            "endDate" to manga.endDate,
            "genres" to manga.genres,
            "plannedPriority" to manga.plannedPriority,
            "plannedNote" to manga.plannedNote,
            "lastModified" to manga.lastModified
        )
        mangasCollection(uid)
            .document(manga.malId.toString())
            .set(data, SetOptions.merge())
            .await()
    }

    suspend fun deleteMangaFromFirestore(malId: Int) {
        val uid = currentUid() ?: return
        mangasCollection(uid)
            .document(malId.toString())
            .delete()
            .await()
    }

    suspend fun fetchAllMangasFromFirestore(): List<FirestoreMangaData> {
        val uid = currentUid() ?: return emptyList()
        val snapshot = mangasCollection(uid).get().await()
        return snapshot.documents.mapNotNull { doc ->
            val malId = (doc.getLong("malId") ?: return@mapNotNull null).toInt()
            FirestoreMangaData(
                malId = malId,
                title = doc.getString("title") ?: "",
                imageUrl = doc.getString("imageUrl"),
                typeManga = doc.getString("typeManga") ?: "",
                statusUser = doc.getString("statusUser") ?: "",
                chaptersRead = (doc.getLong("chaptersRead") ?: 0L).toInt(),
                volumesRead = (doc.getLong("volumesRead") ?: 0L).toInt(),
                chapters = doc.getLong("chapters")?.toInt(),
                volumes = doc.getLong("volumes")?.toInt(),
                rereadCount = (doc.getLong("rereadCount") ?: 0L).toInt(),
                userScore = (doc.getDouble("userScore") ?: 0.0).toFloat(),
                userOpinion = doc.getString("userOpinion") ?: "",
                startDate = doc.getLong("startDate"),
                endDate = doc.getLong("endDate"),
                genres = doc.getString("genres") ?: "",
                plannedPriority = doc.getString("plannedPriority"),
                plannedNote = doc.getString("plannedNote"),
                lastModified = doc.getLong("lastModified") ?: 0L
            )
        }
    }
}
