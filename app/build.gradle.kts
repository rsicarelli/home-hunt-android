import org.gradle.internal.impldep.org.fusesource.jansi.AnsiRenderer.test

plugins {
    id("com.android.application")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    kotlin(Plugins.android)
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = Application.id
        minSdk = Application.minSdk
        targetSdk = Application.targetSdk
        versionCode = Application.versionCode
        versionName = Application.versionName

        testInstrumentationRunner = Application.instrumentationRunner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
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

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }
}

dependencies {
    implementation(Dependencies.androidXCore)
    implementation(Dependencies.androidXAppCompat)
    implementation(Dependencies.coroutines)
    implementation(Dependencies.material)
    implementation(Accompanist.animations)

    implementation(Coil.coil)

    implementation(Compose.activity)
    implementation(Compose.ui)
    implementation(Compose.material)
    implementation(Compose.tooling)
    implementation(Compose.navigation)
    implementation(Compose.hiltNavigation)
    implementation(Compose.materialIconsExtended)

    androidTestImplementation(ComposeTest.uiTestJunit4)
    debugImplementation(ComposeTest.uiTestManifest)

    implementation(platform(Firebase.bom))
    implementation((Firebase.firestore))
    implementation(Firebase.auth)
    implementation(Firebase.playServicesAuth)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    androidTestImplementation(HiltTest.hiltAndroidTesting)
    kaptAndroidTest(Hilt.compiler)
}