import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

version = "1.0"

kotlin {
    android()
    ios()
    // Note: iosSimulatorArm64 target requires that all dependencies have M1 support
//    iosSimulatorArm64()

    cocoapods {
        summary = "Reactive state container library for KMM"
        homepage = "https://github.com/1gravity/Knot"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "knot-core"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(KotlinX.coroutines.core)

                // Badoo's Reaktive library
                implementation("com.badoo.reaktive:reaktive:_")

                // Logging
                implementation(Touchlab.kermit)

                // https://github.com/michaelbull/kotlin-result
                implementation("com.michael-bull.kotlin-result:kotlin-result:_")
                implementation("com.michael-bull.kotlin-result:kotlin-result-coroutines:_")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidTest by getting

        val iosMain by getting
        val iosTest by getting
//        val iosSimulatorArm64Main by getting {
//            dependsOn(iosMain)
//        }
//        val iosSimulatorArm64Test by getting {
//            dependsOn(iosTest)
//        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
}
