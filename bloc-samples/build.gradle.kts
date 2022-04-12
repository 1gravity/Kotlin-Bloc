import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("kotlin-parcelize")

    // todo create a separate module for Jetbrains Compose
    id("org.jetbrains.compose")

    id("android-library-base")
}

version = "1.0"

kotlin {
    android()

    // todo ...
    val isMacOsX = false
//    val isMacOsX = DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX
    if (isMacOsX) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    cocoapods {
        summary = "Reactive state management library for KMM"
        homepage = "https://github.com/1gravity/Kotlin-Bloc"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "bloc-samples"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":bloc-core"))
                implementation(project(":bloc-redux"))
                implementation(project(":bloc-compose"))

                implementation(KotlinX.coroutines.core)

                // Redux store (https://reduxkotlin.org)
                implementation("org.reduxkotlin:redux-kotlin-threadsafe:_")
                implementation("org.reduxkotlin:redux-kotlin-thunk:_")

                // Essenty (https://github.com/arkivanov/Essenty)
                implementation("com.arkivanov.essenty:lifecycle:_")
                implementation("com.arkivanov.essenty:parcelable:_")
                implementation("com.arkivanov.essenty:state-keeper:_")
                implementation("com.arkivanov.essenty:instance-keeper:_")
                implementation("com.arkivanov.essenty:back-pressed:_")

                // Koin
                implementation(Koin.core)

                // Ktor
                implementation(Ktor.client.core)
                implementation(Ktor.client.logging)
                implementation(Ktor.client.json)
                implementation(Ktor.client.serialization)
                implementation("io.ktor:ktor-client-content-negotiation:_")
                implementation("io.ktor:ktor-serialization-kotlinx-json:_")

                // Logging (https://github.com/touchlab/Kermit)
                implementation(Touchlab.kermit)

                // BigNums (https://github.com/ionspin/kotlin-multiplatform-bignum)
                implementation("com.ionspin.kotlin:bignum:_")

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
                implementation(Ktor.client.cio)

                implementation(AndroidX.appCompat)
                implementation(AndroidX.activity.compose)
                implementation(AndroidX.compose.material)
                implementation(AndroidX.compose.animation)
                implementation(AndroidX.compose.ui.tooling)
            }
        }
        val androidTest by getting

        if (isMacOsX) {
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                dependencies {
                    implementation("io.ktor:ktor-client-ios:_")
                }
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

//android {
//    buildFeatures {
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.1.1"
//    }
//    buildToolsVersion = "32.0.0"
//}