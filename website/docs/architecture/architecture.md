---
id: architecture_overview
title: Architecture Overview
hide_title: true
sidebar_label: Overview
---

## Inspiration
The architecture was inspired by the following design patterns and UI frameworks (among others):

#### Design patterns
- MVI (Model-View-Intent)
- MVVM (Model-View-ViewModel)
- [SAM](https://sam.js.org)
- [Flutter BloC](https://www.youtube.com/watch?v=RS36gBEp8OI)

#### Frameworks
- [Orbit](https://orbit-mvi.org)
- [Kotlin MVI](https://arkivanov.github.io/MVIKotlin)
- [Redux Kotlin](https://reduxkotlin.org)
- [Reduce](https://github.com/genaku/Reduce)
- [Decompose](https://arkivanov.github.io/Decompose/)
- Redux

#### Learnings

The essential ideas we take from them are:

- data flowing between model and view is immutable
- data flows unidirectional from the view to the model and back
- data flows are reactive
- every concern is mapped to a component or a function to have clear separation
- functions are first-class citizens


## Goals
The architectural goals of the `Kotlin Bloc` framework are:
- Be **platform-agnostic**: it’s a KMM framework so this is obvious.
- Be **reactive**: reactive UIs have become the standard in recent years for good reasons.
- Be **composable**: ability to decompose the ui into small components and combine them into larger components again.
- Be as **un-opinionated** as possible: support different technologies, programming styles, app complexities and team sizes.
- Be **minimalistic** and **lightweight**: some existing frameworks are very comprehensive but are heavyweight and require to write lots of boilerplate code (e.g. [Decompose](https://arkivanov.github.io/Decompose/)). Strict contracts between components (leading to boilerplate code) are crucial in larger teams but they can bog down the team’s productivity when speed is crucial (and in smaller teams). A more strict approach should be supported but not enforced.
- Be **predictable**: the order of execution (synchronous and asynchronous) and the concurrency model must be clearly specified and lead to predictable and repeatable outcome.

## Design

![Bloc Architecture - Overview](../../static/img/Bloc%20Architecture%20-%20Bloc%20Overview.svg)

The framework has two main components:
- The **Bloc** (Business Logic Component) encapsulates your application's business logic. It receives **Action(s)** from the view, processes those actions and outputs **Proposals** and optionally **SideEffect(s)**.
- The **BlocState** holds the component's **State**. It's separate from the actual Bloc to support different scenarios like:
  - share state between business logic components
  - persist state (database, network)
  - use a global state container like Redux
  - others...

The **View** is obviously an important component too but technically not part of the framework itself (although there are [extensions](../extensions/overview.md) that support/simplify the implementation for different target platforms).

:::tip
Reducers in `Kotlin Bloc` return a `Proposal` instead of `State`. While in many cases they have the same type, thanks to `Proposals`:
- a `BlocState]` has the "power" to enforce domain specific rules like address validation or enrichment
- it's easy to connect a Redux store to a `Bloc` (as its `BlocState`), in which case `Proposals` become Redux actions (see [Redux](./extensions/redux/redux_motivation))
- use `Blocs` as `BlocsState` (see [Bloc isA BlocState](./blocstate/bloc_state.md#bloc-isa-blocstate))

The idea of `Proposals` was inspired by the [SAM pattern](https://sam.js.org/).
:::

Not surprisingly the Flutter Bloc nomenclature is used for some of the other components / sub-components of this architecture as well:
- A **Sink** is a destination for arbitrary data and used to send data from one to the next component. The processing of the data is either synchronous or asynchronous depending on the receiver.
- A **Stream** is a source of asynchronous data. Stream are always "hot" meaning data is emitted regardless whether a component is listening (or subscribed or collecting -> different names for the same thing). The streams for `State` and `SideEffect`s have important differences that will be explained in detail in [Bloc](./bloc/bloc.md) and in [Bloc State](./blocstate/bloc_state.md).