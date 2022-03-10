plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.onegravity.knot.application"
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
    implementation(project(":knot-core"))
    implementation(project(":knot-samples"))

    implementation(Kotlin.stdlib)

    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.coroutines.android)

    implementation(AndroidX.multidex)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.lifecycle.viewModelKtx)
    implementation(AndroidX.fragment.ktx)
    implementation(AndroidX.lifecycle.extensions)
    implementation(AndroidX.constraintLayout)

    implementation(Google.android.material)

    implementation("com.github.kirich1409:viewbindingpropertydelegate:_")
    implementation("org.jetbrains.kotlin:kotlin-reflect:_")

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