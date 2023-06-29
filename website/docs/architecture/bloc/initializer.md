---
id: initializer
title: Initializer
sidebar_label: Initializer
hide_title: true
---

## Definition

Initializers are functions executed when the bloc is created, typically to kick off some initial load. They can execute asynchronous code and dispatch actions to be processed by thunks and reducers. Initializers are executed exactly once during the [Lifecycle](./lifecycle) of a bloc.

:::tip
If more than one initializer is defined, building the bloc will fail with a runtime exception.
:::

### Context

An initializer is called with a `InitializerContext` as receiver. The context is giving access to the current `State`, a `Dispatcher` and a function to "reduce" state directly:


```kotlin
public data class InitializerContext<State, Action>(
    val getState: GetState<State>,
    val dispatch: Dispatcher<Action>,
    val reduce: (proposal: Proposal) -> Unit
)
```

### reduce()

Analogous to thunks, initializers have a `reduce()` function to eliminate boilerplate code:
```kotlin
onCreate {
    reduce(getState().copy(loading = true))
    val books = repository.load()
    reduce(state.copy(loading = false, books = books))
}
```

`reduce()` will suspend till the queued reducer was executed. This is identical to [how reduce() works in thunks](thunk#reduce) and guarantees that this always succeeds:
```
onCreate {
    reduce(newState)                    // <- suspends till the state reduction is done
    assertEquals(getState(), newState)  // <- assertion is always true
}
```

### dispatch()

`dispatch()` will suspend till the triggered reducer was executed. This is identical to [how dispatch() works in thunks](thunk#dispatch) and guarantees that this always succeeds:
```kotlin
onCreate {
    val state = getState()
    dispatch(Increment)                 // <- suspends till the reducer has run
    assertEquals(getState(), state + 1) // <- assertion is always true
}
reduce<Increment> {
    state + 1
}
```

## Execution
Actions dispatched by the initializer are processed by thunks and reducers even before `onStart()` is called. Actions that are not dispatched by the initializer however are ignored before the bloc transitions to the `Started` state (see [Lifecycle](lifecycle)). This guarantees that the initializer runs and finishes before any thunks and reducers are executed. The only exception to that rule is if the initializer launches asynchronous code e.g. via [launch](coroutine_launcher) and would dispatch actions from there (so don't do this).
### Example 1

```kotlin
thunk<Load> {
    dispatch(Loading)
    val result = repository.getPosts()
    dispatch(Loaded(result))
}

onCreate { 
    if (getState().isEmpty()) dispatch(Load) 
}
```

The order of declaration is irrelevant, the initializer will always be called first. 

The thunk's asynchronous code could also be in the initializer itself:

```kotlin
onCreate { 
    if (getState().isEmpty()) {
        dispatch(Loading)
        val result = repository.getPosts()
        dispatch(Loaded(result))
    }
}
```
### Example 2
```kotlin
val lifecycleRegistry = LifecycleRegistry()
val context = BlocContextImpl(lifecycleRegistry)

val bloc = bloc<Int, Int, Unit>(context, 1) {
    onCreate {
        dispatch(2)
    }
    reduce { getState() + action }
}

// initializer executes -> reduce state
lifecycleRegistry.onCreate()
delay(50)
assertEquals(3, bloc.value)

// this action however will be ignored
bloc.send(3)
delay(50)
assertEquals(3, bloc.value)

// after onStart() -> "regular" reducer is being executed
lifecycleRegistry.onStart()
bloc.send(3)
delay(50)
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
    reduce { getState() + action }
}

lifecycleRegistry.onCreate()

// this action will be ignored (also the initializer is still running)
bloc.send(3)
delay(50)
assertEquals(1, bloc.value)

lifecycleRegistry.onStart()

// this action will be queued... (and the initializer is still running)
bloc.send(3)
delay(50)
assertEquals(1, bloc.value)

// ...and processed once the initializer is done
delay(900)
assertEquals(6, bloc.value)
```