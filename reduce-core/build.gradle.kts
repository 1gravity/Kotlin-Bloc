plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "com.github.genaku.reduce"

dependencies {
    implementation(Kotlin.stdlib)
    implementation(KotlinX.coroutines.core)
    implementation(KotlinX.coroutines.android)
}