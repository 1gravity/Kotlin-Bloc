---
id: redux_bloc_state
title: Redux Bloc State
sidebar_label: Redux Bloc State
hide_title: true
---

## ReduxBlocState

![Bloc Redux](../../../static/img/BLoC%20Architecture%20-%20ReduxBLoCState.svg)

The `ReduxBlocState` is a `BlocState` with some "superpowers":
- `Proposals` are dispatched to the Redux store without modification -> `Proposals` are the [Redux actions](https://redux.js.org/tutorials/fundamentals/part-3-state-actions-reducers)
- a selector function selects sub state from the global state tree -> `ReduxModel`
- a mapper function maps the selected sub state / `ReduxModel` to bloc `State` 

:::tip
Combining Redux with `Kotlin BLoC` can mean more than using the store as a global state container. We can leverage all the Redux functionality the library has to offer, e.g.:
- when `Proposals` are dispatched to the Redux store, they can be processed directly or go through middleware (e.g. thunk-middleware) before state is altered
- the store can have its own reducers that work in conjunction with the Bloc reducers
- state can be altered by dispatching actions directly to the store
:::

### Building ReduxBlocState 

Like for regular `BlocState`, there's also a builder for `ReduxBlocState` although the builder itself is used only internally. To create a ReduxBlocState there's an extension function `toBlocState()`:


```kotlin
val blocState = reduxStore.toBlocState(
    context = context,
    select = { reduxModel ->  reduxModel.counter },
    map = { model -> model.count }
)
```

To use a Redux store as `BlocState`, create the store first, then call `toBlocState()` providing the `BlocContext`, a `select` and a `map` function.  

The `ReduxBlocState` built above can be used like any regular `BlocState` when creating a `Bloc`:

```kotlin
bloc<Int, Action, Nothing, ReduxAction>(context, blocState) {
    // reducer, thunks etc.
}
```

:::tip
The `BlocContext` is needed because `ReduxBlocState` subscribes to the Redux store and needs to unsubscribe when the bloc is destroyed (see also [Reselect](./redux_setup#reselect)).
:::