---
id: bloc_state
title: Bloc State
sidebar_label: Overview
hide_title: true
---

## Definition

A `BlocState` is the actual keeper of `State`, a source of asynchronous state data (`StateStream`) and a `Sink` for `Proposals` to (potentially) alter its state.

## Public Interface

A `BlocState` implements two public facing functions.

### State Stream

The `StateStream` is a stream to observe `State`. It's identical to [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) except it doesn't expose the `replayCache`.

```kotlin
public val value: State
public suspend fun collect(collector: FlowCollector<State>)
```

While `value` is used to retrieve the current `State`, the `collect` function is used to collect the flow of `States` emitted by the `BlocState`. 

A `StateStream` emits:
- no duplicate values
- an initial value upon subscription (analogous `BehaviorSubject`)
wrappers that make observing blocs very easy -> [Extensions](../extensions/overview).

### Sink

A sink to send data / `Proposals` to the `BlocState`:
```kotlin
public fun send(proposal: Proposal)
```

As mentioned in the [Design Overview](../architecture.md#design-overview), reducers don't return `State` but a `Proposal`, a concept inspired by the [SAM pattern](https://sam.js.org/).  `Proposals` increase the level of decoupling between `Bloc` and `BlocState` to support a number of use cases:
- a `BlocState]` can enforce domain specific rules like validation or enrichment
- connect a `Bloc` to a [Redux Store](../../extensions/redux/redux_motivation)
- use `Blocs` as `BlocsState` (see [Bloc isA BlocState](../blocstate/bloc_state.md#bloc-isa-blocstate))


## Separation of Concerns

A `Bloc` doesn't store the state itself but delegates to a `BlocState` to separate the two concerns:
1. business logic
2. storing state 

We can easily change a `BlocState` to modify the behavior of the component. Take e.g. the ToDo sample app. The bloc is currently defined like this:

```kotlin
fun toDoBloc(context: BlocContext) = bloc<List<ToDo>, ToDoAction>(
    context = context,
    blocState = PersistingToDoState(CoroutineScope(SupervisorJob())) 
) {

```

`PersistingToDoState` is, as the name implies, storing to do data persistently. Changing one line of code can change that behavior:

```kotlin
fun toDoBloc(context: BlocContext) = bloc<List<ToDo>, ToDoAction>(
    context = context,
    blocState = blocState(emptyList())     
) {
```

Apart from the clear separation of concerns, using `BlocStates` has many advantages:
- we can share state between business logic components
- we can persist state (database, network)
- we can add domain rules to the actual state container (validation, enrichment)
- we can use a global state container like a Redux store instead of individual `BlocState` containers (compare [Redux](../../extensions/redux/redux_motivation))


## Bloc isA BlocState

The attentive reader will have noticed that `Blocs` and `BlocStates` have a very similar public interface:

![Bloc Architecture - Overview](../../../static/img/BLoC%20Architecture%20-%20BLoC%20Overview.svg)

The only difference between a `Bloc` and a `BlocState` is their intended purpose and the `Bloc's` `SideEffectStream`. As a matter of fact a `Bloc` is also a `BlocState`:

```kotlin
class Bloc<out State : Any, in Action : Any, SideEffect : Any> : BlocState<State, Action>() {
```

Given that, it's easy to use a `Bloc` as `BlocState` provided we treat the `Proposal` output of one `Bloc` as `Action` for the next `Bloc` like in this example:

```kotlin
fun <State: Any> auditTrailBloc(context: BlocContext, initialValue: State) = bloc<State, State>(
    context,
    initialValue
) {
    thunk {
        logger.d("auditTrailBloc: changing state from ${getState()} to $action")
        // here we would write the changes to a db or send it to the backend
        dispatch(action)
    }
    reduce { action }
 }
```

Above example illustrates how to define a reusable `Bloc` intercepting the `Proposals` from one `Bloc`, sending it to the actual `BlocState` (which is created automatically by the [BlocBuilder](../bloc/bloc_builder)) and triggering an asynchronous operations. To use this `Bloc` as `BlocState`, we have an extension `asBlocState()` function:

```kotlin
fun bloc(context: BlocContext) = bloc<State, Action>(
    context,
    auditTrailBloc(context, initialState).asBlocState()
) {
    ...
}
```