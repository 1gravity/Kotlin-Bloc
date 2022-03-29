plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("kotlin-parcelize")
}

version = "1.0"

kotlin {
    android()
    ios()
    // Note: iosSimulatorArm64 target requires that all dependencies have M1 support
//    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "bloc-samples"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(KotlinX.coroutines.core)
                implementation(project(":bloc-core"))

                // Redux store (https://reduxkotlin.org)
                implementation("org.reduxkotlin:redux-kotlin-threadsafe:_")
                implementation("org.reduxkotlin:redux-kotlin-thunk:_")

                // Reaktive (https://github.com/badoo/Reaktive)
                implementation("com.badoo.reaktive:reaktive:_")

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
                implementation(Ktor.client.serialization)
                implementation(Ktor.client.json)
                implementation(Ktor.client.logging)

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

                implementation(AndroidX.activity.compose)
                implementation(AndroidX.lifecycle.viewModelCompose)
                implementation(AndroidX.compose.material)
                implementation(AndroidX.compose.animation)
                implementation(AndroidX.compose.ui.tooling)
            }
        }
        val androidTest by getting
//        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
//            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("io.ktor:ktor-client-ios:_")
            }
        }
//        val iosSimulatorArm64Test by getting
        val iosTest by getting {
            dependsOn(commonTest)
//            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
    buildFeatures {
//        viewBinding = true
        compose = true
    }
    dataBinding {
//        isEnabled = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
}