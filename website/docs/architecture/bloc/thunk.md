---
id: thunk
title: Thunk
sidebar_label: Thunk
hide_title: true
---

## Definition

To reiterate:
>The word "thunk" is a programming term that means "a piece of code that does some delayed work". Rather than execute some logic now, we can write a function body or code that can be used to perform the work later.  
https://redux.js.org/usage/writing-logic-thunks

While a Redux thunk is a function, dispatched as an action to a Redux store and processed by the redux-thunk middleware, a `Kotlin Bloc` thunk is not dispatched as an action but triggered the same way a reducer is triggered, by reacting to an `Action` that was sent to the `Bloc`. On top of that it's also:
1. a suspending function
2. actions are dispatched to the "next" thunk or reducer in the execution chain 

### Context

A thunk is called with a `ThunkContext` as receiver. The context is giving access to the current `State`, the `Action` that triggered the thunk's execution a `Dispatcher` and a function to "reduce" state directly:


```kotlin
public data class ThunkContext<State, Action>(
    val getState: GetState<State>,
    val action: Action,
    val dispatch: Dispatcher<Action>,
    val reduce: (proposal: Proposal) -> Unit
)
```

A typical thunk would evaluate the action, run some asynchronous operation and dispatch actions to be processed by other thunks or reducers:

```kotlin
thunk {
    if (action == Load) {
        dispatch(Loading)
        val books = repository.load()
        dispatch(LoadComplete(books))
    } else {
        // without this no action would reach the reducers 
        // below because this is a catch-all thunk
        dispatch(action)
    }
}

reduce<Loading> { 
    state.copy(loading = true)
}

reduce<LoadComplete> { 
    state.copy(loading = false, books = action.books)
}
```

:::tip
The catch-all `thunk { }` needs to call `dispatch(action)` explicitly or no reducers will be executed (see [Execution](#execution)). 
:::

In this case using a single action thunk would be simpler though and you'd write:

```kotlin
thunk<Load> {
    dispatch(Loading)
    val books = repository.load()
    dispatch(LoadComplete(books))
}
```

This doesn't require to call `dispatch(action)` explicitly since it only catches a single action (`Load`) and then dispatches its own actions.

:::tip
There are extension functions to launch `Coroutines` from a thunk (see [Coroutine Launcher](coroutine_launcher)). 
:::
## Thunk reduce()

Thunks are meant to run asynchronous code and reducers are meant to reduce state. In many cases however the reducers are very simple functions. In the example above we need to add a dedicated action `Loading`, dispatch that action in the thunk in order for a reducer to reduce the current state to one that indicates loading. While that "separation of concerns" is useful in many cases, it adds a good amount of boilerplate code for simple cases like the one we have here. To simplify this we can use the `ThunkContext.reduce` function:
```kotlin
thunk<Load> {
    reduce( getState().copy(loading = true) )

    val books = repository.load()
    
    reduce( getState().copy(loading = false, books = books) )
}
```

`ThunkContext.reduce()` is identical to submitting an action that triggers a reducer except that the reducer is implicitly defined as:
```kotlin
reduce {
    Effect(proposal, emptyList())
}
```

## Execution

A lot of what was said in the [reducer documentation](reducer#execution) applies to `thunks` too but there are important differences. Here are the rules:
1. If there's more than one reducer matching an action, only the first one will be executed (can't reduce the same state twice).  
**Rule 1**: for thunks, **every matching thunk will be executed**.
2. Reducers are executed by matching action, the order of declaration only matters when there's more than one matching reducer.  
**Rule 2**: for thunks, **the order of declaration is crucial** in determining which one executes first especially because every matching thunk will be executed (rule 1).
3. **Rule 3**: when a thunk dispatches an action, it's dispatched to all matching thunks declared after the dispatching thunk (or we would end up with an endless loop). If no matching thunk is found, the action is dispatched to the first matching reducer.

### Example 1

An example of rule 1 and 2:

```kotlin
thunk<Increment> {
    println("thunk 1")
}

thunk<Increment> {
    println("thunk 2")
}

thunk {
    println("thunk 3")
}

reduce {
    println("reducer")
    state
}
```

Above thunks will all be executed in the order of their declaration (sequentially). The output will be:
>thunk 1  
thunk 2  
thunk 3

The reducer won't be executed as none of the thunks dispatches any action. 

### Example 2

Try to guess what happens when the first thunk dispatches an `Increment` action (all three rules apply):

```kotlin
thunk<Increment> {
    println("thunk 1")
    dispatch(Increment)
}

thunk<Increment> {
    println("thunk 2")
}

thunk {
    println("thunk 3")
}

reduce {
    println("reducer")
    state
}
```

The output will be:
>thunk 1  
thunk 2  
thunk 3  
thunk 2  
thunk 3

- the original `Increment` action is dispatched to the first thunk  
  -> outputs: `thunk 1`
- the first thunk dispatches a second `Increment` action to all matching thunks declared after the dispatching thunk (rule 3)
- thunk 2 is the first to process the second `Increment` action  
  -> outputs: `thunk 2`
- thunk 3 is next in line to process the second `Increment` action  
  -> outputs: `thunk 3`
- the original `Increment` action is dispatched to the second thunk  
  -> outputs: `thunk 2`
- the original `Increment` action is dispatched to the third thunk    
  -> outputs: `thunk 3`
- nothing reaches the reducer

While this sounds complicated, the rules are pretty straight forward. Examples like the one above are also rather academic and have little relevance for real applications. 
 
### Example 3

Here's a more realistic example:


```kotlin
thunk<LoadUser> {
    dispatch(UserLoading)
    val result = repo.loadUser()
    dispatch(UserLoaded(result))
}

thunk<UserLoading> {
    dispatch(AccountLoading)
    val result = repo.loadAccount()
    dispatch(AccountLoaded(result))
}

reduce<UserLoading> {
    // this won't be executed because the second thunk captures the UserLoading action
}

reduce<AccountLoading> {
    state.copy(loading = true)
}

reduce<UserLoaded> {
    state.copy(user = action.result)
}

reduce<AccountLoaded> {
    state.copy(loading = false, account = action.result)
}
```