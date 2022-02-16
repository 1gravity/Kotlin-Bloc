plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 31
    buildToolsVersion = "32.0.0"

    defaultConfig {
        minSdk = 19
        targetSdk = 31
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