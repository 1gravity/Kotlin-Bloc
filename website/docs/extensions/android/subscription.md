---
id: android_subscription
title: Android Subscription
sidebar_label: Subscription
hide_title: true
---

Subscribing to the bloc's `StateStream` and `SideEffectStream` can be done in a single function call. 


```kotlin
bloc.subscribe(this, state = ::observeState, sideEffect = ::observeSideEffects)

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
The above example shows the subscription to a `Bloc`. There are extension functions to subscribe to a [BlocObservable](../../architecture/blocowner/bloc_observable#blocobservable) and to a [BlocObservableOwner](../../architecture/blocowner/bloc_observable#blocobservableowner) as well.
:::

:::tip
The `state` and `sideEffect` arguments are optional so you can subscribe to just one of the streams or to both (or to none).
:::