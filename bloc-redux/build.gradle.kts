@file:Suppress("UnusedPrivateMember")

import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict

plugins {
    id("bloc-android-base")

    id("org.jetbrains.dokka")

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
                implementation(project(":bloc-core"))

                implementation(libs.kotlinx.coroutines.core)

                // Reaktive (https://github.com/badoo/Reaktive)
                // we only use the DisposableScope
                implementation(libs.reaktive)

                // Essenty (https://github.com/arkivanov/Essenty)
                api(libs.essenty.lifecycle)

                // Redux store (https://reduxkotlin.org)
                api(libs.redux.kotlin.threadsafe)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
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

tasks.dokkaHtmlPartial.configure {
    moduleName.set("Bloc Redux")
    dokkaSourceSets {
        configureEach {
            suppress.set(false)
            includeNonPublic.set(false)
        }
    }
}
