plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("kotlin-parcelize")
    id("org.jetbrains.dokka")
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
            baseName = "bloc-core"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(KotlinX.coroutines.core)

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

                // Logging (https://github.com/touchlab/Kermit)
                implementation(Touchlab.kermit)

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
                implementation(AndroidX.appCompat)
                implementation(AndroidX.activity.ktx)
                implementation(AndroidX.fragment.ktx)
                implementation(AndroidX.navigation.fragmentKtx)

                implementation(AndroidX.lifecycle.viewModelKtx)
                implementation(AndroidX.lifecycle.viewModelCompose)
                implementation(AndroidX.lifecycle.viewModelSavedState)
                implementation(AndroidX.lifecycle.liveDataKtx)
                implementation(AndroidX.lifecycle.runtimeKtx)

                implementation(AndroidX.activity.compose)
                implementation(AndroidX.compose.runtime)
                implementation(AndroidX.compose.compiler)
                implementation(AndroidX.compose.ui)
                implementation(AndroidX.compose.animation)
                implementation(AndroidX.compose.material)
            }
        }
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
    buildToolsVersion = "32.0.0"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = 21
        targetSdk = 32
        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    dataBinding {
        isEnabled = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.0-beta04"
        useLiveLiterals = true
    }
}

tasks.dokkaHtml.configure {
    outputDirectory.set(buildDir.resolve("../../docs/dokka"))
}
