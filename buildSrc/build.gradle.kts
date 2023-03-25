plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api(libs.kotlin.gradle.plugin)
    api(libs.android.tools.gradle.plugin)
}
