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
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
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

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.androidx.multidex)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.recyclerview)

    // Integration with activities
    implementation(libs.androidx.activity.compose)
    // Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Compose Material Design
    implementation(libs.androidx.compose.material)
    // Animations
    implementation(libs.androidx.compose.animation)
    // Tooling support (Previews, etc.)
    implementation(libs.androidx.compose.ui.tooling)

    implementation(libs.com.google.android.material)

    implementation(libs.kotlin.reflect)

    implementation(libs.koin.android)

    // Logging (https://github.com/touchlab/Kermit)
    implementation(libs.kermit)

    implementation(libs.landscapist.glide)
    implementation(libs.glide)
    implementation(libs.groupie)
    implementation(libs.groupie.viewbinding)

    // Kotlin Result (https://github.com/michaelbull/kotlin-result)
    implementation(libs.kotlin.result)
    implementation(libs.kotlin.result.coroutines)

    // Essenty (https://github.com/arkivanov/Essenty)
    implementation(libs.lifecycle)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.junit4)
    testImplementation(libs.mockk)
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
