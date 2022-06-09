---
id: redux_motivation
title: Redux Motivation
sidebar_label: Motivation
hide_title: true
---

Blocs are components designed to take care of a specific part of the business logic. Typically their scope is similar to a view model scope. Creating a more than basic app will require to compose blocs into larger business logic components. There are different ways to achieve this (e.g. use the same `BlocState` in multiple `Blocs` or use a [`Bloc` as `BlocState`](../../architecture/blocstate/bloc_state.md#bloc-isa-blocstate)) but one option is particularly enticing:

![Bloc Redux](../../../static/img/BLoC%20Architecture%20-%20BLoC%20Redux.svg)

A Redux store is a state container holding the application's state. While there can be more than one store, the intended pattern is to have only a single store (https://redux.js.org/faq/store-setup). A Redux store seems to be a good candidate for sharing state between `Blocs` if we find a way to slice and dice the global state tree into smaller bits to serve as a `Bloc's` state (which is exactly what this extension allows us to do).
