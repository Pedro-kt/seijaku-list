// app/build.gradle.kts (MODULE LEVEL)

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) // Si este plugin ya maneja composeOptions, déjalo
    alias(libs.plugins.ksp) // Este es importante para Room
    alias(libs.plugins.dagger.hilt.android)
}

android {
    namespace = "com.example.seijakulist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.seijakulist"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions { // Este bloque es crucial y DEBE IR AQUÍ
        kotlinCompilerExtensionVersion = "1.5.1" // Verifica la versión compatible con tu Compose BOM
    }
}

dependencies {
    // Tus dependencias originales
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // AÑADIR ESTAS DEPENDENCIAS AQUÍ (si no las tenías ya en tu libs.versions.toml)
    // Retrofit y GSON
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler) // KSP para el procesador de anotaciones
    implementation(libs.room.ktx)

    // Coil para imágenes
    implementation(libs.coil.compose)

    // Tus dependencias de testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}