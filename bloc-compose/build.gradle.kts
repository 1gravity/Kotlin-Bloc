@file:Suppress("UnusedPrivateMember")

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

                implementation(KotlinX.coroutines.core)

                // Essenty (https://github.com/arkivanov/Essenty)
                api(libs.lifecycle)

                // Logging (https://github.com/touchlab/Kermit)
                implementation(Touchlab.kermit)
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
            }
        }
    }
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
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
