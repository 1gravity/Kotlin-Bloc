---
id: bloc_context
title: Bloc Context
sidebar_label: Bloc Context
hide_title: true
---

## Definition

A `BlocContext` is an interface (currently) matching [Essenty's LifecycleOwner](https://github.com/arkivanov/Essenty) interface:

```kotlin
public interface BlocContext : LifecycleOwner
```

If you read the chapter [Lifecycle](./lifecycle), you'll have realized how crucial the lifecycle is for the bloc. The lifecycle controls all three `CoroutineScopes` and thus  the coroutines / jobs. The `BlocContext` is the object passed into a bloc's constructor holding that very lifecycle object.

Future releases of the framework might add more functionality to the `BlocContext`.

## Creation

Every bloc takes a `BlocContext` in its constructor. There are extensions on both [Android](../../extensions/android/android_bloc_context) and [iOS](../../extensions/ios) to create one.

Creating a `BlocContext` and a `Bloc` on Android can be as easy as:

```kotlin
private val bloc by getOrCreate { bloc(it) }

// more verbose:
private val bloc by getOrCreate { context -> bloc(context) }
```

Note that this bloc will be retained across configuration changes (Android).