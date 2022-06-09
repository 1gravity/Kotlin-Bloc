---
id: ios_overview
title: Overview
sidebar_label: Overview
hide_title: true
---

## Overview

Using `Blocs` on iOS isn't as straight forward as using it on Android. That's mostly because Kotlin and Swift have some fundamental differences and also because Objective-C sits in between the two languages as the "interface language". One of the culprits that made the integration in Swift difficult is the fact that:
> Generics can only be defined on classes, not on interfaces (protocols in Objective-C and Swift) or functions
 https://kotlinlang.org/docs/native-objc-interop.html.

`Kotlin BLoC` uses generic types extensively and while some interfaces could be converted to abstract classes to better support iOS, the numerous extension functions that make `Kotlin BLoC` so powerful, still need to use generic types.

At least in [SwiftUI](https://developer.apple.com/xcode/swiftui/) the integration with blocs isn't too bad once we add some helper classes though.
