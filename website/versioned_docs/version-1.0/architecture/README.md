---
id: architecture_overview
title: Architecture Overview
sidebar_label: Architecture Overview
hide_title: true
---

# Goals
Kotlin BLoC's architectural goals are:
- be platform-agnostic
- be minimalistic / lightweight
- don't be over-engineered
- requires to write very little code
- be as un-opinionated as possible -> scales with app complexity / team size
- be composable

[KMM UI Architecture - Part 2](https://medium.com/p/e52b84aeb94d) elaborates on those goals in more detail.


# Inspiration
The architecture was inspired by the following design patterns and UI frameworks among others:

#### Design patterns
- MVI (Model-View-Intent)
- MVVM (Model-View-ViewModel)
- [SAM](https://sam.js.org)
- Redux

#### Frameworks
- [Orbit](https://orbit-mvi.org)
- [Kotlin MVI](https://arkivanov.github.io/MVIKotlin)
- [Redux Kotlin](https://reduxkotlin.org)
- [Reduce](https://github.com/genaku/Reduce)
- [Decompose](https://arkivanov.github.io/Decompose/)

### Design Overview

<img alt="BLoC Architecture - Overview" src="./docs/BLoC Architecture - BLoC Overview.svg" width="625" />

The framework has two main components:
- The **BLoC** (Business Logic Component) encapsulates your application's business logic. It receives **Action(s)** from the view, processes those actions and outputs **Proposals** (State) and optionally **SideEffect(s)**.
- The **BLoCState** holds the component's **State**. It's separate from the actual BLoC to support different scenarios like:
  - share state between business logic components
  - persist state (database, network)
  - use a global state container like Redux
  - others...

The **View** is obviously an important component too but technically not part of the framework itself (although there are extensions that support/simplify the implementation for different target platforms).

