import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    id("bloc-android-base")

    id("kotlin-parcelize")
    kotlin("plugin.serialization")
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
                transitiveExport = true
                export(project(":blocCore"))
                export(project(":blocRedux"))
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
                api(project(":blocCore"))
                api(project(":blocRedux"))

                implementation(KotlinX.coroutines.core)

                // Redux store (https://reduxkotlin.org)
                implementation("com.1gravity.redux:redux-kotlin-threadsafe:0.5.8-SNAPSHOT")

                // Essenty (https://github.com/arkivanov/Essenty)
                api("com.arkivanov.essenty:lifecycle:_")
                api("com.arkivanov.essenty:parcelable:_")
                api("com.arkivanov.essenty:state-keeper:_")
                api("com.arkivanov.essenty:instance-keeper:_")
                api("com.arkivanov.essenty:back-pressed:_")

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
