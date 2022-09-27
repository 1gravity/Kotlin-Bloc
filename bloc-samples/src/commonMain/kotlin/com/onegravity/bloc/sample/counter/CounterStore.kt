package com.onegravity.bloc.sample.counter

import org.reduxkotlin.createThreadSafeStore

object CounterStore {
    // the Model is intentionally more complex than needed to show how a Bloc can interact with
    // a Redux Store that holds "other" data/state
    data class Purpose(val title: String, val description: String)
    data class Counter(val count: Int, val lastValue: Int)
    data class ReduxModel(val purpose: Purpose, val counter: Counter)

    // Actions
    sealed class ReduxAction {
        data class UpdateTitle(val value: String) : ReduxAction()
        data class UpdateDescription(val value: String) : ReduxAction()
        data class UpdateCount(val value: Int = 1) : ReduxAction()
    }

    // Reducers
    private fun purposeReducer(state: Purpose, action: Any) = when (action) {
        is ReduxAction.UpdateTitle -> state.copy(title = action.value)
        is ReduxAction.UpdateDescription -> state.copy(description = action.value)
        else -> state
    }

    private fun counterReducer(state: Counter, action: Any) = when (action) {
        is ReduxAction.UpdateCount -> Counter(action.value, state.count)
        else -> state
    }

    private fun rootReducer(state: ReduxModel, action: Any) = ReduxModel(
        purpose = purposeReducer(state.purpose, action),
        counter = counterReducer(state.counter, action)
    )

    // Redux Store
    internal val reduxStore = createThreadSafeStore(
        ::rootReducer,
        ReduxModel(
            purpose = Purpose(
                title = "Redux Test",
                description = "This model is used to demonstrate how Blocs can interact with parts of the state held by a Redux store"
            ),
            counter = Counter(1, 1)
        )
    )
}
