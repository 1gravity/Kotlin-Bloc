plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.onegravity.bloc.sample"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    dataBinding {
        isEnabled = true
    }
}

dependencies {
    implementation(project(":bloc-core"))
    implementation(project(":bloc-samples"))

    implementation(Kotlin.stdlib)

    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.coroutines.android)

    implementation(AndroidX.multidex)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.lifecycle.viewModelKtx)
    implementation(AndroidX.fragment.ktx)
    implementation(AndroidX.constraintLayout)
    implementation(AndroidX.navigation.fragmentKtx)
    implementation(AndroidX.navigation.uiKtx)
    implementation(AndroidX.recyclerView)

    implementation(Google.android.material)

    implementation("org.jetbrains.kotlin:kotlin-reflect:_")

    implementation(Koin.android)

    implementation("com.github.bumptech.glide:glide:_")
    implementation("com.github.lisawray.groupie:groupie:_")
    implementation("com.github.lisawray.groupie:groupie-viewbinding:_")

    // Essenty (https://github.com/arkivanov/Essenty)
    implementation("com.arkivanov.essenty:lifecycle:_")
    implementation("com.arkivanov.essenty:parcelable:_")
    implementation("com.arkivanov.essenty:state-keeper:_")
    implementation("com.arkivanov.essenty:instance-keeper:_")
    implementation("com.arkivanov.essenty:back-pressed:_")

    testImplementation(Testing.kotest.runner.junit5)
    testImplementation(KotlinX.coroutines.test)
    testImplementation(CashApp.turbine)
    testImplementation(Testing.junit4)
    testImplementation(Testing.mockK)
}