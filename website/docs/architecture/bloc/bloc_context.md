---
id: bloc_context
title: Bloc Context
sidebar_label: Bloc Context
hide_title: true
---

## Definition

A `BlocContext` is an interface that extends three [Essenty](https://github.com/arkivanov/Essenty) interfaces:

```kotlin
public interface BlocContext : LifecycleOwner, InstanceKeeperOwner, BackPressedHandlerOwner
```

If you read the chapter [Lifecycle](./lifecycle), you'll have realized how crucial the lifecycle is for the bloc. The lifecycle controls all three `CoroutineScopes` and thus  the coroutines / jobs. The `BlocContext` is the object passed into a bloc's constructor holding that very lifecycle object.

As `InstanceKeeperOwner` the `BlocContext` holds a reference to an `InstanceKeeper` which allows  to store objects that are retained across configuration changes (on Android). While [Decompose](https://arkivanov.github.io/Decompose/) promotes the use of the `InstanceKeeper` as part of writing a Decompose component (Decompose's take on a business logic component), the `BlocContext` and as a consequence the `InstanceKeeper` is not accessible to the actual business logic code. The `InstanceKeeper` specifically is nevertheless critical in implementing many of the pretty cool [Android Extensions](../../extensions/android)

## Creation

Every bloc takes a `BlocContext` in its constructor. There are extensions on both Android and IOS to create a `BlocContext` described in the [Extensions](../../extensions/overview) section. Creating a `BlocContext` and a `Bloc` can be as easy as:

```kotlin
private val bloc by getOrCreate { context -> bloc(context) }

// or shorter
private val bloc by getOrCreate { bloc(it) }
```

Note that above bloc will be retained across configuration changes on Android.