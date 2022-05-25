rootProject.name = "Kotlin-Bloc"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://jitpack.io")
    }
}

plugins {
    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.40.1"
}

include(":blocCore")
include(":blocRedux")
include(":blocCompose")
include(":blocSamples")
include(":androidApp")

project(":blocCore").projectDir = file("bloc-core")
project(":blocRedux").projectDir = file("bloc-redux")
project(":blocCompose").projectDir = file("bloc-compose")
project(":blocSamples").projectDir = file("bloc-samples")
