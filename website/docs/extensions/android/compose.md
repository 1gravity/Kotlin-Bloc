---
id: android_compose
title: Jetpack Compose
sidebar_label: Compose
hide_title: true
---

## Dependency

To use the [Jetpack Compose](https://developer.android.com/jetpack/compose) extensions please add the `bloc-compose` artifact as a dependency in the Gradle build file:

```kotlin
implementation("com.1gravity:bloc-compose:<version>")
```

## observeState

Observing a bloc's state in a [Composable](https://developer.android.com/reference/kotlin/androidx/compose/runtime/Composable) is very easy due to the `observeState` extensions functions:


```kotlin
@Composable
fun MyComposable(bloc: MyBloc)
    val state by bloc.observeState()
```

The `state` property above is of type [State](https://developer.android.com/reference/kotlin/androidx/compose/runtime/State) so state changes will trigger the `Composable` to recompose.

Here's a more comprehensive example:

```kotlin
@Composable
fun ToDo(bloc: Bloc<List<ToDo>, ToDoAction, Unit>) {
    val state: List<ToDo> by bloc.observeState()

    var text: String by rememberSaveable { mutableStateOf("") }

    Column {
        TextField(
            value = text,
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    // when we hit the enter key, add a new todo item to our list
                    bloc.send(AddToDo(text))
                    text = ""
                },
            )
        )

        LazyColumn {
            // the list of todo items will update automatically when items are 
            // added, removed or modified
            items(state) { todo ->
                Row {
                    Checkbox(
                        // updates automatically when the item's completion status changes
                        checked = todo.completed,
                        onCheckedChange = { 
                            // this will modify the todo item's completion status
                            bloc.send(ToggleToDo(todo.uuid)) 
                        }
                    )
                    Text(text = todo.description)
                }
            }
        }
    }
}
```
:::tip
`observeState` is available for [Bloc](../../architecture/bloc/bloc.md), [BlowOwner](../../architecture//blocowner/bloc_owner.md), [BlowObservable](../../architecture//blocowner/bloc_observable.md#blocobservable) and [BlocObservableOwner](../../architecture//blocowner/bloc_observable.md#blocobservableowner).
:::

## observeSideEffects

`observeSideEffects` is the equivalent of `observeState` but for side effects:

```kotlin
@Composable
internal fun MenuEntries(bloc: MenuBloc) {
    val sideEffect by bloc.observeSideEffects()

    sideEffect?.let { menuEntry ->
        val context = LocalContext.current
        val intent = Intent(context, menuItem2Activity[menuEntry])
        context.startActivity(intent)
    }
```

:::tip
`observeSideEffects` is available for [Bloc](../../architecture/bloc/bloc.md), [BlowOwner](../../architecture//blocowner/bloc_owner.md), [BlowObservable](../../architecture//blocowner/bloc_observable.md#blocobservable) and [BlocObservableOwner](../../architecture//blocowner/bloc_observable.md#blocobservableowner).
:::

## Composable Preview

To create [Composable Previews](https://developer.android.com/jetpack/compose/tooling) use the `previewBlocContext()` function that creates a [BlocContext](../../architecture/bloc/bloc_context.md) which is required to create a `Bloc`:

```kotlin
@Preview
@Composable
fun MenuEntriesPreview() {
    val bloc = MainMenuCompose.bloc(previewBlocContext())
    MenuEntries(bloc, Modifier.fillMaxWidth().fillMaxHeight())
}
```

:::tip
`previewBlocContext()` creates a [Lifecycle](../..//architecture/bloc/lifecycle.md) tied to the lifecycle of the `Composable` so it's shorter lived than the "real" lifecycle but it's good enough for a preview.
:::
