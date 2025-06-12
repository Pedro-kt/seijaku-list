// build.gradle.kts (project level - ROOT)
plugins {
    // Aquí solo se definen los plugins y se dice que no se aplican aún
    // Se aplican en los módulos específicos (como :app)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false // Este puede que no sea necesario aquí
    alias(libs.plugins.ksp) apply false // Este también
    // ... otros plugins globales que tengas
}