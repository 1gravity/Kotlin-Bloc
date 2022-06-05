---
id: bloc_builder
title: Bloc Builder
sidebar_label: Bloc Builder
hide_title: true
---

There's a `BlocBuilder` with a DSL to make the definition of `Blocs` easy. Best to let the examples speak for themselves:

```kotlin
fun bloc(context: BlocContext) = bloc<Int, Int>(
    this,
    1
    ) {
        reduce {
            logger.d("interceptor 1: $action -> ${action + 1}")
            action + 1
        }
    }

```