plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.devtools.ksp") version "2.1.20-2.0.1" apply false
    alias(libs.plugins.compose.compiler) apply false

}