---
id: lifecycle
title: Lifecycle
sidebar_label: Lifecycle
hide_title: true
---

## Four Lifecycles

A bloc has four relevant lifecycles:
1. The `View` lifecycle which is a platform-specific lifecycle.
2. The [Essenty](https://github.com/arkivanov/Essenty) lifecycle which translates the `View` lifecycle to a platform-agnostic lifecycle.
3. There's typically a platform specific lifecycle between the `View` and the [Essenty](https://github.com/arkivanov/Essenty) lifecycle. On Android that would normally be a `ViewModel` lifecycle. On iOS there's a `BlocHolder` or `BlocComponent` lifecycle. This and how it ties to the other two lifecycles will be discussed in more detail in  [Extensions](../../extensions/overview).
4. The bloc has a lifecycle of its own. It's "driven" by the [Essenty](https://github.com/arkivanov/Essenty) lifecycle but adds states to manage its internal workings (see [Bloc Lifecycle](lifecycle.md#bloc-lifecycle)).

From a `Bloc's` perspective, the `Essenty` lifecycle is the most relevant one.

## Essenty Lifecycle

![Bloc Architecture - Details](../../../static/img/Bloc%20Architecture%20-%20External%20Lifecycle.svg)

### onCreate()

`onCreate()` is called when the component controlling the lifecycle is created. If there's an [Initializer](initializer), the bloc will start executing it in its own `CoroutineScope`.

### onStart()

`onStart()` is called when the component controlling the lifecycle is started. That's typically when the UI is being displayed / rendered (platform specific meaning). This is the moment the bloc starts processing actions for thunks and reducers. The workers that process the two queues / channels are started within their own `CoroutineScope`.
:::tip
`onStart()` doesn't move the bloc to its `Started` state if the initializer hasn't finished yet (see [Bloc Lifecycle](lifecycle.md#bloc-lifecycle))
:::

### onResume() / onPause()

These two events are ignored by the bloc.

### onStop()

`onStop()` is called when the component controlling the lifecycle is stopped. That's typically when the UI stops showing (platform specific meaning). When this happens, the bloc stops processing actions for thunks and reducers. The `CoroutineScopes` created for the workers that process the two queues / channels are cancelled (and thus will all started coroutines).

:::tip
After `onStop()` all actions sent to the bloc will be ignored. If the bloc is started again (`onStart()`), new actions will be processed but the ones that were sent in `Stopped` state are gone.
:::

### onDestroy()

When the component controlling the lifecycle is destroyed, the Bloc will cancel the 
[Initializer](initializer) `CoroutineScope`. If the initializer is still running, it would be stopped (all coroutines are cancelled). Once a bloc is destroyed, it's can't be used any more (`onCreate` will be ignored).

## Bloc Lifecycle

The Bloc lifecycle is driven by the [Essenty](https://github.com/arkivanov/Essenty) lifecycle on one hand and by the logic to synchronize initializer and reducer/thunk execution on the other hand. As explained [here](./initializer.md) initializers need to finish execution before the bloc transitions to the `Started` state (which will start the processing of thunks and reducers). 

The Bloc lifecycle is driven by the following state machine:

![Bloc Architecture - Details](../../../static/img/Bloc%20Architecture%20-%20Internal%20Lifecycle.svg)

For `Kotlin Bloc` users this isn't really relevant. Important to remember is only the fact that the order of execution is guaranteed. An initializer will always run and terminate before thunks and reducers are executed, regardless of the [Essenty](https://github.com/arkivanov/Essenty) lifecycle. Actions (and thunks/reducers in MVVM+ style) dispatched to the bloc after [Essenty's](https://github.com/arkivanov/Essenty) lifecycle `onStart()` will be sent to a queue and processed once the initializer is done (in the same order they were sent).
## CoroutineScopes

A bloc has three `CoroutineScopes` which are tied to the lifecycle of the bloc as follows:


| Lifecycle Event | CoroutineScope    | Operation                                                                |
| --------------- | ----------------- | ------------------------------------------------------------------------ |
| onCreate()      | InitializerScope  | create scope -> start initializer                                        |
| onStart()       | ThunkScope        | create scope -> start processing thunk queue (if initializer is done)    |
|                 | ReduceScope       | create scope -> start processing reducer queue (if initializer is done)  |
| onStop()        | ThunkScope        | cancel scope -> stop thunk coroutines                                    |
|                 | ReduceScope       | cancel scope -> stop reduce coroutines                                   |
| onDestroy()     | InitializerScope  | cancel scope -> stop initializer coroutines                              |
