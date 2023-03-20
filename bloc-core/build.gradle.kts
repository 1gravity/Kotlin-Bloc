@file:Suppress("UnusedPrivateMember", "UnstableApiUsage")

import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict

plugins {
    id("bloc-android-base")

    id("kotlin-parcelize")
    id("org.jetbrains.dokka")

    // run "./gradlew tiTree assemble" to get the task tree for the "assemble" task
    id("org.barfuin.gradle.taskinfo")

    id("bloc-publish")
}

version = "1.0"

kotlin {
    explicitApi = Strict

    android {
        publishLibraryVariants("release")
    }

    val isMacOsX = DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX
    if (isMacOsX) {
        listOf(iosX64(), iosArm64(), iosSimulatorArm64())
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
                implementation(libs.kotlinx.coroutines.core)

                // Essenty (https://github.com/arkivanov/Essenty)
                api(libs.lifecycle)
                api(libs.instance.keeper)

                // Logging (https://github.com/touchlab/Kermit)
                implementation(libs.kermit)

                implementation(libs.atomicfu)

                // Kotlin Result (https://github.com/michaelbull/kotlin-result)
                implementation(libs.kotlin.result)
                implementation(libs.kotlin.result.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.fragment)
            }
        }

        if (isMacOsX) {
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                iosX64Main.dependsOn(this)
                iosArm64Main.dependsOn(this)
                iosSimulatorArm64Main.dependsOn(this)
            }
            val iosX64Test by getting
            val iosArm64Test by getting
            val iosSimulatorArm64Test by getting
            val iosTest by creating {
                dependsOn(commonTest)
                iosX64Test.dependsOn(this)
                iosArm64Test.dependsOn(this)
                iosSimulatorArm64Test.dependsOn(this)
            }
        }
    }
}

android {
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

tasks.dokkaHtmlPartial.configure {
    moduleName.set("Bloc Core")
    dokkaSourceSets {
        configureEach {
            suppress.set(false)
            includeNonPublic.set(false)
        }
    }
}
