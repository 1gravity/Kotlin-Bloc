---
id: counter
title: Counter
sidebar_label: Counter
hide_title: true
---

## Counter

The "Hello World" sample of UI frameworks is the counter app. There are three sample implementations in this framework that demonstrate:
- how to use a `Bloc` as `BlocState`
- how to use a Redux store as `BlocState`
- how to define a `Bloc` in the view itself (Android only)

### Counter 1

[SimpleCounter](https://github.com/1gravity/Kotlin-Bloc/blob/master/bloc-samples/src/commonMain/kotlin/com/onegravity/bloc/sample/counter/SimpleCounter.kt) demonstrates how a `Bloc` can be used as `BlocState` (see also [Bloc is a BlocState](../architecture/blocstate/bloc_state.md#bloc-isa-blocstate)) basically intercepting the "communication" between a `Bloc` and it's `BlocState`. 

All it takes to "convert" a `Bloc` to a `BlocState` is the extension function `asBlocState()`:

```kotlin
fun bloc(context: BlocContext) = bloc<Int, Action>(
  context,
  interceptorBloc1(context, 1).asBlocState()
) {
```

:::tip
Using a `Bloc` as `BlocState` is not recommended. All business logic related to a view component should be implemented in a single bloc. Even the `auditTrailBloc` in the example that just adds some logging, isn't a good example because an audit trail would typically be implemented at the source of truth and that's the `BlocState`, not some intercepting `Bloc`.
:::

### Counter 2

[ReduxCounter](https://github.com/1gravity/Kotlin-Bloc/blob/master/bloc-samples/src/commonMain/kotlin/com/onegravity/bloc/sample/counter/ReduxCounter.kt) demonstrates how a `Bloc` connects to a Redux store as it's `BlocState`. The store intentionally has a more complex model than what we need for a simple counter to demonstrate how to compose reducers for different `Blocs` and how to select sub state:

```kotlin
// the model
data class Purpose(val title: String, val description: String)
data class Counter(val count: Int, val lastValue: Int)
data class ReduxModel(val purpose: Purpose, val counter: Counter)
```

```kotlin
// reducers
private fun purposeReducer(state: Purpose, action: Any) = when (action) {
    is ReduxAction.UpdateTitle -> state.copy(title = action.value)
    is ReduxAction.UpdateDescription -> state.copy(description = action.value)
    else -> state
}

private fun counterReducer(state: CounterStore.Counter, action: Any) = when (action) {
    is ReduxAction.UpdateCount -> Counter(action.value, state.count)
    else -> state
}

private fun rootReducer(state: CounterStore.ReduxModel, action: Any) = ReduxModel(
    purpose = purposeReducer(state.purpose, action),
    counter = counterReducer(state.counter, action)
)
```

```kotlin
// select sub state
reduxStore.toBlocState(
  context = context,
  select = { reduxModel ->  reduxModel.counter },
  map = { model -> model.count }
)
```

### Counter 3

The [third implementation](https://github.com/1gravity/Kotlin-Bloc/blob/master/androidApp/src/main/kotlin/com/onegravity/bloc/counter/CounterActivityCompose.kt) is Android only and moves the "business logic" right into the `View` / `Activity`:

```kotlin
class CounterActivityCompose : AppCompatActivity() {

    private val bloc by getOrCreate { bloc<Int, Int>(it, 1) { reduce { state + action } } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            // observe State
            val state by bloc.observeState()

            // display State
            Text(text = stringResource(R.string.counter_value, state))
            
            // send Actions to the Bloc
            Button(
              onClick = { bloc.send(1) },
              content = { Text(text = "Increment") }
            )
            Button(
              onClick = { bloc.send(-1) },
              content = { Text(text = "Decrement") }
            )
        }
    }
}
```

:::tip
Typically the business logic is in a shared component so it can be used in Android and iOS. 
:::