---
id: bloc_state_builder
title: Bloc State Builder
sidebar_label: Bloc State Builder
hide_title: true
---

A `BlocState` can be defined using a `BlocStateBuilder`

```kotlin
blocState<CountState, CountState> {  
    initialState = CountState(1)
 
    accept { proposal, state ->
        proposal
    }
}
```

- `initialState` is obviously the initial state of the `BlocState` (and this of the `Bloc`)
- `accept` is the function that accepts/rejects a proposal and updates the state if it's accepted (see [Overview](../architecture))

`initialState` and `accept` are both mandatory parameters(unfortunately there's no compile time check for this).

Since `State` and `Proposal` are identical in avove example, it can be simplified to:

```kotlin
blocState(CountState(1))
```