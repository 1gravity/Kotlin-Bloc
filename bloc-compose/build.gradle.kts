@file:Suppress("UnusedPrivateMember", "UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict

plugins {
    id("bloc-android-base")

    id("org.jetbrains.dokka")
    id("org.jetbrains.compose")

    id("bloc-publish")
}

kotlin {
    explicitApi = Strict

    android {
        publishLibraryVariants("release")
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
                api(project(":bloc-core"))

                implementation(libs.kotlinx.coroutines.core)

                // Essenty (https://github.com/arkivanov/Essenty)
                api(libs.lifecycle)

                // Logging (https://github.com/touchlab/Kermit)
                implementation(libs.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidx.compose.compiler.get()
        useLiveLiterals = true
    }
}

tasks.dokkaHtmlPartial.configure {
    moduleName.set("Bloc Compose")
    dokkaSourceSets {
        configureEach {
            suppress.set(false)
            includeNonPublic.set(false)
        }
    }
}
