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
                export(libs.essenty.lifecycle)
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

                implementation(libs.kotlinx.coroutines.core)

                // Redux store (https://reduxkotlin.org)
                implementation(libs.redux.kotlin.threadsafe)

                // Essenty (https://github.com/arkivanov/Essenty)
                api(libs.essenty.lifecycle)
                api(libs.essenty.parcelable)

                // Koin
                implementation(libs.koin.core)

                // Ktor
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.json)
                implementation(libs.ktor.client.serialization)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                // SQLDelight (https://cashapp.github.io/sqldelight/)
                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutines.extensions)
                implementation(libs.kotlinx.datetime)

                // Logging (https://github.com/touchlab/Kermit)
                implementation(libs.kermit)

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

                implementation(libs.ktor.client.okhttp)
                implementation(libs.sqldelight.android.driver)

                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.activity.compose)
                implementation(libs.androidx.compose.material)
                implementation(libs.androidx.compose.animation)
                implementation(libs.androidx.compose.ui.tooling)
            }
        }

        if (isMacOsX) {
            val iosX64Main by getting
            val iosArm64Main by getting
            val iosSimulatorArm64Main by getting
            val iosMain by creating {
                dependsOn(commonMain)
                dependencies {
                    implementation(libs.ktor.client.darwin)
                    implementation(libs.sqldelight.native.driver)
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
        dialect = "sqlite:${libs.versions.sqldelight.dialect.get()}"
    }
}
