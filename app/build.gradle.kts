plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.genaku.reduce"
        minSdk = 19
        targetSdk = 30
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
//    implementation("com.github.genaku.Reduce:reduce-core:_")
//    implementation("com.github.genaku.Reduce:reduce-ext:_")
    implementation(project(":reduce-core"))
    implementation(project(":reduce-ext"))

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