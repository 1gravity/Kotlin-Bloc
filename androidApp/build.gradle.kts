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
}

dependencies {
    implementation(project(":knot-core"))
    implementation(project(":knot-extension"))

    implementation("androidx.multidex:multidex:2.0.1")

    implementation(Kotlin.stdlib)
    implementation(AndroidX.appCompat)
    implementation(AndroidX.core.ktx)
    implementation(AndroidX.lifecycle.viewModelKtx)
    implementation(AndroidX.fragment.ktx)
    implementation(AndroidX.lifecycle.extensions)
    implementation(Google.android.material)
    implementation(AndroidX.constraintLayout)
    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.coroutines.android)
    implementation("com.github.kirich1409:viewbindingpropertydelegate:_")
    implementation("com.github.genaku.Android-PLog:plog-core:_")
    implementation("org.jetbrains.kotlin:kotlin-reflect:_")

    testImplementation(Testing.kotest.runner.junit5)
    testImplementation(KotlinX.coroutines.test)
    testImplementation(CashApp.turbine)
    testImplementation(Testing.junit4)
    testImplementation(Testing.mockK)
}