plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 19
        targetSdk = 30
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

group = "com.github.genaku.reduce"

dependencies {
    implementation(project(":reduce-core"))
    implementation(Kotlin.stdlib)
    implementation(KotlinX.coroutines.core)
    implementation(AndroidX.lifecycle.runtimeKtx)
}