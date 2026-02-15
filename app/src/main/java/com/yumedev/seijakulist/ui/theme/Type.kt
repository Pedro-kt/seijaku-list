package com.yumedev.seijakulist.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.yumedev.seijakulist.R

// Poppins Font Family
val PoppinsRegular = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal)
)

val PoppinsMedium = FontFamily(
    Font(R.font.poppins_medium, FontWeight.Medium)
)

val PoppinsBold = FontFamily(
    Font(R.font.poppins_bold, FontWeight.Bold)
)

// Mantener Roboto para compatibilidad (deprecated)
@Deprecated("Use PoppinsRegular instead", ReplaceWith("PoppinsRegular"))
val RobotoRegular = PoppinsRegular

@Deprecated("Use PoppinsBold instead", ReplaceWith("PoppinsBold"))
val RobotoBold = PoppinsBold

val Typography = Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = PoppinsRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = PoppinsBold,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
)