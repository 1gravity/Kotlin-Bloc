---
id: android_live_data
title: Android Live Data
sidebar_label: Live Data
hide_title: true
---

## Live Data

To observe [Live Data](https://developer.android.com/topic/libraries/architecture/livedata) in general and in combination with [Data Binding](./data_binding.md) specifically, we can "convert" the bloc's `StateStream` to `LiveData`:

```kotlin
class CalculatorViewModel : ViewModel() {
    val state = toLiveData(bloc)
```

In an Activity we would write:

```kotlin
class CalculatorActivity : AppCompatActivity() {

    val bloc by getOrCreate { calculatorBloc(it) }

    val state by lazy { toLiveData(bloc) }
```

Note that in Activities the initialization of the state property must be lazy because the bloc is initialized lazy too. ViewModels are created lazily already and there's no need to add another lazy initialization.


The `state` LiveData can be bound to the layout as usual:

```xml
<TextView
    android:text="@{viewmodel.state.toString()}"/>

<!-- or -->

<TextView
    android:text="@{activity.state.toString()}"/>
```