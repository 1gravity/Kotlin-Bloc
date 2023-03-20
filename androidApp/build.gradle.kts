@file:Suppress("UnusedPrivateMember", "UnstableApiUsage")

import Bloc_android_base_gradle.AndroidBuild

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    buildToolsVersion = AndroidBuild.buildToolsVersion
    compileSdk = AndroidBuild.compileSdkVersion

    defaultConfig {
        applicationId = "com.onegravity.bloc.sample"
        minSdk = AndroidBuild.minSdkVersion
        targetSdk = AndroidBuild.targetSdkVersion
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
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
        viewBinding = true
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
}

dependencies {
    implementation(project(":bloc-core"))
    implementation(project(":bloc-redux"))
    implementation(project(":bloc-compose"))
//    implementation("com.1gravity:bloc-core:_")
//    implementation("com.1gravity:bloc-redux:_")
//    implementation("com.1gravity:bloc-compose:_")

    implementation(project(":bloc-samples"))

    implementation(Kotlin.stdlib)

    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.coroutines.android)

    implementation(AndroidX.multidex)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.lifecycle.viewModelKtx)
    implementation(AndroidX.fragment.ktx)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.navigation.fragmentKtx)
    implementation(AndroidX.navigation.uiKtx)
    implementation(AndroidX.recyclerView)

    // Integration with activities
    implementation(AndroidX.activity.compose)
    // Integration with ViewModels
    implementation(AndroidX.lifecycle.viewModelCompose)
    // Compose Material Design
    implementation(AndroidX.compose.material)
    // Animations
    implementation(AndroidX.compose.animation)
    // Tooling support (Previews, etc.)
    implementation(AndroidX.compose.ui.tooling)

    implementation(Google.android.material)

    implementation(libs.kotlin.reflect)

    implementation(Koin.android)

    // Logging (https://github.com/touchlab/Kermit)
    implementation(Touchlab.kermit)

    implementation(libs.landscapist.glide)
    implementation(libs.glide)
    implementation(libs.groupie)
    implementation(libs.groupie.viewbinding)

    // Kotlin Result (https://github.com/michaelbull/kotlin-result)
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)

    // Essenty (https://github.com/arkivanov/Essenty)
    implementation(libs.lifecycle)

    testImplementation(Testing.kotest.runner.junit5)
    testImplementation(KotlinX.coroutines.test)
    testImplementation(CashApp.turbine)
    testImplementation(Testing.junit4)
    testImplementation(Testing.mockK)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf(
            "-Xcontext-receivers",
            "-Xskip-prerelease-check",
        )
    }
}
