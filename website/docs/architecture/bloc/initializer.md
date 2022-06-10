---
id: initializer
title: Initializer
sidebar_label: Initializer
hide_title: true
---

## Definition

Initializers are functions executed when the bloc is created, typically to kick off some initial load. They can execute asynchronous code and dispatch actions to be processed by other thunks and reducers. Initializers are executed exactly once during the [Lifecycle](./lifecycle) of a bloc.

### Context

An initializer is called with a `InitializerContext` as receiver. The context is giving access to the current `State`, a `Dispatcher` and a `CoroutineScope`:


```kotlin
public data class InitializerContext<State, Action>(
    val state: State,
    val dispatch: Dispatcher<Action>,
    val coroutineScope: CoroutineScope
)
```

Here's a typical example:

```kotlin
onCreate { 
    if (state.isEmpty()) dispatch(Load) 
}

thunk<Load> {
    dispatch(Loading)
    val result = repository.getPosts()
    dispatch(Loaded(result))
}
```

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

The order of declaration is irrelevant, the initializer will always be called first (see [Lifecycle](lifecycle)). It could however be that the initializer is still running when the bloc starts processing actions (thunks and reducers). This behavior might change in a future version.