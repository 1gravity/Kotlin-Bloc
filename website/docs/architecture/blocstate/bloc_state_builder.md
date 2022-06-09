---
id: bloc_state_builder
title: Bloc State Builder
sidebar_label: Bloc State Builder
hide_title: true
---

A `BlocState` can be defined using a `BlocStateBuilder`

```kotlin
blocState<CountState, CountState> {  
    initialState = CountState(1)
 
    accept { proposal, state ->
        // make a decision whether to accept or reject the proposal
        // return Null to reject the proposal
        // return the proposal to accept it (and update the state)
        proposal
    }
}
```

- `initialState` is obviously the initial state of the `BlocState` (and this of the `Bloc`)
- `accept` is the function that accepts/rejects a proposal and updates the state if it's accepted

`initialState` and `accept` are both mandatory parameters(unfortunately there's no compile time check for this).

Since `State` and `Proposal` are identical in above example, it can be simplified to (no more `accept` function either):

```kotlin
blocState(CountState(1))
```

These are relatively simple / default `BlocState` implementations.
To create your own `BlocState`, extend the `BlocStateBase` class which implements the `Sink` and the `StateStream` (which is good enough to implement the `BlocState` interface). 
Typically you'd want to change:
1. how `Proposals` are accepted/rejected
2. how/where `State` is stored
3. how/from where `State` is retrieved

An example of the first case is the [DefaultBlocState](https://github.com/1gravity/Kotlin-Bloc/blob/feature/documentation/bloc-core/src/commonMain/kotlin/com/onegravity/bloc/state/DefaultBlocState.kt) which is used by the `BlocBuilder` itself:

```kotlin
internal open class DefaultBlocState<State : Any, Proposal : Any>(
    initialState: State,
    private val accept: Acceptor<Proposal, State>,
) : BlocStateBase<State, Proposal>(initialState) {

    override fun send(proposal: Proposal) {
        accept(proposal, value)?.also { state.send(it) }
    }
    
}
```

All `DefaultBlocState` does is adding the accept/reject functionality.

An example of the second and third cases are the [PersistingToDoState](https://github.com/1gravity/Kotlin-Bloc/blob/feature/documentation/bloc-samples/src/commonMain/kotlin/com/onegravity/bloc/sample/todo/PersistingToDoState.kt) and the [ReduxBlocState](https://github.com/1gravity/Kotlin-Bloc/blob/feature/documentation/bloc-redux/src/commonMain/kotlin/com/onegravity/bloc/redux/ReduxBlocState.kt).

The former stores state in a local database and retrieves if from the same db:

```kotlin
// inject the database
private val dao = getKoinInstance<ToDoDao>()

// retrieve the db content and send it to the StateStream
init {
    coroutineScope.launch(Dispatchers.Default) {
        dao.getFlow().collect { state.send(it) }
    }
}

// send state to the database
override fun send(proposal: List<ToDo>) {
    proposal.forEach { newTodo ->
        val oldTodo = value.firstOrNull { it.uuid == newTodo.uuid }
        if (newTodo != oldTodo) {
            dao.upsert(newTodo.uuid, newTodo.description, newTodo.completed)
        }
    }
}
```

The latter stores state in a Redux store:

```kotlin
// subscribe to sub state from the Redux store and send it to the StateStream
init {
    // selectScoped will unsubscribe from the store automatically when the Bloc is destroyed
    // select is a memoized selector to subscribe to the store's sub state
    store.selectScoped(disposableScope = this, select = select) { model ->
        state.send(map(model))
    }
}

// send state to the Redux store
override fun send(proposal: Proposal) {
    store.dispatch(proposal)
}
```