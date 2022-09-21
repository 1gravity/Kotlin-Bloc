import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Strict

plugins {
    id("bloc-android-base")

    id("kotlin-parcelize")
    id("org.jetbrains.dokka")

    // run "./gradlew tiTree assemble" to get the task tree for the "assemble" task
    id("org.barfuin.gradle.taskinfo")

    id("bloc-publish")
}

version = "1.0"

kotlin {
    explicitApi = Strict

    android {
        publishLibraryVariants("release")
    }

    jvm()

    val isMacOsX = DefaultNativePlatform.getCurrentOperatingSystem().isMacOsX
    if (isMacOsX) {
        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach {
            it.binaries.framework {
                baseName = "blocCore"
                isStatic = false
                transitiveExport = true
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
                implementation(KotlinX.coroutines.core)

                // Essenty (https://github.com/arkivanov/Essenty)
                api("com.arkivanov.essenty:lifecycle:_")
                api("com.arkivanov.essenty:instance-keeper:_")

                // Logging (https://github.com/touchlab/Kermit)
                implementation(Touchlab.kermit)

                implementation("org.jetbrains.kotlinx:atomicfu:_")

                // Kotlin Result (https://github.com/michaelbull/kotlin-result)
                implementation("com.michael-bull.kotlin-result:kotlin-result:_")
                implementation("com.michael-bull.kotlin-result:kotlin-result-coroutines:_")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(KotlinX.coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(AndroidX.appCompat)
                implementation(AndroidX.activity.compose)
                implementation(AndroidX.fragment)
//                implementation(AndroidX.compose.runtime)
//                implementation(AndroidX.compose.compiler)
//                implementation(AndroidX.compose.foundation)
            }
        }
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

android {
    buildFeatures {
        viewBinding = true
    }

    dataBinding {
        isEnabled = true
    }
}

tasks.dokkaHtmlPartial.configure {
    moduleName.set("Bloc Core")
    dokkaSourceSets {
        configureEach {
            suppress.set(false)
            includeNonPublic.set(true)  // we want to see some details
        }
    }
}
