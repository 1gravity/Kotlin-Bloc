---
id: initializer
title: Initializer
sidebar_label: Initializer
hide_title: true
---

## Definition

Initializers are functions executed when the bloc is created, typically to kick off some initial load. They can execute asynchronous code and dispatch actions to be processed by other thunks and reducers. Initializers are executed exactly once during the [Lifecycle](./lifecycle) of a bloc.

:::tip
If more than one initializer is defined, the first one (according to their order of declaration) is used, all others are ignored.
:::

### Context

An initializer is called with a `InitializerContext` as receiver. The context is giving access to the current `State` and a `Dispatcher`:


```kotlin
public data class InitializerContext<State, Action>(
    val state: State,
    val dispatch: Dispatcher<Action>
)
```

## Execution
### Example 1

```kotlin
thunk<Load> {
    dispatch(Loading)
    val result = repository.getPosts()
    dispatch(Loaded(result))
}

onCreate { 
    if (state.isEmpty()) dispatch(Load) 
}
```

The order of declaration is irrelevant, the initializer will always be called first. 

The thunk's asynchronous code could also be in the initializer itself:

```kotlin
onCreate { 
    if (state.isEmpty()) {
        dispatch(Loading)
        val result = repository.getPosts()
        dispatch(Loaded(result))
    }
}
```

Actions dispatched by the initializer are processed by the thunks and reducers even before `onStart()` is called. Actions that are not dispatched by the initializer however are sent to a queue and are processed once the bloc transitions to the `Started` state (see [Lifecycle](lifecycle)). This way it's guaranteed that the initializer runs and finishes first before any thunks and reducers are executed. The only exception to that rule is if the initializer launches asynchronous code e.g. via [launch](coroutine_launcher) and would dispatch actions from there (so don't do this).

### Example 2
```kotlin
val lifecycleRegistry = LifecycleRegistry()
val context = BlocContextImpl(lifecycleRegistry)

val bloc = bloc<Int, Int, Unit>(context, 1) {
    onCreate {
        delay(1000)
        dispatch(2)
    }
    reduce { state + action }
}

lifecycleRegistry.onCreate()

// this action will be queued
bloc.send(3)

delay(1050)
assertEquals(3, bloc.value)

lifecycleRegistry.onStart()
delay(100)

// and only processed once the the initializer is done and onStart() was called
assertEquals(6, bloc.value)
```
### Example 3

```kotlin
val lifecycleRegistry = LifecycleRegistry()
val context = BlocContextImpl(lifecycleRegistry)

val bloc = bloc<Int, Int, Unit>(context, 1) {
    onCreate {
        delay(1000)
        dispatch(2)
    }
    reduce { state + action }
}

lifecycleRegistry.onCreate()

// this action won't be processed before the initializer is done...
bloc.send(3)

lifecycleRegistry.onStart()
delay(100)

// ...which isn't the case here yet...
assertEquals(1, bloc.value)

delay(950)

// ...but after 1050ms initializer and dispatched action are both processed
assertEquals(6, bloc.value)
```
