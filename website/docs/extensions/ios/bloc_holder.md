---
id: ios_bloc_holder
title: Bloc Holder
sidebar_label: Bloc Holder
hide_title: true
---

## BlocHolder

The [BlocHolder](https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocHolder.swift) is the iOS equivalent of [getOrCreate()](../android/bloc_context.md#activityfragment) on Android:

- it creates a `BlocContext` with a lifecycle tied to the "lifecycle" of the `BlocHolder` object itself
- it calls a factory method with the `BlocContext` as parameter in order to create the `Bloc`
- it exposes the `Bloc` and the lifecycle as properties

Example:

```swift
struct CalculatorView: View {
    private let holder = BlocHolder { CalculatorKt.bloc(context: $0) }
```

:::tip
The lifecycle is tied to the lifecycle of the `BlocHolder` object itself. Unfortunately that means that you need to keep an explicit reference to that object like in the example above.

It's not good enough to create the `BlocHolder` and only keep a reference to the bloc and/or the lifecycle. The wrapper class (that is the `BlocHolder`) will be removed from memory eventually, even if the bloc is still in use. When that happens the bloc will be stopped/destroyed prematurely.

Experiments with tying the lifecycle to a `View` e.g. by using `onAppear()` / `onDisappear()`, didn't yield the expected results.
:::
