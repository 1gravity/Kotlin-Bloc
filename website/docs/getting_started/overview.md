---
id: overview
title: Overview
sidebar_label: Overview
hide_title: true
---

# Overview

`Kotlin Bloc` is a Multiplatform UI framework combining the best of MVI, MVVM and [SAM](https://sam.js.org/). It's 
- **simple**: designed from the ground up for simplicity with a super concise syntax
- **adaptable**: supports different programming styles
- **predictable**: write reactive applications that behave consistently and are easy to debug and test
- **composable**: grows with the complexity of app and the size of the team


![Bloc Architecture - Overview](../../static/img/Bloc%20Architecture%20-%20Bloc%20Overview.svg)

- The **Bloc** (Business Logic Component) encapsulates your application's business logic. It receives **Action(s)** from the view, processes those actions and outputs **Proposals** and optionally **SideEffect(s)**.
- The **BlocState** holds the component's **State**. It's separate from the actual Bloc to support different scenarios like:
  - share state between business logic components
  - persist state (database, network)
  - use a global state container like Redux
