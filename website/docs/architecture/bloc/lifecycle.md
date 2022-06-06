---
id: lifecycle
title: Lifecycle
sidebar_label: Lifecycle
hide_title: true
---

## Two/Three Lifecycles

A bloc has two or three relevant lifecycles:
- The `View` lifecycle which is a platform-specific lifecycle.
- The [Essenty](https://github.com/arkivanov/Essenty) lifecycle which thanslates the `View` lifecycle to a platform-agnostic lifecycle.
- Depending on the `View` implementation, there can be a "component" lifecycle which sits in between the other two lifecycles. On Android that would typically be a `ViewModel` lifecycle. On iOS there's a `BlocHolder` or `BlocComponent` lifecycle. This and how it ties to the other two lifecycles will be discussed in more detail in  [Extensions](../../extensions/overview).

From a `Bloc's` perspective, the `Essenty` lifecycle is the most relevant one.

## Essenty Lifecycle

![Bloc Architecture - Details](../../../static/img/BLoC%20Architecture%20-%20External%20Lifecycle.svg)

### onCreate()

`onCreate()` is called when the component controlling the lifecycle is created. If there's an [Initializer](initializer), the bloc will execute it in its own `CoroutineScope`.

### onStart()

`onStart()` is called when the component controlling the lifecycle is started. That's typically when the UI is being displayed / rendered (platform specific meaning). This is the moment the bloc starts processing actions for thunks and reducers. The workers that process the two queues / channels are started within their own `CoroutineScope`.

### onResume() / onPause()

These two events are ignored by the bloc.

### onStop()

`onStop()` is called when the component controlling the lifecycle is stopped. That's typically when the UI stops showing (platform specific meaning). When this happens, the bloc stops processing actions for thunks and reducers. The `CoroutineScopes` created for the workers that process the two queues / channels are cancelled (and thus will all started coroutines).

Note that the bloc still accepts actions, even though they aren't processed when the bloc is stopped. Processing of actions will resume when `onStart()` is called again (although all coroutines were cancelled so resume only applies to the processing of the two queues).

### onDestroy()

When the component controlling the lifecycle is destroyed, the Bloc will cancel the 
[Initializer](initializer) `CoroutineScope`. If the initializer is still running, it would be stopped (all couroutines are cancelled).


## CoroutineScopes

A bloc has three `CoroutineScopes` which are tied to the lifecycle of the bloc as follows:


| Lifecycle Event | CoroutineScope    | Operation                                      |
| --------------- | ----------------- | ---------------------------------------------- |
| onCreate()      | InitializerScope  | execute Initializer                            |
| onStart()       | ThunkScope        | create scope -> start processing thunk queue   |
|                 | ReduceScope       | create scope -> start processing reducer queue |
| onStop()        | ThunkScope        | cancel scope -> stop thunk coroutines          |
|                 | ReduceScope       | cancel scope -> stop reduce coroutines         |
| onDestroy()     | InitializerScope  | cancel scope -> stop initializer coroutines    |
