---
id: android
title: Android Extensions
sidebar_label: Android
hide_title: true
---

## BlocContext

If you haven't read the chapter about [BlocContext](../architecture/bloc/bloc_context) yet, you should do so. It's crucial to understanding how blocs are tied to the different Android lifecycles.

Creating a bloc always requires a `BlocContext`. On Android that process is a rather complicated one (fear not, that's why we have the extensions discussed here). The two main parts we need to create a `BlocContext` are a [Lifecycle](../architecture/bloc/lifecycle) and an [InstanceKeeper](https://github.com/arkivanov/Essenty).

### Lifecycle

As explained [here](../architecture/bloc/lifecycle), there are three lifecycles. What we need for the `BlocContext` is an [Essenty Lifecycle](https://github.com/arkivanov/Essenty) which defines when the bloc is created, started, stopped and destroyed.

The view lifecycle can be an Activity/Fragment lifecycle, a Composable lifecycle or something similar, depending on what tech you're using for the `View`. A bloc should not be tied to that lifecycle since it's too short lived and we'd typically lose state upon configuration changes. 

On Android side we want a `ViewModel`'s "lifecycle" to be the one connecting the `view` lifecycle and the [Essenty Lifecycle](https://github.com/arkivanov/Essenty).

#### ViewModel

In a ViewModel we can use the `blocContext()` extension function to create a `BlocContext` using the ViewModel's "lifecycle":

```kotlin
class CounterViewModel(context: ActivityBlocContext) : ViewModel() {

    private val bloc = bloc(blocContext(context))
```

Note that `blocContext()` takes an `ActivityBlocContext` as parameters. That's because we also need an [InstanceKeeper](android#instancekeeper) to create the context.

If you're familiar with the Android ViewModel, you'll know that they have a really basic [lifecycle](https://developer.android.com/topic/libraries/architecture/viewmodel#lifecycle) and not one that can be retrieved easily (like the Activity's lifecycle). Most frameworks have solved that problem by providing a BaseViewModel class that uses the `onCleared()` function to create an artificial ViewModel lifecycle. That solution forces you to extend their BaseViewModel for each of your own ViewModels, an annoying solution in a language that doesn't support multiple inheritance. `Kotlin BLoC` offers a better solution using the `viewModelScope` and coroutines to tie into the ViewModel lifecycle without the need to extend some BaseViewModel.

### Activity/Fragment

Android ViewModels are great to retain state across configuration changes but if the business logic is implemented in a bloc in a platform agnostic way, they become simple proxies from/to the view and the bloc. Their only "added-value" would be to provide the lifecycle we need (see above). Some frameworks have solved this by providing a platform independent ViewModel implementation (e.g. [KaMPKit](https://github.com/touchlab/KaMPKit/blob/main/docs/GENERAL_ARCHITECTURE.md)). `Kotlin BLoC` has a better solution:

```kotlin
class CounterActivity : AppCompatActivity() {

    private val bloc by getOrCreate { bloc(it) }

```

`getOrCreate()`
### InstanceKeeper
