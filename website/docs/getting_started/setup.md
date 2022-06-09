---
id: setup
title: Setup
sidebar_label: Setup
hide_title: true
---

# Setup

[![Download](https://img.shields.io/maven-central/v/org.orbit-mvi/orbit-viewmodel)](https://search.maven.org/artifact/com.1gravity/bloc-core)

```kotlin
dependencies {
    // the core library
    implementation("com.1gravity:bloc-core:0.1.2-SNAPSHOT")

    // add to use the framework together with Redux
    implementation("com.1gravity:bloc-redux:0.1.2-SNAPSHOT")

    // useful extensions for Android and Jetpack/JetBrains Compose
    implementation("com.1gravity:bloc-compose:0.1.2-SNAPSHOT")
}
```