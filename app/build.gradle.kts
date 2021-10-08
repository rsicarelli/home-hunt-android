plugins {
    id("com.android.application")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
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
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Compose.composeVersion
    }
}

dependencies {
    implementation(Accompanist.animations)
    implementation(Accompanist.pager)

    implementation(Coil.coil)

    implementation(Compose.activity)
    implementation(Compose.ui)
    implementation(Compose.material)
    implementation(Compose.tooling)
    implementation(Compose.navigation)
    implementation(Compose.hiltNavigation)
    implementation(Compose.constraintLayout)

    androidTestImplementation(ComposeTest.uiTestJunit4)
    debugImplementation(ComposeTest.uiTestManifest)

    implementation(Dependencies.androidXCore)
    implementation(Dependencies.androidXAppCompat)
    implementation(Dependencies.coroutines)
    implementation(Dependencies.touchImageView)
    implementation(Dependencies.heuler)
    implementation(Dependencies.heulerDataBinding)
    implementation(Dependencies.timber)

    implementation(platform(Firebase.bom))
    implementation((Firebase.firestore))
    implementation(Firebase.auth)
    implementation(Firebase.playServicesAuth)

    implementation(Google.material)
    implementation(Google.maps)
    implementation(Google.mapsKtx)

    implementation(Hilt.android)
    kapt(Hilt.compiler)
    androidTestImplementation(HiltTest.hiltAndroidTesting)
    kaptAndroidTest(Hilt.compiler)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}