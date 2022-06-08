---
id: bloc_observable
title: Bloc Observable
sidebar_label: Bloc Observable
hide_title: true
---

## BlocObservable

A `BlocObservable` is an object you can subscribe to state changes and side effects in a single `subscribe` call:

```kotlin
public abstract fun subscribe(
    lifecycle: Lifecycle,
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
)
```
:::tip
The subscription is tied to the (Essenty) lifecycle of the caller (see [Lifecycle](../bloc/lifecycle)) meaning with an `onStop()` event, the subscription ends as well.
:::

## BlocObservableOwner

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

### Extensions

To make the implementation of a `BlocObservableOwner` easy, you can use `toObservable()` to adapt a `Bloc` to a `BlocOwner`:

```kotlin
private val bloc = bloc(blocContext(context))
override val observable = bloc.toObservable()
```

Of course a class could simply implement `BlocOwner` instead of `BlocObservableOwner` but in some cases you don't want to expose the bloc itself but only the ability to subscribe to state changes and side effects.
