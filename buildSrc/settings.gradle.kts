pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        // See https://jmfayard.github.io/refreshVersions
        id("de.fayard.refreshVersions") version "0.50.2"
    }
}

plugins {
    id("de.fayard.refreshVersions")
}
