package com.yumedev.seijakulist.ui.screens.local_anime_detail

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.yumedev.seijakulist.data.mapper.local.toAnimeEntity
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
import com.yumedev.seijakulist.data.repository.FirestoreAnimeRepository
import com.yumedev.seijakulist.domain.models.AnimeEntityDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class LocalAnimeDetailViewModel @Inject constructor(
    private val animeLocalRepository: AnimeLocalRepository,
    private val firestoreAnimeRepository: FirestoreAnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Int? = savedStateHandle["animeId"]

    private val _anime = MutableStateFlow<AnimeEntityDomain?>(null)
    val anime: StateFlow<AnimeEntityDomain?> = _anime.asStateFlow()

    private val _isSharing = MutableStateFlow(false)
    val isSharing: StateFlow<Boolean> = _isSharing.asStateFlow()

    init {
        viewModelScope.launch {
            if (animeId != null) {
                val loadedAnime = animeLocalRepository.getAnimeById(animeId)
                _anime.value = loadedAnime
            }
        }
    }

    fun updateDates(startDate: Long?, endDate: Long?) {
        val currentAnime = _anime.value ?: return
        viewModelScope.launch {
            val updated = currentAnime.copy(startDate = startDate, endDate = endDate)
            val entity = updated.toAnimeEntity()
            animeLocalRepository.updateAnime(entity)
            _anime.value = updated
            firestoreAnimeRepository.syncAnimeToFirestore(entity)
        }
    }

    fun updatePlannedPriorityAndNote(priority: String?, note: String?) {
        val currentAnime = _anime.value ?: return
        viewModelScope.launch {
            val updated = currentAnime.copy(plannedPriority = priority, plannedNote = note)
            val entity = updated.toAnimeEntity()
            animeLocalRepository.updateAnime(entity)
            _anime.value = updated
            firestoreAnimeRepository.syncAnimeToFirestore(entity)
        }
    }

    fun updateAnime(
        status: String,
        score: Float,
        opinion: String?,
        startDate: Long?,
        endDate: Long?,
        plannedPriority: String?,
        plannedNote: String?
    ) {
        val currentAnime = _anime.value ?: return
        viewModelScope.launch {
            val updated = currentAnime.copy(
                userStatus       = status,
                userScore        = score,
                userOpiniun      = opinion ?: "",
                startDate        = startDate,
                endDate          = endDate,
                plannedPriority  = if (status == "Planeado") plannedPriority else null,
                plannedNote      = if (status == "Planeado") plannedNote else null
            )
            val entity = updated.toAnimeEntity()
            animeLocalRepository.updateAnime(entity)
            _anime.value = updated
            firestoreAnimeRepository.syncAnimeToFirestore(entity)
        }
    }

    fun shareAnime(context: Context) {
        val currentAnime = _anime.value ?: return

        viewModelScope.launch {
            _isSharing.value = true
            try {
                withContext(Dispatchers.IO) {
                    // Descargar la imagen del anime
                    val loader = ImageLoader(context)
                    val request = ImageRequest.Builder(context)
                        .data(currentAnime.image)
                        .allowHardware(false)
                        .build()

                    val result = (loader.execute(request) as? SuccessResult)?.drawable
                    val animeBitmap = (result as? BitmapDrawable)?.bitmap

                    // Crear imagen compuesta
                    val composedBitmap = animeBitmap?.let { createStoryCard(it, currentAnime) }

                    // Guardar imagen en cache con máxima calidad
                    val imagesDir = File(context.cacheDir, "images")
                    imagesDir.mkdirs()
                    val imageFile = File(imagesDir, "shared_anime_${System.currentTimeMillis()}.png")

                    composedBitmap?.let {
                        FileOutputStream(imageFile).use { out ->
                            it.compress(Bitmap.CompressFormat.PNG, 100, out)
                        }
                    }

                    // Crear intent
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        imageFile
                    )

                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "image/png"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }

                    withContext(Dispatchers.Main) {
                        context.startActivity(
                            Intent.createChooser(shareIntent, "Compartir anime")
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isSharing.value = false
            }
        }
    }

    private fun createStoryCard(animeBitmap: Bitmap, anime: AnimeEntityDomain): Bitmap {

        val width = 1600
        val height = 3200

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        canvas.drawFilter = android.graphics.PaintFlagsDrawFilter(
            0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )

        val accentBlue = Color.parseColor("#58A6FF")

        // ---------------------------------------------------
        // 1️⃣ FONDO CINEMÁTICO
        // ---------------------------------------------------

        val bgScaled = Bitmap.createScaledBitmap(animeBitmap, width, height, true)
        canvas.drawBitmap(bgScaled, 0f, 0f, null)

        val gradient = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            intArrayOf(
                Color.parseColor("#88000000"),
                Color.parseColor("#E0000000"),
                Color.parseColor("#F5000000")
            ),
            floatArrayOf(0f, 0.38f, 1f),
            Shader.TileMode.CLAMP
        )
        val overlayPaint = Paint().apply { shader = gradient }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)

        // Barra de acento superior
        val accentPaint = Paint().apply { color = accentBlue; isAntiAlias = true }
        canvas.drawRect(0f, 0f, width.toFloat(), 10f, accentPaint)

        // ---------------------------------------------------
        // 2️⃣ POSTER FLOTANTE
        // ---------------------------------------------------

        var currentY = 110f
        val imageWidth = 780
        val imageHeight = 1060
        val imageX = (width - imageWidth) / 2f

        // Sombra sutil detrás del poster
        val shadowPaint = Paint().apply {
            color = Color.parseColor("#55000000")
            isAntiAlias = true
        }
        canvas.drawRoundRect(
            RectF(imageX + 12f, currentY + 18f, imageX + imageWidth + 12f, currentY + imageHeight + 18f),
            50f, 50f, shadowPaint
        )

        val croppedBitmap = getCenterCroppedBitmap(animeBitmap, imageWidth, imageHeight)
        val roundedBitmap = getRoundedBitmap(croppedBitmap, 50f)
        canvas.drawBitmap(roundedBitmap, imageX, currentY, null)

        val borderPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 3f
            color = Color.parseColor("#33FFFFFF")
            isAntiAlias = true
        }
        canvas.drawRoundRect(
            RectF(imageX, currentY, imageX + imageWidth, currentY + imageHeight),
            50f, 50f, borderPaint
        )

        currentY += imageHeight + 72f

        // ---------------------------------------------------
        // 3️⃣ TÍTULO DEL ANIME
        // ---------------------------------------------------

        val titlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 88f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        currentY = drawMultilineTextCentered(
            canvas, anime.title, width / 2f,
            currentY + titlePaint.textSize,
            titlePaint, width * 0.84f, maxLines = 2
        ) + 18f

        anime.titleJapanese?.takeIf { it.isNotBlank() }?.let { jpTitle ->
            val jpPaint = Paint().apply {
                color = Color.parseColor("#70FFFFFF")
                textSize = 50f
                typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            val display = if (jpTitle.length > 40) jpTitle.take(39) + "…" else jpTitle
            canvas.drawText(display, width / 2f, currentY + 50f, jpPaint)
            currentY += 76f
        }

        currentY += 48f

        // ---------------------------------------------------
        // 4️⃣ PUNTUACIÓN DEL USUARIO
        // ---------------------------------------------------

        val scoreText = if (anime.userScore > 0f) String.format("%.1f", anime.userScore) else "—"

        val scorePaint = Paint().apply {
            color = accentBlue
            textSize = 152f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(scoreText, width / 2f, currentY + 152f, scorePaint)

        val scoreLabelPaint = Paint().apply {
            color = Color.parseColor("#60FFFFFF")
            textSize = 44f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("MI PUNTUACIÓN", width / 2f, currentY + 212f, scoreLabelPaint)
        currentY += 258f

        anime.score?.takeIf { it > 0f }?.let { malScore ->
            val malPaint = Paint().apply {
                color = Color.parseColor("#48FFFFFF")
                textSize = 40f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("Puntuación MAL: ${String.format("%.2f", malScore)}", width / 2f, currentY + 40f, malPaint)
            currentY += 60f
        }

        currentY += 52f

        // ---------------------------------------------------
        // 5️⃣ DIVISOR
        // ---------------------------------------------------

        val dividerPaint = Paint().apply {
            color = Color.parseColor("#30FFFFFF")
            strokeWidth = 2f
            isAntiAlias = true
        }
        val divMargin = width * 0.10f
        canvas.drawLine(divMargin, currentY, width - divMargin, currentY, dividerPaint)
        currentY += 55f

        // ---------------------------------------------------
        // 6️⃣ STATS EN GRID 2 COLUMNAS
        // ---------------------------------------------------

        val cellGap = 28f
        val cellW = (width * 0.84f - cellGap) / 2f
        val cellH = 185f
        val gridLeft = (width - (cellW * 2 + cellGap)) / 2f

        val cellBgPaint = Paint().apply {
            color = Color.parseColor("#18FFFFFF")
            isAntiAlias = true
        }
        val statLabelPaint = Paint().apply {
            color = accentBlue
            textSize = 38f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        val statValuePaint = Paint().apply {
            color = Color.WHITE
            textSize = 60f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }

        fun drawStatCell(label: String, value: String, x: Float, y: Float) {
            canvas.drawRoundRect(RectF(x, y, x + cellW, y + cellH), 28f, 28f, cellBgPaint)
            canvas.drawText(label, x + 32f, y + 52f, statLabelPaint)
            val maxValWidth = cellW - 60f
            var dv = value
            while (statValuePaint.measureText(dv) > maxValWidth && dv.length > 1) {
                dv = dv.dropLast(1)
            }
            if (dv.length < value.length) dv += "…"
            canvas.drawText(dv, x + 32f, y + 145f, statValuePaint)
        }

        // Fila 1: Episodios vistos | Estado
        drawStatCell(
            "Episodios",
            "${anime.episodesWatched} / ${anime.totalEpisodes}",
            gridLeft, currentY
        )
        drawStatCell("Estado", anime.userStatus, gridLeft + cellW + cellGap, currentY)
        currentY += cellH + cellGap

        // Fila 2: Tipo | Temporada + año
        val seasonVal = listOfNotNull(
            anime.season?.replaceFirstChar { it.uppercaseChar() },
            anime.year
        ).joinToString(" ").ifEmpty { "—" }
        drawStatCell("Tipo", anime.typeAnime ?: "—", gridLeft, currentY)
        drawStatCell("Temporada", seasonVal, gridLeft + cellW + cellGap, currentY)
        currentY += cellH + cellGap

        // Fila 3: Rewatch | Rank MAL
        val rewatchVal = when (anime.rewatchCount) {
            0 -> "Primera vez"
            1 -> "1 vez"
            else -> "${anime.rewatchCount} veces"
        }
        drawStatCell("Rewatch", rewatchVal, gridLeft, currentY)
        drawStatCell("Rank MAL", anime.rank?.let { "#$it" } ?: "—", gridLeft + cellW + cellGap, currentY)
        currentY += cellH + 55f

        // ---------------------------------------------------
        // 7️⃣ GÉNEROS
        // ---------------------------------------------------

        if (anime.genres.isNotBlank()) {
            canvas.drawLine(divMargin, currentY, width - divMargin, currentY, dividerPaint)
            currentY += 50f

            val genresPaint = Paint().apply {
                color = Color.parseColor("#65FFFFFF")
                textSize = 44f
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            var displayGenres = anime.genres
            while (genresPaint.measureText(displayGenres) > width * 0.84f && displayGenres.contains(",")) {
                displayGenres = displayGenres.substringBeforeLast(",").trim()
            }
            if (displayGenres.length < anime.genres.length) displayGenres += "…"
            canvas.drawText(displayGenres, width / 2f, currentY + 44f, genresPaint)
            currentY += 100f
        }

        // ---------------------------------------------------
        // 8️⃣ FOOTER BRANDING
        // ---------------------------------------------------

        val footerY = height - 175f

        canvas.drawRect(0f, footerY - 58f, width.toFloat(), footerY - 56f, accentPaint)

        val brandPaint = Paint().apply {
            color = Color.parseColor("#99FFFFFF")
            textSize = 52f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            letterSpacing = 0.25f
        }
        canvas.drawText("SEIJAKU LIST", width / 2f, footerY, brandPaint)

        val tagPaint = Paint().apply {
            color = accentBlue
            textSize = 36f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            letterSpacing = 0.08f
        }
        canvas.drawText("Tu lista de anime personal", width / 2f, footerY + 56f, tagPaint)

        return bitmap
    }

    private fun drawWrappedStat(canvas: Canvas, label: String, value: String, x: Float, y: Float) {
        // Label en celeste de SeijakuList
        val labelPaint = Paint().apply {
            color = Color.parseColor("#58A6FF") // Celeste Primary
            textSize = 36f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText(label, x, y, labelPaint)

        // Valor en blanco grande
        val valuePaint = Paint().apply {
            color = Color.WHITE
            textSize = 64f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText(value, x, y + 80f, valuePaint)
    }

    private fun drawMultilineTextCentered(
        canvas: Canvas,
        text: String,
        centerX: Float,
        y: Float,
        paint: Paint,
        maxWidth: Float,
        maxLines: Int = Int.MAX_VALUE
    ): Float {
        val words = text.split(" ")
        var line = ""
        var currentY = y
        var lineCount = 0

        for (word in words) {
            if (lineCount >= maxLines) break

            val testLine = if (line.isEmpty()) word else "$line $word"
            val testWidth = paint.measureText(testLine)

            if (testWidth > maxWidth && line.isNotEmpty()) {
                canvas.drawText(line, centerX, currentY, paint)
                line = word
                currentY += paint.textSize * 1.2f
                lineCount++
            } else {
                line = testLine
            }
        }

        if (line.isNotEmpty() && lineCount < maxLines) {
            canvas.drawText(line, centerX, currentY, paint)
            currentY += paint.textSize
        }

        return currentY
    }

    private fun getCenterCroppedBitmap(source: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val sourceWidth = source.width
        val sourceHeight = source.height

        // Calcular el ratio de escalado para cubrir completamente el área objetivo
        val scale = maxOf(
            targetWidth.toFloat() / sourceWidth,
            targetHeight.toFloat() / sourceHeight
        )

        // Dimensiones escaladas
        val scaledWidth = (sourceWidth * scale).toInt()
        val scaledHeight = (sourceHeight * scale).toInt()

        // Escalar la imagen
        val scaledBitmap = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true)

        // Calcular offset para crop center
        val xOffset = (scaledWidth - targetWidth) / 2
        val yOffset = (scaledHeight - targetHeight) / 2

        // Crear el bitmap final con crop center
        return Bitmap.createBitmap(scaledBitmap, xOffset, yOffset, targetWidth, targetHeight)
    }

    private fun getRoundedBitmap(bitmap: Bitmap, cornerRadius: Float): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
        }

        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)

        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }
}
