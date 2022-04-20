import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlin-parcelize")
    id("org.jetbrains.dokka")

    id("bloc-android-base")
    id("bloc-publish")
}

version = "1.0"

kotlin {
    // todo
    // explicitApi = ExplicitApiMode.Strict

    android()

    jvm()
    js().browser()

    val isMacOsX = DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX
    if (isMacOsX) {
        ios()
        iosSimulatorArm64()
    }

    cocoapods {
        summary = "Reactive state management library for KMM"
        homepage = "https://github.com/1gravity/Kotlin-Bloc"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "blocCore"
            isStatic = false
        }
    }

    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            }
        }

        val commonMain by getting {
            dependencies {
                implementation(KotlinX.coroutines.core)

                // Essenty (https://github.com/arkivanov/Essenty)
                implementation("com.arkivanov.essenty:lifecycle:_")
                implementation("com.arkivanov.essenty:parcelable:_")
                implementation("com.arkivanov.essenty:state-keeper:_")
                implementation("com.arkivanov.essenty:instance-keeper:_")
                implementation("com.arkivanov.essenty:back-pressed:_")

                // Logging (https://github.com/touchlab/Kermit)
                implementation(Touchlab.kermit)

                // Kotlin Result (https://github.com/michaelbull/kotlin-result)
                implementation("com.michael-bull.kotlin-result:kotlin-result:_")
                implementation("com.michael-bull.kotlin-result:kotlin-result-coroutines:_")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(AndroidX.appCompat)
                implementation(AndroidX.activity.compose)
                implementation(AndroidX.fragment)
//                implementation(AndroidX.compose.runtime)
//                implementation(AndroidX.compose.compiler)
//                implementation(AndroidX.compose.foundation)
            }
        }
        val androidTest by getting

        if (isMacOsX) {
            val iosSimulatorArm64Main by getting
            val iosMain by getting {
                dependsOn(commonMain)
                iosSimulatorArm64Main.dependsOn(this)
            }
            val iosSimulatorArm64Test by getting
            val iosTest by getting {
                dependsOn(commonTest)
                iosSimulatorArm64Test.dependsOn(this)
            }
            targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().all {
                val mainSourceSet = compilations.getByName("main").defaultSourceSet
                val testSourceSet = compilations.getByName("test").defaultSourceSet

                mainSourceSet.dependsOn(iosMain)
                testSourceSet.dependsOn(iosTest)
            }
        }
    }
}


android {
    buildFeatures {
        viewBinding = true
//        compose = true
    }

    dataBinding {
        isEnabled = true
    }

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.1.1"
//        useLiveLiterals = true
//    }
    compileSdk = 32
    buildToolsVersion = "32.0.0"
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("../../docs/dokka"))
}
