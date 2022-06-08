---
id: android_subscription
title: Android Subscription
sidebar_label: Subscription
hide_title: true
---

## Introduction

Under "subscription" we subsume all operations that observe a bloc's `State` and `SideEffect`s. Because there are a couple of different ways to implement the `View` on Android, there are also a couple of different ways to "subscribe" to a bloc.

## Data Binding

For [Data Binding](https://developer.android.com/topic/libraries/data-binding) there's an extension function eliminating some boilerplate code:

```kotlin
class CalculatorActivity : AppCompatActivity()

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.viewmodel = viewModel }
```

without a ViewModel:
```kotlin
class CalculatorActivity : AppCompatActivity()

    val bloc by getOrCreate { calculatorBloc(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.activity = this }
```

The `bind` function:
1. sets the Activity's content view to the given layout
2. binds the layout with a component (ViewModel or Activity in above examples)
3. sets the LifecycleOwner that should be used for observing changes of LiveData in the binding

Sending ui events to the bloc happens in the standard data binding way, e.g. in combination with a ViewModel:

```xml
<data>
    <variable
        name="viewmodel"
        type="com.onegravity.bloc.calculator.CalculatorViewModel" />
</data>

<Button
    android:id="@+id/button_clear"
    android:onClick="@{(view) -> viewmodel.onClick(view.id)}"/>
```

If no ViewModel is used (see [getOrCreate()](../android/bloc_context.md#activityfragment)):

```xml
<data>
    <variable
        name="activity"
        type="com.onegravity.bloc.calculator.CalculatorActivity" />
</data>
    
<Button
    android:id="@+id/button_clear"
    android:onClick="@{() -> activity.button(ActionEnum.Clear)}"/>
```

## Live Data

To observe [Live Data](https://developer.android.com/topic/libraries/architecture/livedata) in general and [Data Binding](https://developer.android.com/topic/libraries/data-binding) specifically, we can "convert" the bloc's `StateStream` to `LiveData`:

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

## Jetpack Compose

## Subscribe
