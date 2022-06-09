---
id: ios_bloc_observer
title: Bloc Observer
sidebar_label: Bloc Observer
hide_title: true
---

## BlocObserver

A bloc exposes a `StateStream` and a `SideEffectStream` which are essentially Kotlin Flows. The question is how can a Kotlin Flow be observed in Swift? Different solutions have been proposed:
- https://betterprogramming.pub/using-kotlin-flow-in-swift-3e7b53f559b6
- https://johnoreilly.dev/posts/kotlinmultiplatform-swift-combine_publisher-flow/
- https://dev.to/touchlab/working-with-kotlin-coroutines-and-rxswift-24fa
- https://github.com/FutureMind/koru
- https://touchlab.co/kotlin-coroutines-swift-revisited/

Some of these solutions are more generic than what we need for our purpose which is simply be able to observe the streams in SwiftUI and update the view when state changes.

The [BlocObserver](https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocObserver.swift) class:
- observes both streams and exposes them as [@Published](https://developer.apple.com/documentation/combine/published) properties (-> the `BlocObserver` needs to be an [ObservableObject](https://developer.apple.com/documentation/combine/observableobject))
- `state` and `sideEffect` are the @Published properties than can be observed by the view
- creates a lifecycle used to `unsubscribe` from the bloc, the lifecycle is tied to the "lifecycle" of the `BlocObserver` object itself

```swift
    private let holder = BlocHolder { CalculatorKt.bloc(context: $0) }
    
    @ObservedObject
    private var model: BlocObserver<CalculatorState, CalculatorAction, KotlinUnit>

    init() {
        self.model = BlocObserver(self.holder.value)
    }
```

:::tip
The lifecycle is tied to the lifecycle of the `BlocObserver` object itself. As with `BlocHolder` that means that you need to keep an explicit reference to that object like in the example above. See also: [BlocHolder](bloc_holder.md#)
:::