@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object AndroidBuild {
    const val buildToolsVersion = "32.0.0"
    const val compileSdkVersion = 32
    const val targetSdkVersion = compileSdkVersion
    const val minSdkVersion = 21
}

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android()
}

android {
    compileSdk = AndroidBuild.compileSdkVersion
    buildToolsVersion = AndroidBuild.buildToolsVersion

    defaultConfig {
        minSdk = AndroidBuild.minSdkVersion
        targetSdk = AndroidBuild.targetSdkVersion
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets["main"].run {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        resources.srcDirs(
            "src/androidMain/resources",
            "src/commonMain/resources",
        )
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
//        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
//
//    dependencies {
//        coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
//    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
}
