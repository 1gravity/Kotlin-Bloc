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

    jvm()

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
                api("com.arkivanov.essenty:lifecycle:_")

                // Logging (https://github.com/touchlab/Kermit)
                implementation(Touchlab.kermit)

                implementation(AndroidX.Compose.compiler)
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
        val androidTest by getting
    }
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0-beta02"
        useLiveLiterals = true
    }
}

tasks.dokkaHtmlPartial.configure {
    moduleName.set("Bloc Compose")
    dokkaSourceSets {
        configureEach {
            suppress.set(false)
            includeNonPublic.set(true)  // we want to see some details
        }
    }
}
