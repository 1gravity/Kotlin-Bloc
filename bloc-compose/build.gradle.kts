plugins {
    id("kotlin-multiplatform")
    id("com.android.library")

    id("org.jetbrains.dokka")
    id("org.jetbrains.compose")

    id("android-library-base")
}

kotlin {
    android()

    jvm()
    js().browser()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":bloc-core"))

                implementation(KotlinX.coroutines.core)

                // Essenty (https://github.com/arkivanov/Essenty)
                implementation("com.arkivanov.essenty:lifecycle:_")
                implementation("com.arkivanov.essenty:parcelable:_")
                implementation("com.arkivanov.essenty:state-keeper:_")
                implementation("com.arkivanov.essenty:instance-keeper:_")
                implementation("com.arkivanov.essenty:back-pressed:_")

                // Logging (https://github.com/touchlab/Kermit)
                implementation(Touchlab.kermit)

                implementation(AndroidX.Compose.compiler)
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
        kotlinCompilerExtensionVersion = "1.1.1"
        useLiveLiterals = true
    }
    compileSdk = 32
    buildToolsVersion = "32.0.0"
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("../../docs/dokka"))
}
