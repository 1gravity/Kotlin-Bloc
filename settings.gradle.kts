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

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("libs") {
            from(files("./buildSrc/gradle/libs.versions.toml"))
        }
    }
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
