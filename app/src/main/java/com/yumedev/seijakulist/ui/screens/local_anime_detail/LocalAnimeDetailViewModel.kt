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
import kotlin.math.min

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
        // Dimensiones más altas para aprovechar el dispositivo
        val width = 1080
        val height = 2400

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Habilitar anti-aliasing
        canvas.drawFilter = android.graphics.PaintFlagsDrawFilter(
            0,
            Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG
        )

        // Fondo negro base
        canvas.drawColor(Color.parseColor("#0A0A0A"))

        // Dibujar patrón de franjas diagonales (estilo imagen morada)
        drawDiagonalStripes(canvas, width, height)

        val margin = 60f
        var currentY = 80f

        // Imagen del anime - rectangular y más alta
        val imageWidth = 600
        val imageHeight = 800  // Más alta que ancha (ratio 3:4)
        val imageX = (width - imageWidth) / 2f

        // Sombra elegante para la imagen
        val shadowPaint = Paint().apply {
            color = Color.parseColor("#60000000")
            maskFilter = android.graphics.BlurMaskFilter(50f, android.graphics.BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawRoundRect(
            RectF(imageX - 5f, currentY + 10f, imageX + imageWidth + 5f, currentY + imageHeight + 10f),
            30f, 30f, shadowPaint
        )

        // Imagen del anime con crop center y esquinas redondeadas
        val croppedBitmap = getCenterCroppedBitmap(animeBitmap, imageWidth, imageHeight)
        val roundedBitmap = getRoundedBitmap(croppedBitmap, 30f)
        canvas.drawBitmap(roundedBitmap, imageX, currentY, null)

        currentY += imageHeight + 100f

        // Título del anime - centrado y elegante
        val titlePaint = Paint().apply {
            color = Color.WHITE
            textSize = 62f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        currentY = drawMultilineTextCentered(
            canvas, anime.title, width / 2f, currentY, titlePaint,
            width - margin * 2, maxLines = 2
        )

        // Línea decorativa con gradiente de colores SeijakuList
        val linePaint = Paint().apply {
            shader = LinearGradient(
                margin * 2, currentY,
                width - margin * 2, currentY,
                intArrayOf(
                    Color.parseColor("#58A6FF"), // Celeste
                    Color.parseColor("#7EE787"), // Verde
                    Color.parseColor("#79C0FF")  // Celeste claro
                ),
                null,
                Shader.TileMode.CLAMP
            )
            strokeWidth = 4f
            isAntiAlias = true
        }
        canvas.drawLine(margin * 2, currentY, width - margin * 2, currentY, linePaint)

        currentY += 80f

        // Estadísticas en dos columnas (Spotify Wrapped style)
        val col1X = margin + 40f
        val col2X = width / 2f + 60f

        // Columna 1 - Score
        drawWrappedStat(canvas, "Mi puntuación", anime.userScore.toString(), col1X, currentY)

        // Columna 2 - Episodios
        drawWrappedStat(canvas, "Episodios", anime.totalEpisodes.toString(), col2X, currentY)

        currentY += 200f

        // Fila 2
        // Columna 1 - Estado
        drawWrappedStat(canvas, "Estado", anime.userStatus, col1X, currentY)

        // Columna 2 - Veces visto
        drawWrappedStat(canvas, "Visto", if (anime.rewatchCount == 1) {anime.rewatchCount.toString() + " vez"} else {anime.rewatchCount.toString() + " veces"}, col2X, currentY)

        currentY += 230f

        // Fila 3 - Género y Tipo
        val genres = anime.genres.split(",").map { it.trim() }
        val mainGenre = genres.firstOrNull() ?: "Anime"
        drawWrappedStat(canvas, "Genero", mainGenre, col1X, currentY)

        anime.typeAnime?.let { type ->
            drawWrappedStat(canvas, "Tipo", type, col2X, currentY)
        }

        currentY += 230f

        // Fila 4 - Fechas de seguimiento (si existen)
        if (anime.startDate != null || anime.endDate != null) {
            val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())

            anime.startDate?.let { date ->
                val formattedDate = dateFormat.format(date)
                drawWrappedStat(canvas, "Inicio", formattedDate, col1X, currentY)
            }

            anime.endDate?.let { date ->
                val formattedDate = dateFormat.format(date)
                drawWrappedStat(canvas, "Final", formattedDate, col2X, currentY)
            }

            currentY += 230f
        }

        anime.aired?.let { transmision ->
            // Acortar la transmisión si es muy larga
            val shortAired = if (transmision.length > 20) {
                transmision.take(17) + "..."
            } else {
                transmision
            }
            drawWrappedStat(canvas, "Transmitido", shortAired, col2X, currentY)
            currentY += 230f
        }

        // Footer - Branding con colores SeijakuList
        val footerY = height - 110f
        val footerPaint = Paint().apply {
            shader = LinearGradient(
                width / 2f - 200f, footerY,
                width / 2f + 200f, footerY,
                intArrayOf(
                    Color.parseColor("#58A6FF"), // Celeste
                    Color.parseColor("#7EE787"), // Verde
                    Color.parseColor("#79C0FF")  // Celeste claro
                ),
                null,
                Shader.TileMode.CLAMP
            )
            textSize = 46f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            letterSpacing = 0.15f
        }
        canvas.drawText("SEIJAKULIST", width / 2f, footerY, footerPaint)

        // Año pequeño debajo
        val yearPaint = Paint().apply {
            color = Color.parseColor("#79C0FF") // Celeste claro
            textSize = 28f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            alpha = 180
        }
        canvas.drawText("2026", width / 2f, footerY + 50f, yearPaint)

        return bitmap
    }

    private fun drawDiagonalStripes(canvas: Canvas, width: Int, height: Int) {
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        // Configuración de las franjas
        val stripeWidth = 140f
        val numStripes = ((width + height) / stripeWidth * 2).toInt()

        // Colores de SeijakuList: Celeste, Verde, Celeste fuerte, Blanco
        val stripeColors = listOf(
            Color.parseColor("#58A6FF"), // Celeste (Primary)
            Color.parseColor("#7EE787"), // Verde
            Color.parseColor("#79C0FF"), // Celeste claro (Secondary)
            Color.parseColor("#FFFFFF"), // Blanco
            Color.parseColor("#58A6FF"), // Celeste (Primary)
            Color.parseColor("#79C0FF")  // Celeste claro (Secondary)
        )

        // Dibujar franjas diagonales con colores de SeijakuList
        for (i in 0 until numStripes) {
            paint.color = stripeColors[i % stripeColors.size]
            paint.alpha = (40..70).random() // Opacidad suave para no saturar

            val path = android.graphics.Path()

            // Calcular posición de la franja diagonal
            val startX = (i * stripeWidth) - height.toFloat()

            // Crear franja diagonal (de arriba-izquierda a abajo-derecha)
            path.moveTo(startX, 0f)
            path.lineTo(startX + stripeWidth, 0f)
            path.lineTo(startX + stripeWidth + height, height.toFloat())
            path.lineTo(startX + height, height.toFloat())
            path.close()

            canvas.drawPath(path, paint)
        }
    }

    private fun drawModernShapes(canvas: Canvas, width: Int, height: Int, accentColor: Int) {
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        // Círculos decorativos con blur
        paint.color = Color.parseColor("#30FFFFFF")
        paint.maskFilter = android.graphics.BlurMaskFilter(80f, android.graphics.BlurMaskFilter.Blur.NORMAL)
        canvas.drawCircle(width * 0.15f, height * 0.25f, 150f, paint)
        canvas.drawCircle(width * 0.85f, height * 0.75f, 200f, paint)

        // Líneas diagonales sutiles
        paint.maskFilter = null
        paint.alpha = 30
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f

        for (i in 0..3) {
            val startX = width * (0.1f + i * 0.25f)
            canvas.drawLine(startX, height * 0.1f, startX + 100f, height * 0.3f, paint)
        }
    }

    private fun drawModernStat(
        canvas: Canvas,
        value: String,
        unit: String,
        label: String,
        x: Float,
        y: Float,
        maxWidth: Float,
        accentColor: Int
    ) {
        // Valor principal - ENORME
        val valuePaint = Paint().apply {
            color = Color.WHITE
            textSize = 140f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText(value, x, y, valuePaint)

        // Unidad pequeña al lado
        val unitPaint = Paint().apply {
            color = Color.WHITE
            textSize = 56f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            isAntiAlias = true
            alpha = 180
        }
        val valueWidth = valuePaint.measureText(value)
        canvas.drawText(unit, x + valueWidth + 10f, y, unitPaint)

        // Label debajo
        val labelPaint = Paint().apply {
            color = Color.WHITE
            textSize = 36f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            isAntiAlias = true
            alpha = 150
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText(label, x, y + 55f, labelPaint)
    }

    private fun drawCompactStat(
        canvas: Canvas,
        value: String,
        label: String,
        x: Float,
        y: Float,
        maxWidth: Float
    ) {
        // Card de fondo sutil
        val cardPaint = Paint().apply {
            color = Color.parseColor("#20FFFFFF")
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        canvas.drawRoundRect(
            RectF(x - 15f, y - 20f, x + maxWidth - 5f, y + 100f),
            16f, 16f, cardPaint
        )

        // Valor
        val valuePaint = Paint().apply {
            color = Color.WHITE
            textSize = 48f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText(value, x, y + 20f, valuePaint)

        // Label
        val labelPaint = Paint().apply {
            color = Color.WHITE
            textSize = 28f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            isAntiAlias = true
            alpha = 150
            textAlign = Paint.Align.LEFT
        }
        canvas.drawText(label, x, y + 65f, labelPaint)
    }

    private fun drawWrappedFrame(canvas: Canvas, x: Float, y: Float, size: Float, accentColor: Int) {
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        // Colores de SeijakuList para el marco
        val colors = listOf(
            Color.parseColor("#58A6FF"), // Celeste Primary
            Color.parseColor("#7EE787"), // Verde
            Color.parseColor("#79C0FF"), // Celeste claro
            Color.parseColor("#FFFFFF")  // Blanco
        )

        val centerX = x + size / 2
        val centerY = y + size / 2
        val radius = size / 2 + 60f

        // Dibujar "rayos" de sol alrededor
        val numRays = 24
        for (i in 0 until numRays) {
            val angle = (i * 360f / numRays)
            val colorIndex = i % colors.size

            paint.color = colors[colorIndex]
            paint.alpha = 200

            val path = android.graphics.Path()
            val angleRad = Math.toRadians(angle.toDouble())
            val nextAngleRad = Math.toRadians((angle + 15f).toDouble())

            // Punto interno
            val innerX = centerX + (radius * 0.85f * Math.cos(angleRad)).toFloat()
            val innerY = centerY + (radius * 0.85f * Math.sin(angleRad)).toFloat()

            // Punto externo (punta del rayo)
            val outerX = centerX + (radius * 1.3f * Math.cos(angleRad)).toFloat()
            val outerY = centerY + (radius * 1.3f * Math.sin(angleRad)).toFloat()

            // Punto interno siguiente
            val innerNextX = centerX + (radius * 0.85f * Math.cos(nextAngleRad)).toFloat()
            val innerNextY = centerY + (radius * 0.85f * Math.sin(nextAngleRad)).toFloat()

            path.moveTo(innerX, innerY)
            path.lineTo(outerX, outerY)
            path.lineTo(innerNextX, innerNextY)
            path.close()

            canvas.drawPath(path, paint)
        }

        // Círculo interior con celeste de SeijakuList
        paint.color = Color.parseColor("#58A6FF")
        paint.alpha = 255
        canvas.drawCircle(centerX, centerY, radius * 0.95f, paint)
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

    private fun drawMultilineText(
        canvas: Canvas,
        text: String,
        x: Float,
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
                canvas.drawText(line, x, currentY, paint)
                line = word
                currentY += paint.textSize * 1.2f
                lineCount++
            } else {
                line = testLine
            }
        }

        if (line.isNotEmpty() && lineCount < maxLines) {
            canvas.drawText(line, x, currentY, paint)
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

    private fun extractDominantColor(bitmap: Bitmap): Int {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true)

        var redSum = 0L
        var greenSum = 0L
        var blueSum = 0L
        var pixelCount = 0

        for (x in 0 until scaledBitmap.width) {
            for (y in 0 until scaledBitmap.height) {
                val pixel = scaledBitmap.getPixel(x, y)

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
            Color.parseColor("#58A6FF") // Celeste de SeijakuList como fallback
        }
    }

    private fun enhanceVibrance(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)

        // Aumentar saturación y brillo para hacerlo más vibrante
        hsv[1] = min(hsv[1] * 1.4f, 1f) // Saturación
        hsv[2] = min(hsv[2] * 1.2f, 0.9f) // Brillo

        return Color.HSVToColor(hsv)
    }

    private fun darkenColor(color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)

        hsv[1] = (hsv[1] * (1 - factor * 0.3f)).coerceIn(0f, 1f)
        hsv[2] = (hsv[2] * (1 - factor)).coerceIn(0f, 1f)

        return Color.HSVToColor(hsv)
    }
}
