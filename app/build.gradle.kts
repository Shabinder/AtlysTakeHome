import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

val tmdbApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("tmdbApiKey")
    ?: throw GradleException("Please specify tmdbApiKey in local.properties")

android {
    namespace = "com.shabinder.tmdb"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shabinder.tmdb"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.compose.navigation)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.kotlinx.serialization)

    implementation(libs.coil.core)
    implementation(libs.coil.compose)

    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlinx)

    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)

    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}