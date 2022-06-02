---
id: reducer
title: Reducer
sidebar_label: Reducer
hide_title: true
---

## Definition

To reiterate:
> A reducer is a function that receives the current state and an action object, decides how to update the state if necessary, and returns the new state: (state, action) => newState  
(https://redux.js.org/tutorials/fundamentals/part-2-concepts-data-flow)

While the offical Redux reducer definition captures the essence, reducers in the context of `Kotlin BLoC` are a bit more complex: 
```kotlin
suspend (State, Action, CoroutineScope) -> Proposal
```
Compared to a Redux reducer, "our" reducer is:
1. suspending
2. takes a CoroutineScope as parameter (on top of the `State` and the `Action`)
3. returns a `Proposal` instead of `State`

### Context

A reducer is called with a `ReducerContext` as receiver. The context giving access to the current `State`, the `Action` that triggered the reducer's execution and a CoroutineScope:


```kotlin
public data class ReducerContext<State, Action>(
    val state: State,
    val action: Action,
    val coroutineScope: CoroutineScope
)
```

A typical reducer would evaluate the action and apply some transformation to the immutable state like this:

```kotlin
reduce {
    when (action) {
        is PostLoading -> state.copy(loading = true)
        is PostLoaded -> state.copy(loading = false, post = action.result)
    }
}
```

The `CoroutineScope` is only used in the [Redux Extension](../../extensions/redux).

### Side Effect

Reducers can have side effects. This idea and some of the syntax was inspired by the [Reduce](https://github.com/genaku/Reduce) library. Unlike with `Reduce`, the default for reducers with `Kotlin BLoC` is to return the new `State` instead of an `Effect` which is a class wrapping `State` and a list of side effects:

```kotlin
// no side effects
suspend (State, Action, CoroutineScope) -> Proposal
```

instead of:

```kotlin
// with side effects
suspend (State, Action, CoroutineScope) -> Effect<Proposal, SideEffect>

public data class Effect<Proposal : Any, SideEffect : Any>(
    val proposal: Proposal?,
    val sideEffects: List<SideEffect>
)
```

This decision was based on the observation that most reducers have no side effects and thus this case should be the primary one with the simplest syntax. To return side effects, there's a slightly more complex syntax:

```kotlin
data class State(val loading: Boolean = false, val post: Post? = null)

sealed class Action
object Clicked : Action()

sealed class SideEffect
object Open : SideEffect()
object LogEvent : SideEffect()

fun bloc(context: BlocContext) =
    bloc<State, Action, SideEffect, State>(context, State()) {
        // reducer without side effects
        reduce<Clicked> {
            state.copy(loading = true)
        }

        // reducer with two side effects
        reduceAnd<Clicked> {
            state.copy(loading = true) and Open and LogEvent
        }

        // reducer without side effects
        reduceAnd<Clicked> {
            state.copy(loading = true).noSideEffect()
        }

        // no state reduction, only one side effect
        sideEffect<Clicked> { 
            Open
        }        
    }
```

`reduceAnd` is the builder function to use if a reducer has side effects. The `and` operator can be used to combine state and side effects into an `Effect` instead of instantiating the object directly. The `Reduce` library uses the `+` operator instead of `and` which makes for some neat syntax but also interferres with mathematical operations.

## Execution

When an `Action` is sent to the bloc, it will determine whether there are matching thunks. If there are, it will be put into the thunk queue. If there are no matching thunks but matching reducers, the action will be put into the reducer queue. The queues are [Channels](https://kotlinlang.org/docs/channels.html) which are processed asynchronously.

If there's a matching thunk and a matching reducer, only the thunk will be executed like in this example:

```kotlin
thunk<Increment> {
    // thunk code will be executed
}

reduce<Increment> {
    // reduce code won't be executed
}
```

This behavior makes sense because thunks are like Redux thunk-middleware. They execute before the reducers do. They can however dispatch actions to the "next" thunk or reducer. In this example the reducer is called when the thunk dispatches the `Increment` action.

```kotlin
thunk<Increment> {
    // do asynchronous thunk stuff
    dispatch(Increment)
}

reduce<Increment> {
    // reduce code will be executed
}
```

What happens if there are two matching reducers?

```kotlin
reduce<Increment> {
    // will be executed
}

reduce<Increment> {
    // won't be executed
}

reduce {
    // won't be executed
}
```

This bevavior also makes sense since state must be reduced only once. Whichever matching reducer is defined first is the one being called. While the order of definition is relevant for reducers (if they match the same action), it's not for thunks and reducers, thunks will always be executed first.

What about side effects?

```kotlin
reduce<Increment> {
    // will be executed
}

sideEffect<Increment> {
    // will also be executed
}

sideEffect<Increment> {
    // this one too will be executed
}

reduceAnd<Increment> {
    // won't be executed
}
```

Since "pure" side effects functions (`sideEffect { }` block) aren't reducing state, they will always be executed.

## Matching

So what's a match?

In the case of reducers, we have a match if:
1. a reducer doesn't define an action explicitly (catch all reducer) 
2. a reducer defines an action identical to the sent action (`KClass.isInstance`)

### Without Action

A reducer not defining an action explicitly is like a Redux reducer, a catch-all function to process a bunch of actions typically using a when statement like this:
```kotlin
reduce {
    when (action) {
        is Increment -> state + 1
        is Decrement -> state - 1
    }
}
```

### With Action

A reducer defining an action will be executed only if the sent action matches the reducer's action. 
There are two different action types requiring a different syntax:

If an action is a `class`/`object` or `data class`, the syntax is:
```kotlin
reduce<Increment> { state + 1 }

reduce<Decrement> { state - 1 }

// Action definition
sealed class Action
object Increment : Action()
object Decrement : Action()
```

If an action is an `enum class`, the syntax is:
```kotlin
reduce(Action.Increment) { state + 1 }

reduce(Action.Decrement) { state - 1 }

// Action definition
enum class Action { 
    Increment, 
    Decrement 
}
```

## A Matter of Taste

Reducers can be catch all reducers or they can be single action reducers (we can also use a combination of the two styles).

Catch all reducers can make sense to have all the reducer logic in one place like in this calculator example:

```kotlin
reduce {
    try {
        val newState = state.resetErrors()
        when (action) {
            Clear -> CalculatorState()
            Equals -> newState.equals()
            Add -> newState.apply(Operator.Add)
            Subtract -> newState.apply(Operator.Subtract)
            Multiply -> newState.apply(Operator.Multiply)
            Divide -> newState.apply(Operator.Divide)
            PlusMinus -> newState.plusMinus()
            Percentage -> newState.percentage()
            Period -> newState.period()
            is Digit -> newState.digit(action.digit)
        }
    } catch (ex: Exception) {
        CalculatorState.error(ex.message)
    }
}
```

Reducers per action can make sense if the reducer logic is more complex like in this example:

```kotlin
reduce<Add> {
    with (state.resetErrors()) {
        runCatching {
            val state = if (register1.isNotEmpty() && register2.isNotEmpty()) equals() else this
            if (state.register1.isEmpty())
                state
            else state.copy(
                register1 = Register(),
                register2 = if (state.register1.isNotEmpty()) state.register1 else state.register2,
                operator = op
            )
        }.mapToState()
    }
}

reduce<Equals> {
    with (state.resetErrors()) {
        runCatching {
            if (register2.isEmpty()) return this
            val reg1 = when (operator) {
                Operator.Add -> register2 + register1
                Operator.Subtract -> register2 - register1
                Operator.Multiply -> register2 * register1
                Operator.Divide -> register2 / register1
                else -> register1
            }
            copy(register1 = reg1, register2 = Register(), operator = null)
        }.mapToState()
    }
}
```

It's a "matter of taste", which style you prefer. While the traditional/Redux style is to have reducers dealing with different actions, [this article](https://dev.to/feresr/a-case-against-the-mvi-architecture-pattern-1add) advocates for splitting reducers into smaller chunks. 

[Orbit](https://orbit-mvi.org/) is one of the frameworks championing the MVVM+ style (that's what they call it in [this article](https://appmattus.medium.com/top-android-mvi-libraries-in-2021-de1afe890f27)) serving as inspiration for this part of the `Kotlin BLoC` framework.
