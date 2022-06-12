---
id: android_data_binding
title: Android Data Binding
sidebar_label: Data Binding
hide_title: true
---

## Data Binding

For [Data Binding](https://developer.android.com/topic/libraries/data-binding) there's an extension function eliminating some boilerplate code:

```kotlin
class CalculatorActivity : AppCompatActivity()

    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        
        // extension function to eliminate boilerplate code
        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.viewmodel = viewModel }
```

without a ViewModel:
```kotlin
class CalculatorActivity : AppCompatActivity()

    val bloc by getOrCreate { calculatorBloc(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        
        // extension function to eliminate boilerplate code
        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.activity = this }
```

The `bind` function:
1. sets the Activity's content view to the given layout
2. binds the layout with a component (ViewModel or Activity in above examples)
3. sets the LifecycleOwner that should be used for [observing changes of LiveData](./android_live_data) in the binding

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