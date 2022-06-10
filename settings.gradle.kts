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
    id("de.fayard.refreshVersions") version "0.40.2"
}

include(":bloc-core")
include(":bloc-redux")
include(":bloc-compose")
include(":bloc-samples")
include(":androidApp")

project(":bloc-core").projectDir = file("bloc-core")
project(":bloc-redux").projectDir = file("bloc-redux")
project(":bloc-compose").projectDir = file("bloc-compose")
project(":bloc-samples").projectDir = file("bloc-samples")
