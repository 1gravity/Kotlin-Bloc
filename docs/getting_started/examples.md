---
id: examples
title: Examples
sidebar_label: Examples

hide_title: true
---

## Counter

The "Hello World" example of UI frameworks is the counter app. Creating the "business logic" part of such an app is incredibly simple with `Kotlin Bloc`:

```kotlin
fun bloc(context: BlocContext) = bloc<Int, Int>(context, 1) {
    reduce { state + action }
}
```

The view part is very simple too.

### Android

```kotlin
class CounterActivity : AppCompatActivity() {

    // create or retrieve the lifecycle aware Bloc
    private val bloc by getOrCreate { bloc(it) }
```

```kotlin
setContent {
    // observe the Bloc state
    val state by bloc.observeState()

    // updates on state / count changes
    Text("Counter: $state")

    // emit events / actions to update the state / count
    Button(onClick = { bloc.send(1) }, content = { Text("Increment") })
    Button(onClick = { bloc.send(-1) }, content = { Text("Decrement") })
}
```

This is very little code considering the fact that the Bloc is lifecycle aware and will survive configuration changes.

### iOS

On iOS there's a bit more boilerplate code ([BlocHolder](https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocHolder.swift) and [BlocObserver](https://github.com/1gravity/Kotlin-Bloc/blob/master/iosApp/iosApp/utils/BlocObserver.swift) are omitted here) but it's still pretty "lean":

```swift
// iOS
struct CounterView: View {
    // create the lifecycle aware Bloc
    private let holder = BlocHolder { CounterKt.bloc(context: $0) }
    
    @ObservedObject
    private var model: BlocObserver<KotlinInt, KotlinInt, KotlinUnit>

    init() {
        // observe the Bloc state
        model = BlocObserver(holder.value)
    }
```
```swift
var body: some View {
    return VStack() {    
        // updates on state / count changes
        Text("Counter \(model.value)")
    
        // emit events / actions to update the state / count
        Button(
            action: { holder.value.send(value:  1) },
            label: { Text("Increment") }
        )
        Button(
            action: { holder.value.send(value: -1) },
            label: { Text("Decrement") }
        )
```

### Single-Action Reducer

`Kotlin Bloc` supports different MVI/MVVM "styles" and above example shows one of many ways to implement the counter app. Here are some alternative approaches to implementing the bloc:

```kotlin
sealed class Action
object Increment : Action()
object Decrement : Action()

fun bloc(context: BlocContext) = bloc<Int, Action>(context, 1) {
    reduce<Increment> { state + 1 }
    reduce<Decrement> { state - 1 }
}
```

### Catch-all Reducer

```kotlin
fun bloc(context: BlocContext) = bloc<Int, Action>(context, 1) {
    reduce {
        when (action) {
            Increment -> state + 1
            Decrement -> state - 1
        }
    }
}
```

### Enums

```kotlin
enum class Action { Increment, Decrement }

fun bloc(context: BlocContext) = bloc<Int, Action>(context, 1) {
    reduce(Increment) { state + 1 }
    reduce(Decrement) { state - 1 }
}
```

### MVVM+ / Orbit Style

```kotlin
class CounterActivityCompose : AppCompatActivity(), BlocOwner<Int, Action, Unit, Int> {

    // create or retrieve the lifecycle aware Bloc
    override val bloc by getOrCreate { bloc<Int, Action>(it, 1) }

    private fun increment() = reduce { state + 1 }

    private fun decrement() = reduce { state - 1 }
```
```kotlin
setContent {
    // observe the Bloc state
    val state by bloc.observeState()

    // updates on state / count changes
    Text("Counter: $state")

    // emit events / actions to update the state / count
    Button(onClick = { increment() }, content = { Text("Increment") })
    Button(onClick = { decrement() }, content = { Text("Decrement") })
}
```

:::tip
With `Kotlin Bloc` there's no need for an Android ViewModel which only adds unnecessary boilerplate code (see [BlocOwner](../architecture/blocowner/bloc_owner.md#blocowner)).
:::

## Post List

The following (artificial) example gives a more comprehensive overview of the different `Kotlin Bloc` functions:
- single-action + catch-all reducer
- single-action and catch-all side effects
- reducer with side effects
- thunks
- initializer
- Redux and MVVM+ style


```kotlin
sealed class Action
object Loading : Action()
data class Loaded(val posts: List<Post>) : Action()

sealed class SideEffect
data class OpenPost(val post: Post) : SideEffect()
object PostsLoaded : SideEffect()
object NOP : SideEffect()

data class Post(
    val id: Int,
    val title: String,
    val body: String,
)

data class State(
    val loading: Boolean = false,
    val posts: List<Post> = emptyList(),
)

class PostViewModel : ViewModel(), BlocOwner<State, Action, SideEffect, State> {

    override val bloc = bloc<State, Action, SideEffect, State>(
        blocContext(), State(false)
    ) {
        // initializer
        onCreate {
            Log.i("bloc", "Bloc is starting")
        }

        // NOP thunk
        thunk {
            Log.i("bloc", "current state: ${getState()}")
            dispatch(action)
        }

        // single-action reducer
        reduce<Loading> {
            state.copy(loading = true)
        }

        // single-action reducer with side effect
        reduceAnd<Loaded> {
            state.copy(loading = false, posts = state.posts) and PostsLoaded
        }

        // catch-all reducer with side effect
        reduceAnd {
            when (action) {
                Loading -> state.copy(loading = true).noSideEffect()
                is Loaded -> state.copy(loading = false, posts = state.posts) and PostsLoaded
            }
        }

        // single-action side effect
        sideEffect<Loaded> { PostsLoaded }

        // catch-all side effect
        sideEffect {
            when (action) {
                Loading -> NOP
                is Loaded -> PostsLoaded
            }
        }
    }

    init {
        // initializer, MVVM+ style
        onCreate {
            if (state.posts.isEmpty()) {
                load()
            }
        }
    }

    // thunks for asynchronous operations, MVVM+ style
    private fun load() = thunk {
        dispatch(Loading)
        // load the posts asynchronously
        val posts: List<Post> = repository.loadPosts()
        dispatch(Loaded(posts))
    }

    // side effects, MVVM+ style
    fun onPostClicked(post: Post) = sideEffect {
        OpenPost(post)
    }

}
```