---
id: todo
title: To Do
sidebar_label: To Do
hide_title: true
---

## To Do

The ToDo example's purpose is to demonstrate how state can be persisted. There's only an Android implementation because the ui part isn't very interesting.

### Bloc

The `Bloc` itself comes with no surprises and hence we won't discuss the details. The only part worth mentioning is the `Bloc` declaration:

```kotlin
fun toDoBloc(context: BlocContext) = bloc<List<ToDo>, ToDoAction>(
    context = context,
//    blocState = blocState(emptyList())                              // non persisting BlocState
    blocState = PersistingToDoState(CoroutineScope(SupervisorJob()))  // persisting BlocState
) {
```

As you can see, the `blocState` parameter is either a regular `BlocState` (using [BlocStateBuilder](../architecture/blocstate/bloc_state_builder.md)) or `PersistingToDoState` which, as the name implies, persists the state (using [SQLDelight](https://cashapp.github.io/sqldelight/)). Which one is used is completely transparent for the `Bloc`.

### BlocState

The `PersistingToDoState` class extends `BlocStateBase` which provides a full `BlocState` implementation minus the `send()` function. There are really just two parts to our `PersistingToDoState`:
1. the `send()` function which upserts the `Proposal` = a list of todo into the database if the todo has changed
2. the initializer bloc that connects to the database and subscribes to updates to the todo table and sends those updates to the `StateStream` (or rather `MutableStateStream`)
   
```kotlin
class PersistingToDoState(
    coroutineScope: CoroutineScope
) : BlocStateBase<List<ToDo>, List<ToDo>>(
    initialState = emptyList()
) {

    private val dao = getKoinInstance<ToDoDao>()

    init {
        coroutineScope.launch(Dispatchers.Default) {
            dao.getFlow().collect { state.send(it) }
        }
    }

    override fun send(proposal: List<ToDo>) {
        proposal.forEach { newTodo ->
            val oldTodo = value.firstOrNull { it.uuid == newTodo.uuid }
            if (newTodo != oldTodo) {
                dao.upsert(newTodo.uuid, newTodo.description, newTodo.completed)
            }
        }
    }
}
```
