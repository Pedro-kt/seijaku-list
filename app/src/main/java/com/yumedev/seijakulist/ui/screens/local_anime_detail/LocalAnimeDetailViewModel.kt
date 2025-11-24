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
import com.yumedev.seijakulist.data.repository.AnimeLocalRepository
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

@HiltViewModel
class LocalAnimeDetailViewModel @Inject constructor(
    private val animeLocalRepository: AnimeLocalRepository,
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
        // Dimensiones en ultra alta calidad
        val width = 1440
        val height = 3200

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Habilitar anti-aliasing y filtrado de alta calidad
        canvas.drawFilter = android.graphics.PaintFlagsDrawFilter(
            0,
            Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )

        // Extraer color dominante de la imagen
        val dominantColor = extractDominantColor(animeBitmap)
        val darkerColor = darkenColor(dominantColor, 0.3f)
        val darkestColor = darkenColor(dominantColor, 0.6f)

        // Fondo con gradiente basado en el color de la portada
        val backgroundPaint = Paint().apply {
            shader = LinearGradient(
                0f, 0f, 0f, height.toFloat(),
                intArrayOf(
                    darkestColor,
                    darkerColor,
                    darkestColor
                ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
            isAntiAlias = true
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        // Patrón japonés sutil en el fondo (círculos seigaiha - olas)
        drawJapanesePattern(canvas, width, height)

        val margin = 110f
        var currentY = 160f

        // Header centrado
        val yearPaint = Paint().apply {
            color = Color.parseColor("#E63946")
            textSize = 54f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            letterSpacing = 0.15f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("MY ANIME 2025", width / 2f, currentY, yearPaint)

        currentY += 120f

        // Título del anime - centrado
        val titlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 86f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            letterSpacing = -0.02f
            textAlign = Paint.Align.CENTER
        }
        currentY = drawMultilineTextCentered(canvas, anime.title, width / 2f, currentY, titlePaint, width - margin * 2, maxLines = 3)

        currentY += 95f

        // Imagen del anime rectangular (centrada) - proporción 2:3 típica de portadas - AUMENTADA
        val imageWidth = 800
        val imageHeight = 1200
        val imageLeft = (width - imageWidth) / 2f

        // Sombra de la imagen
        val shadowPaint = Paint().apply {
            color = Color.parseColor("#60000000")
            maskFilter = android.graphics.BlurMaskFilter(50f, android.graphics.BlurMaskFilter.Blur.NORMAL)
            isAntiAlias = true
        }
        val shadowRect = RectF(imageLeft - 7f, currentY + 20f, imageLeft + imageWidth + 7f, currentY + imageHeight + 20f)
        canvas.drawRoundRect(shadowRect, 32f, 32f, shadowPaint)

        // Escalar imagen manteniendo aspecto ratio con alta calidad
        val scaledBitmap = Bitmap.createScaledBitmap(animeBitmap, imageWidth, imageHeight, true)
        val roundedBitmap = getRoundedBitmap(scaledBitmap, 32f)
        canvas.drawBitmap(roundedBitmap, imageLeft, currentY, null)

        currentY += imageHeight + 80f

        // Stats centrados
        val scoreText = if (anime.userScore % 1.0 == 0.0) {
            anime.userScore.toInt().toString()
        } else {
            anime.userScore.toString()
        }

        // Puntuación
        currentY = drawWrappedStatCentered(canvas, "Tu puntuación", "$scoreText/10", width / 2f, currentY)
        currentY += 95f

        // Veces visto
        currentY = drawWrappedStatCentered(canvas, "Veces visto", "${anime.rewatchCount}", width / 2f, currentY)
        currentY += 95f

        // Episodios
        val episodesText = "${anime.totalEpisodes}"
        currentY = drawWrappedStatCentered(canvas, "Cantidad de episodios", episodesText, width / 2f, currentY)
        currentY += 95f

        // Estado
        currentY = drawWrappedStatCentered(canvas, "Estado actual", anime.userStatus, width / 2f, currentY)

        // Reseña (si existe)
        if (!anime.userOpiniun.isNullOrEmpty()) {
            currentY += 120f

            val reviewLabelPaint = Paint().apply {
                color = Color.parseColor("#999999")
                textSize = 43f
                isAntiAlias = true
                letterSpacing = 0.05f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText("Tu reseña", width / 2f, currentY, reviewLabelPaint)

            currentY += 67f
            val reviewTextPaint = Paint().apply {
                color = Color.parseColor("#DDDDDD")
                textSize = 48f
                isAntiAlias = true
                typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                textAlign = Paint.Align.CENTER
            }

            val reviewLines = anime.userOpiniun.take(300) + if (anime.userOpiniun.length > 300) "..." else ""
            currentY = drawMultilineTextCentered(canvas, "\" $reviewLines\"", width / 2f, currentY, reviewTextPaint, width - margin * 2, maxLines = 5)
        }

        // Footer centrado - marca
        val footerY = height - 120f
        val footerPaint = Paint().apply {
            color = Color.parseColor("#666666")
            textSize = 37f
            isAntiAlias = true
            letterSpacing = 0.2f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("SEIJAKULIST", width / 2f, footerY, footerPaint)

        return bitmap
    }

    private fun drawJapanesePattern(canvas: Canvas, width: Int, height: Int) {
        val paint = Paint().apply {
            isAntiAlias = true
        }

        // Sol/Luna en la esquina superior derecha
        paint.apply {
            color = Color.parseColor("#20FFFFFF")
            style = Paint.Style.FILL
        }
        canvas.drawCircle(width - 180f, 200f, 120f, paint)

        // Bambú izquierdo
        drawBamboo(canvas, 80f, height - 700f, paint)
        drawBamboo(canvas, 130f, height - 800f, paint)

        // Bambú derecho
        drawBamboo(canvas, width - 80f, height - 700f, paint)
        drawBamboo(canvas, width - 130f, height - 800f, paint)

        // Torii grande en la parte inferior
        drawToriiLarge(canvas, width / 2f, height - 550f, paint)

        // Nubes estilo japonés
        drawJapaneseClouds(canvas, 150f, 300f, paint)
        drawJapaneseClouds(canvas, width - 200f, 400f, paint)
    }

    private fun drawBamboo(canvas: Canvas, x: Float, startY: Float, paint: Paint) {
        paint.apply {
            color = Color.parseColor("#15FFFFFF")
            style = Paint.Style.FILL
            strokeWidth = 3f
        }

        val segments = 5
        val segmentHeight = 80f
        val bambooWidth = 20f

        for (i in 0 until segments) {
            val y = startY + i * segmentHeight

            // Segmento de bambú
            paint.style = Paint.Style.FILL
            canvas.drawRoundRect(
                x - bambooWidth / 2,
                y,
                x + bambooWidth / 2,
                y + segmentHeight - 5f,
                bambooWidth / 2,
                bambooWidth / 2,
                paint
            )

            // Nodo del bambú
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 2f
            canvas.drawLine(x - bambooWidth / 2, y, x + bambooWidth / 2, y, paint)
        }
    }

    private fun drawToriiLarge(canvas: Canvas, centerX: Float, y: Float, paint: Paint) {
        paint.apply {
            color = Color.parseColor("#15E63946")
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        val width = 600f
        val height = 350f
        val pillarWidth = 35f

        // Travesaño superior curvado
        canvas.drawRoundRect(
            centerX - width / 2 - 40f,
            y,
            centerX + width / 2 + 40f,
            y + 35f,
            15f,
            15f,
            paint
        )

        // Travesaño medio
        canvas.drawRoundRect(
            centerX - width / 2 + 20f,
            y + 100f,
            centerX + width / 2 - 20f,
            y + 125f,
            12f,
            12f,
            paint
        )

        // Pilar izquierdo
        canvas.drawRoundRect(
            centerX - width / 2 + 40f,
            y + 30f,
            centerX - width / 2 + 40f + pillarWidth,
            y + height,
            12f,
            12f,
            paint
        )

        // Pilar derecho
        canvas.drawRoundRect(
            centerX + width / 2 - 40f - pillarWidth,
            y + 30f,
            centerX + width / 2 - 40f,
            y + height,
            12f,
            12f,
            paint
        )
    }

    private fun drawJapaneseClouds(canvas: Canvas, x: Float, y: Float, paint: Paint) {
        paint.apply {
            color = Color.parseColor("#10FFFFFF")
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // Nube estilo japonés (círculos superpuestos)
        canvas.drawCircle(x, y, 40f, paint)
        canvas.drawCircle(x + 35f, y + 10f, 35f, paint)
        canvas.drawCircle(x + 70f, y, 40f, paint)
        canvas.drawCircle(x + 35f, y - 10f, 30f, paint)
    }

    private fun drawWrappedStatCentered(canvas: Canvas, label: String, value: String, x: Float, y: Float): Float {
        // Label pequeño arriba (centrado)
        val labelPaint = Paint().apply {
            color = Color.parseColor("#999999")
            textSize = 32f
            isAntiAlias = true
            letterSpacing = 0.05f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(label, x, y, labelPaint)

        // Valor grande abajo (centrado)
        val valuePaint = Paint().apply {
            color = Color.WHITE
            textSize = 56f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            isAntiAlias = true
            letterSpacing = -0.01f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(value, x, y + 65f, valuePaint)

        return y + 65f
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
                currentY += paint.textSize + 12
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

    private fun extractDominantColor(bitmap: Bitmap): Int {
        // Reducir la imagen para mejorar el rendimiento
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true)

        var redSum = 0L
        var greenSum = 0L
        var blueSum = 0L
        var pixelCount = 0

        // Calcular el color promedio
        for (x in 0 until scaledBitmap.width) {
            for (y in 0 until scaledBitmap.height) {
                val pixel = scaledBitmap.getPixel(x, y)

                // Ignorar píxeles muy oscuros o muy claros
                val brightness = (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
                if (brightness in 30..220) {
                    redSum += Color.red(pixel)
                    greenSum += Color.green(pixel)
                    blueSum += Color.blue(pixel)
                    pixelCount++
                }
            }
        }

        return if (pixelCount > 0) {
            Color.rgb(
                (redSum / pixelCount).toInt(),
                (greenSum / pixelCount).toInt(),
                (blueSum / pixelCount).toInt()
            )
        } else {
            Color.parseColor("#1a1a2e") // Color por defecto
        }
    }

    private fun darkenColor(color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)

        // Reducir el brillo y saturación para hacerlo más oscuro
        hsv[1] = (hsv[1] * (1 - factor * 0.3f)).coerceIn(0f, 1f) // Saturación
        hsv[2] = (hsv[2] * (1 - factor)).coerceIn(0f, 1f) // Brillo

        return Color.HSVToColor(hsv)
    }
}
