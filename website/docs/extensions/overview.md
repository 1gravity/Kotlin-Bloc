---
id: overview
title: Extensions Overview
sidebar_label: Overview
hide_title: true
---

While `Kotlin BLoC` is a platform-agnostic framework, apps ultimately run on a specific platform with a specific view technology. One of the architectural goals is to be "be as un-opinionated as possible" to support different technologies like `Storyboard` / `Swift UI` on iOS or `Jetpack Compose` / XML layouts + view or data binding on Android. Especially on Android there are many different technologies to implement the user interface: Activities, Fragments, ViewModels, view binding, data binding, RxJava, live data, Kotlin Flow, XML layouts and Jetpack Compose to name the most important ones.

The purpose of `Kotlin-BLoC` extensions is to support different technologies because, let's face it, most companies can't afford a greenfield approach but need to improve an existing product with existing technologies. `Kotlin-BLoC` can be added to literally any mobile tech stack, new features can be built using blocs and existing features can be migrated one step at a time.
While `Kotlin-BLoC` was built with multi-platform in mind, it's also a great framework for building "just" a native Android app and the many [Android Extensions](./android) can eliminate lots of existing boilerplate code.
