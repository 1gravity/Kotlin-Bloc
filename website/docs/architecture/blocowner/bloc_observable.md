---
id: bloc_observable
title: Bloc Observable
sidebar_label: Bloc Observable
hide_title: true
---

## BlocObservable

A `BlocObservable` is an object with a `subscribe()` function to observe state changes and side effects:

```kotlin
public fun subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
)
```

:::tip
The subscription is tied to the (Essenty) lifecycle of the caller (see [Lifecycle](../bloc/lifecycle)) meaning with an `onStop()` event, the subscription ends as well.
:::

A `BlocObservable` also exposes the bloc's current state as the `subscribe()`'s state function will only be called when the bloc's state changes:
```kotlin
public val value: State
```

:::tip
You will likely never call this directly but use one of the [extension functions](../../extensions/android/android_subscription).
:::

## BlocObservableOwner

Sometimes a component should not expose the `Bloc` as it would when implementing the [BlocOwner](bloc_owner) interface. If a component only requires users to observe the bloc's state and side effects (no actions or actions are encapsulated by the component), `BlocObservableOwner` is the right choice.

While a `BlocOwner` exposes a `Bloc` as property, `BlocObservableOwner` exposes a `BlocObservable` as property:

```kotlin
public interface BlocObservableOwner<out State : Any, out SideEffect : Any> {
    public val observable: BlocObservable<State, SideEffect>
}
```

While `BlocOwner` gives the implementing class the ability to use the "MVVM+" syntax, `BlocObservableOwner` gives users of an implementing class the ability to observe state and side effects with a single `subscribe` call:

```kotlin
// the ViewModel implements BlocObservableOwner
viewModel.subscribe(this, state = ::observeState, sideEffect = ::observeSideEffects)

private fun observeState(state: State) {
    // process the new state
    when (state) {
        Empty -> showEmptyPage()
        Loading -> showLoadingPage()
        is Loaded -> showContent(state)
        is Failure -> showError(state)
    }
}

private fun observeSideEffects(target: Target) {
    // process side effects
    navigateTo(target)
}
```

:::tip
As you can see in the [subscribe function's signature](#blocobservable), `state` and `sideEffect` are optional arguments so you can subscribe to both or just one of the them.
:::

### Adapter

To simplify the implementation of a `BlocObservableOwner`, use `toObservable()` to "convert" a `Bloc`:

```kotlin
// we can keep the Bloc private!
private val bloc = bloc(blocContext(context))

// and only expose the BlocObservable
override val observable = bloc.toObservable()
```