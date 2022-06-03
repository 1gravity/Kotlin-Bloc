---
id: lifecycle
title: Lifecycle
sidebar_label: Lifecycle
hide_title: true
---

A bloc has an internal and an external lifecycle.

## Internal Lifecycle

The internal lifecycle is managed by a finite state machine and has the following states:

![Bloc Architecture - Details](../../../static/img/BLoC%20Architecture%20-%20Internal%20Lifecycle.svg)


### Created

When the Bloc is created it will transition to the `Created` state automatically

### Started
### Stopped
### Destroyed

## External Lifecycle

https://github.com/arkivanov/Essenty

![Bloc Architecture - Details](../../../static/img/BLoC%20Architecture%20-%20External%20Lifecycle.svg)

### Created
### Started
### Resumed
### Destroyed
