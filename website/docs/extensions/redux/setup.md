---
id: redux_setup
title: Redux Setup
sidebar_label: Setup
hide_title: true
---

## Dependency

To use the [Redux](https://developer.android.com/jetpack/compose) extensions please add the `bloc-redux` artifact as a dependency in the Gradle build file:

```kotlin
implementation("com.1gravity:bloc-redux:0.9.3")
```

## Libraries

The extension is built on top of two open-source libraries.


### Redux Kotlin

[Redux Kotlin](https://reduxkotlin.org/) is the actual state container and a port of JS Redux. While the library is mature, it doesn't seem to be maintained any more.

 To get it to work with current versions of Kotlin and different plugins, I had to fork the repo at https://github.com/reduxkotlin/redux-kotlin -> https://github.com/1gravity/redux-kotlin. The latter is also published on Maven Central with a different group id / artifact id than the original.

### Reselect

[Reselect](https://github.com/reduxkotlin/Reselect) is a [Redux Reselect](https://github.com/reduxjs/reselect) implementation to create memoized "selector" functions.

Memoized "selector" functions are functions that:
- select sub state from the Redux state tree
- subscribe to that sub state 
- inform the caller whenever that particular sub state in the state tree changes
- ignore changes in other parts of the state tree
- cache the selected state transparently for the caller (that's the memoized part)

[Reselect](https://github.com/reduxkotlin/Reselect) doesn't seem to be maintained any more. Since it has at least one critical bug, needed some updates and only consists of two files, I decided to include the whole library in the Redux extension module.
