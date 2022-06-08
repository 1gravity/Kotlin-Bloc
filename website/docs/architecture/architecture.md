---
id: architecture_overview
title: Architecture Overview
hide_title: true
sidebar_label: Overview
---

## Goals
The architectural goals of the `Kotlin BLoC` framework are:
- be platform-agnostic, reactive and composable
- be minimalistic / lightweight -> no over-engineering
- simple concurrency model
- users write very little code
- be as un-opinionated as possible -> support different `View` technologies, programming styles, app complexities and team sizes

[KMM UI Architecture - Part 2](https://medium.com/p/e52b84aeb94d) elaborates on those goals in more detail.

## Inspiration
The architecture was inspired by the following design patterns and UI frameworks (among others):

#### Design patterns
- MVI (Model-View-Intent)
- MVVM (Model-View-ViewModel)
- [SAM](https://sam.js.org)
- Redux
- [Flutter BloC](https://www.youtube.com/watch?v=RS36gBEp8OI)

#### Frameworks
- [Orbit](https://orbit-mvi.org)
- [Kotlin MVI](https://arkivanov.github.io/MVIKotlin)
- [Redux Kotlin](https://reduxkotlin.org)
- [Reduce](https://github.com/genaku/Reduce)
- [Decompose](https://arkivanov.github.io/Decompose/)

## Design Overview

![Bloc Architecture - Overview](../../static/img/BLoC%20Architecture%20-%20BLoC%20Overview.svg)

The framework has two main components:
- The **Bloc** (Business Logic Component) encapsulates your application's business logic. It receives **Action(s)** from the view, processes those actions and outputs **Proposals** (State) and optionally **SideEffect(s)**.
- The **BlocState** holds the component's **State**. It's separate from the actual Bloc to support different scenarios like:
  - share state between business logic components
  - persist state (database, network)
  - use a global state container like Redux
  - others...

The **View** is obviously an important component too but technically not part of the framework itself (although there are [extensions](../extensions/README.md) that support/simplify the implementation for different target platforms).

Not surprisingly the Flutter Bloc nomenclature is used for some of the other components / sub-components of this architecture as well:
- A **Sink** is a destination for arbitrary data and used to send data from one to the next component. The processing of the data is either synchronous or asynchronous depending on the receiver.
- A **Stream** is a source of asynchronous data. Stream are always "hot" meaning data is emitted regardless whether a component is listening (or subscribed or collecting -> different names for the same thing). The streams for `State` and `SideEffect`s have important differences that will be explained in detail in [Bloc](./bloc/README.md) and in [Bloc State](./blocstate/README.md).