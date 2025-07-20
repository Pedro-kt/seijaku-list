plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsKotlinSerialization)

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
    //composeOptions { // Este bloque es crucial y DEBE IR AQUÍ
    //    kotlinCompilerExtensionVersion = "1.6.0" // Verifica la versión compatible con tu Compose BOM
    //}
    hilt {
        enableAggregatingTask = false
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
    //ksp(libs.hilt.compiler)
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Kapt
    kapt(libs.hilt.compiler)

    implementation("com.squareup:javapoet:1.13.0")

    //navigationCompose
    implementation(libs.androidx.navigation.compose)

    //kotlin serialization
    implementation(libs.kotlin.serialization)

    implementation(platform(libs.androidx.compose.bom))

    implementation("androidx.compose.foundation:foundation")
}