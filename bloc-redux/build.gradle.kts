import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    id("bloc-android-base")
    kotlin("native.cocoapods")

    id("org.jetbrains.dokka")

    id("bloc-publish")
}

version = "1.0"

kotlin {
    // todo
    // explicitApi = ExplicitApiMode.Strict

    android{
        publishLibraryVariants("release")
    }
    jvm()

    val isMacOsX = DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX
    if (isMacOsX) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    cocoapods {
        summary = "Reactive state management library for KMM"
        homepage = "https://github.com/1gravity/Kotlin-Bloc"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "blocRedux"
            isStatic = false
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

                implementation(KotlinX.coroutines.core)

                // Reaktive (https://github.com/badoo/Reaktive)
                // we only use the DisposableScope
                implementation("com.badoo.reaktive:reaktive:_")

                // Essenty (https://github.com/arkivanov/Essenty)
                api("com.arkivanov.essenty:lifecycle:_")
                api("com.arkivanov.essenty:parcelable:_")
                api("com.arkivanov.essenty:state-keeper:_")
                api("com.arkivanov.essenty:instance-keeper:_")
                api("com.arkivanov.essenty:back-pressed:_")

                // Redux store (https://reduxkotlin.org)
                api("com.1gravity.redux:redux-kotlin-threadsafe:0.5.8-SNAPSHOT")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting
        val androidTest by getting

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

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("../../docs/dokka"))
}
