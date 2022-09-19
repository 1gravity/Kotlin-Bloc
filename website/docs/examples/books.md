---
id: books
title: Books
sidebar_label: Books
hide_title: true
---

## Books

The books sample apps (taken from [Reduce](https://github.com/genaku/Reduce)), demonstrates:
- how to share `BlocState` between `Blocs`
- how to run asynchronous code

### Share State

One advantage of separating the business logic component (`Bloc`) from the actual state holder (`BlocState`) is that the latter can be shared by multiple blocs.
The books example 

```kotlin
// define the shared state
private val commonState = blocState<BookState>(BookState.Empty)
```

```kotlin
// first Bloc to use the commonState
private val clearBloc = bloc<BookState, BookAction.Clear>(
  context, 
  commonState
) {
  // business logic
}
```

```kotlin
// second Bloc to use the commonState
private val loadBloc = bloc<BookState, BookAction>(
  context, 
  commonState
) {
  // business logic
}
```

:::tip
Since both blocs share the same state, only one of them needs to be observed by the view (they expose the same state):
```kotlin
val observable = loadBloc.toObservable()
```
:::

:::tip
If the blocs had side effects, they would not be shared and thus an observer needed to observe both blocs.
:::

### Asynchronous Code

The example uses a [Thunk](../architecture/bloc/thunk.md) to load the data "asynchronously" (the repository uses `delay()` to simulate an asynchronous call):

```kotlin
thunk<BookAction.Load> {
  dispatch(BookAction.Loading)
  val nextAction = repository.loadBooks().toAction()
  dispatch(nextAction)
}

reduce<BookAction.Loading> { BookState.Loading }
reduce<BookAction.LoadComplete> { action.result.toState() }
```

The code shows a typical pattern for thunks:
1. it dispatches a loading action which a reducer reduces to some "loading" state
2. it executes an asynchronous operation
3. it dispatches an action with the result of that asynchronous operation which will result in another state update by a reducer 

:::tip
Reducers are suspended functions as well and can launch coroutines (see [Coroutine Launcher](../architecture/bloc/coroutine_launcher)) -> reducers can theoretically run asynchronous code as well. While there's nothing preventing you from doing that, don't:
1. A thunk's purpose is to run asynchronous code, a reducer's purpose is to reduce state based on ui events. Trying to use a component for something they aren't built for, will inevitably lead to problems.
2. Reducers run sequentially. Consequentially this will update the state to `SomeState` first before processing `SomeAction`:
```kotlin
reduce {
  bloc.send(<SomeAction>)
  <SomeState>
}
```
3. Reducers expect `State` as return value. You can theoretically do something like this:
```kotlin
// use your own CoroutineScope here
withContext(coroutineScope.coroutineContext) {
  // do asynchronous stuff
  BookState.Loading
}
```
or:
```kotlin
// use your own CoroutineScope here
coroutineScope.async {
 // do asynchronous stuff
 BookState.Loading
}.await()
```
The ui however is waiting for a state update in response to some user interaction (responsiveness) -> this is an anti-pattern.
:::