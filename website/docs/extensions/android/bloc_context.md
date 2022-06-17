---
id: android_bloc_context
title: Android BlocContext
sidebar_label: BlocContext
hide_title: true
---

## Introduction

If you haven't read the chapter about [BlocContext](../../architecture/bloc/bloc_context) yet, you should do so. It's crucial to understanding how blocs are tied to the different Android lifecycles.

Creating a bloc always requires a `BlocContext`. On Android that process is rather complicated (fear not, that's why there are the extensions discussed here). The main part needed to create a `BlocContext` is the correct [Lifecycle](../../architecture/bloc/lifecycle).

## Lifecycle

As explained [here](../../architecture/bloc/lifecycle), there are three lifecycles. What we need for the `BlocContext` is an [Essenty Lifecycle](https://github.com/arkivanov/Essenty) which is the one determining when the bloc is created, started, stopped and destroyed.

The view lifecycle can be an Activity/Fragment lifecycle, a Composable lifecycle or something similar, depending on what tech is being used for the `View`. A bloc should not be tied to that lifecycle since it's too short lived and it would typically lose state upon configuration changes. 

On Android side we want a ViewModel "lifecycle" to be the one connecting the `View` lifecycle and the [Essenty Lifecycle](https://github.com/arkivanov/Essenty).

### ViewModel

In an Android ViewModel, use the `blocContext()` extension function to create a `BlocContext`. `blocContext()` creates a context that is tied to the ViewModel's "lifecycle":

```kotlin
class CounterViewModel : ViewModel() {

    private val bloc = bloc(blocContext())
```

Those familiar with the Android ViewModel know that those ViewModels have a really basic [lifecycle](https://developer.android.com/topic/libraries/architecture/viewmodel#lifecycle) and not one that can be retrieved like the Activity's lifecycle. Most frameworks solve that problem by providing a BaseViewModel class that uses the `onCleared()` function to create an artificial ViewModel lifecycle. That solution forces you to extend their BaseViewModel, an annoying solution in a language that doesn't support multiple inheritance. `Kotlin Bloc` offers a better solution using coroutines / [viewModelScope](https://developer.android.com/topic/libraries/architecture/coroutines#viewmodelscope) to tie into the ViewModel lifecycle so there's no need to extend a BaseViewModel.

### Activity/Fragment

Android ViewModels are great to retain state across configuration changes but if the business logic is implemented with a bloc in a platform agnostic way, they become just proxies between the `View` and the `Bloc`. Their only "added-value" would be to provide the [lifecycle](#lifecycle). Some frameworks solved this by providing a platform independent ViewModel implementation (like [KaMPKit](https://github.com/touchlab/KaMPKit/blob/main/docs/GENERAL_ARCHITECTURE.md)).  

`Kotlin Bloc` has a better solution:

```kotlin
class CounterActivity : AppCompatActivity() {

    private val bloc by getOrCreate { bloc(it) }
```

That small piece of code `getOrCreate { bloc(it) }` does a lot for you:

- it creates a `ViewModel` 
- it creates an [Essenty `Lifecycle`](https://github.com/arkivanov/Essenty) tied to the `ViewModel`'s lifecycle
- it creates an [Essenty `InstanceKeeper`](https://github.com/arkivanov/Essenty) tied to the `ViewModel`
- it creates a `BlocContext` using the [Essenty `Lifecycle`](https://github.com/arkivanov/Essenty)
- the `ViewModel` is stored in the Activity's [ViewModelStore](https://developer.android.com/reference/android/arch/lifecycle/ViewModelStore)
- it creates the `bloc` and stores it in the [Essenty `InstanceKeeper`](https://github.com/arkivanov/Essenty)
- it does all that lazily and only if the `ViewModel` isn't already in the `ViewModelStore`

Ultimately you get a `bloc` that is tied to a `ViewModel` (which is created under-the-hood) with a lifecycle that is determined by the `ViewModel`'s lifecycle

The expression's builder block receives `it` = `BlocContext` as argument and can use it to instantiate a bloc or some other component that needs a `BlocContext`. Here's another example:

```kotlin
class PostsActivity : AppCompatActivity() {
    
    private val component: PostsComponent by getOrCreate { PostsComponentImpl(it) }
```
or 

```kotlin
class BooksActivity : AppCompatActivity() {

    private val useCase: BooksUseCase by getOrCreate { BooksUseCaseImpl(it, BooksRepositoryImpl()) }
```

##### The Key

Some might have noticed that `getOrCreate()` has a optional `key` parameter. The `key` is needed to store/retrieve the component instantiated in the lambda / builder block and it's default value is `Component::class`. If the builder block returns a `Bloc` instance then the key value is `com.onegravity.bloc.Bloc`. That key ignores the bloc's generic types (which are erased at runtime) that make the bloc unique. If `getOrCreate()` is used to create/retrieve blocs with different generic types in the context of the same Activity, it would use the same key for different blocs which would lead to a `ClassCastException`.

In the [Posts sample app](https://github.com/1gravity/Kotlin-Bloc/tree/master/androidApp/src/main/kotlin/com/onegravity/bloc/posts), the two fragments use different blocs but obviously run in the context of the same Activity (and thus use the same `ViewModelStore`). 

Doing this would crash the app when the second bloc is created (it would find the `Posts` bloc object and return it because that one is stored under the `com.onegravity.bloc.Bloc` key):

```kotlin
// PostsFragment
val bloc: PostsBloc by getOrCreate { Posts.bloc(it) }

// PostFragment
val bloc: PostBloc by getOrCreate { Post.bloc(it) }
```

Providing a unique key solves the problem:

```kotlin
// PostsFragment
val bloc: PostsBloc by getOrCreate("posts") { Posts.bloc(it) }

// PostFragment
val bloc: PostBloc by getOrCreate("post") { Post.bloc(it) }
```