@file:Suppress("UnusedPrivateMember")

import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    id("bloc-android-base")

    id("kotlin-parcelize")
    kotlin("plugin.serialization")

    // database
    id("com.squareup.sqldelight")
}

version = "1.0"

kotlin {
    android()

    val isMacOsX = DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX
    if (isMacOsX) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { target ->
            target.binaries.framework {
                baseName = "blocSamples"
                export(project(":bloc-core"))
                export(project(":bloc-redux"))
//                export("com.1gravity:bloc-core:_")
//                export("com.1gravity:bloc-redux:_")
                export(libs.lifecycle)
            }
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
                api(project(":bloc-core"))
                api(project(":bloc-redux"))
//                api("com.1gravity:bloc-core:_")
//                api("com.1gravity:bloc-redux:_")

                implementation(KotlinX.coroutines.core)

                // Redux store (https://reduxkotlin.org)
                implementation(libs.redux.kotlin.threadsafe)

                // Essenty (https://github.com/arkivanov/Essenty)
                api(libs.lifecycle)
                api(libs.parcelable)

                // Koin
                implementation(Koin.core)

                // Ktor
                implementation(Ktor.client.core)
                implementation(Ktor.client.logging)
                implementation(Ktor.client.json)
                implementation(Ktor.client.serialization)
                implementation(Ktor.client.contentNegotiation)
                implementation(Ktor.plugins.serialization.kotlinx.json)

                // SQLDelight (https://cashapp.github.io/sqldelight/)
                implementation(libs.com.squareup.sqldelight.runtime)
                implementation(Square.sqlDelight.extensions.coroutines)
                implementation(KotlinX.datetime)

                // Logging (https://github.com/touchlab/Kermit)
                implementation(Touchlab.kermit)

                // BigNums (https://github.com/ionspin/kotlin-multiplatform-bignum)
                implementation(libs.bignum)

                // UUIDs (https://github.com/benasher44/uuid)
                implementation(libs.uuid)

                // Kotlin Result (https://github.com/michaelbull/kotlin-result)
                implementation(libs.kotlin.result)
                implementation(libs.kotlin.result.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependencies {
                api(project(":bloc-compose"))
//                api("com.1gravity:bloc-compose:_")

                implementation(Ktor.client.cio)

                implementation(Square.sqlDelight.drivers.android)

                implementation(AndroidX.appCompat)
                implementation(AndroidX.activity.compose)
                implementation(AndroidX.compose.material)
                implementation(AndroidX.compose.animation)
                implementation(AndroidX.compose.ui.tooling)
            }
        }

        if (isMacOsX) {
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                dependencies {
                    implementation(libs.ktor.client.ios)
                    implementation(Square.sqlDelight.drivers.native)
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

sqldelight {
    database("TodoDatabase") {
        packageName = "com.onegravity.bloc"
        sourceFolders = listOf("sqldelight")
        dialect = "sqlite:3.25"
    }
}
