---
id: ios_bloc_component
title: Bloc Component
sidebar_label: Bloc Component
hide_title: true
---

## BlocComponent

The [BlocComponent](https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocComponent.swift) class is very similar to `BlocHolder`. The only difference is that the latter always wraps a `Bloc` while the former wraps an arbitrary component that needs a `BlocContext` to be instantiated, e.g.:

```swift
struct PostListView: View {
    private let component = BlocComponent<PostsComponent> { PostsComponentImpl(context: $0) }
```

```kotlin
class PostsComponentImpl(context: BlocContext) {

    val bloc by lazy {
        bloc<PostsRootState, PostsAction>(context, blocState) {
            // build the bloc
        }
    }
```
:::tip
In both cases the generic types will be preserved.
In the case of a `BlocHolder`, the `BlocHolder` itself declares the generic types (and also the bloc it wraps).
In the case of a `BlocComponent`, the wrapped component or rather the bloc it contains declares the generic types.
:::
