object Dependencies {
    private const val androidXCoreVersion = "1.6.0"
    const val androidXCore = "androidx.core:core-ktx:$androidXCoreVersion"

    private const val androidXAppCompatVersion = "1.3.1"
    const val androidXAppCompat = "androidx.appcompat:appcompat:$androidXAppCompatVersion"

    private const val coroutinesVersion = "1.3.9"
    const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"

    private const val materialVersion = "1.5.0-alpha04"
    const val material = "com.google.android.material:material:$materialVersion"

    private const val touchImageViewVersion = "3.1.1"
    const val touchImageView = "com.github.MikeOrtiz:TouchImageView:$touchImageViewVersion"

    private const val heulerVersion = "5.0.0"
    const val heuler = "app.futured.hauler:hauler:$heulerVersion"
    const val heulerDataBinding = "app.futured.hauler:databinding:$heulerVersion"
}

object Plugins {
    const val androidApplication = "com.android.application"
    const val android = "android"
}

object Build {
    private const val androidBuildToolsVersion = "7.1.0-alpha13"
    const val androidBuildTools = "com.android.tools.build:gradle:$androidBuildToolsVersion"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"

    const val hiltAndroid = "com.google.dagger:hilt-android-gradle-plugin:${Hilt.hiltVersion}"

    private const val googleServicesVersion = "4.3.10"
    const val googleServicesPlugin =
        "com.google.gms:google-services:$googleServicesVersion"
}

object Kotlin {
    const val version = "1.5.21"
}

object Application {
    const val id = "com.rsicarelli.homehunt"
    const val minSdk = 21
    const val targetSdk = 31
    const val versionCode = 1
    const val versionName = "1.0"
    const val instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}