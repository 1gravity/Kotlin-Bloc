---
id: android_bloccontext
title: Android BlocContext
sidebar_label: BlocContext
hide_title: true
---

## Introduction

If you haven't read the chapter about [BlocContext](../architecture/bloc/bloc_context) yet, you should do so. It's crucial to understanding how blocs are tied to the different Android lifecycles.

Creating a bloc always requires a `BlocContext`. On Android that process is a rather complicated one (fear not, that's why we have the extensions discussed here). The two main parts we need to create a `BlocContext` are a [Lifecycle](../architecture/bloc/lifecycle) and an [InstanceKeeper](https://github.com/arkivanov/Essenty).

## Lifecycle

As explained [here](../architecture/bloc/lifecycle), there are three lifecycles. What we need for the `BlocContext` is an [Essenty Lifecycle](https://github.com/arkivanov/Essenty) which defines when the bloc is created, started, stopped and destroyed.

The view lifecycle can be an Activity/Fragment lifecycle, a Composable lifecycle or something similar, depending on what tech you're using for the `View`. A bloc should not be tied to that lifecycle since it's too short lived and we'd typically lose state upon configuration changes. 

On Android side we want a `ViewModel`'s "lifecycle" to be the one connecting the `view` lifecycle and the [Essenty Lifecycle](https://github.com/arkivanov/Essenty).

### ViewModel

In a ViewModel we can use the `blocContext()` extension function to create a `BlocContext` which is using the ViewModel's "lifecycle" under the hood:

```kotlin
class CounterViewModel(context: ActivityBlocContext) : ViewModel() {

    private val bloc = bloc(blocContext(context))
```

Note that `blocContext()` takes an `ActivityBlocContext` as parameters. That's because we also need an `InstanceKeeper` to create the context (see [below](android#instancekeeper)).

If you're familiar with the Android ViewModel, you know that they have a really basic [lifecycle](https://developer.android.com/topic/libraries/architecture/viewmodel#lifecycle) and not one that can be retrieved like the Activity's lifecycle. Most frameworks have solved that problem by providing a BaseViewModel class that uses the `onCleared()` function to create an artificial ViewModel lifecycle. That solution forces you to extend their BaseViewModel, an annoying solution in a language that doesn't support multiple inheritance. `Kotlin BLoC` offers a better solution using the `viewModelScope` and coroutines to tie into the ViewModel lifecycle without the need to extend anything.

### Activity/Fragment

#### ActivityBlocContext



#### No ViewModel!

Android ViewModels are great to retain state across configuration changes but if the business logic is implemented in a bloc in a platform agnostic way, they become just proxies from/to the `View` and the `Bloc`. Their only "added-value" would be to provide the lifecycle we need (see above). Some frameworks have solved this by providing a platform independent ViewModel implementation (like [KaMPKit](https://github.com/touchlab/KaMPKit/blob/main/docs/GENERAL_ARCHITECTURE.md)). `Kotlin BLoC` has a better solution:

```kotlin
class CounterActivity : AppCompatActivity() {

    private val bloc by getOrCreate { bloc(it) }
```
That single line of code does a lot for you:
- it creates a `ViewModel` 
- it creates an Essenty `Lifecycle` tied to that `ViewModel`'s lifecycle
- it creates an Essenty `InstanceKeeper` also tied to the `ViewModel`
- it then creates a `BlocContext` using the `Lifecycle` and the `InstanceKeeper`
- it puts the `ViewModel` into the Activity's [ViewModelStore](https://developer.android.com/reference/android/arch/lifecycle/ViewModelStore)
- it does all that only if the `ViewModel` doesn't already exist in the `ViewModelStore` (that's why the function is called `getOrCreate()`)
- it does all that lazily because we cannot access the `ViewModelStore` before the Activity's `onCreate()` method is called

The result is that the builder block passed to `getOrCreate()` is called when the component is used the first time, which typically happens in one of the Activity lifecycle methods. `it` is the `BlocContext` passed as argument to that builder block. In above example the built component is a bloc but it can be any component that takes a `BlocContext` as parameter like:

```kotlin
class PostsActivity : AppCompatActivity() {
    
    private val component: PostsComponent by getOrCreate { PostsComponentImpl(it) }
```
or 

```kotlin
class BooksActivity : AppCompatActivity() {

    private val useCase: BooksUseCase by getOrCreate { BooksUseCaseImpl(it, BooksRepositoryImpl()) }
```


Some might have noticed that `getOrCreate()` has a optional `key` parameter. 


## InstanceKeeper
