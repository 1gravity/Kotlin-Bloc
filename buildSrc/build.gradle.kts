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
    api(Android.tools.build.gradlePlugin)
}
