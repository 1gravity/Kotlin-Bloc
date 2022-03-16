package com.onegravity.bloc.sample

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext

object MainMenu {
    sealed class Action {
        object Counter1 : Action()
        object Counter2 : Action()
        object Books : Action()
        object Traffic : Action()
    }

    sealed class State {
        object MainMenu : State()
        object ShowCounter1 : State()
        object ShowCounter2 : State()
        object ShowBooks : State()
        object ShowTraffic : State()
    }

    private val mapping = mapOf(
        Action.Counter1 to State.ShowCounter1,
        Action.Counter2 to State.ShowCounter2,
        Action.Books to State.ShowBooks,
        Action.Traffic to State.ShowTraffic,
    )

    fun bloc(context: BlocContext) =
        bloc<State, Action>(context, State.MainMenu) {
            reduce { _, action -> mapping[action] ?: State.MainMenu }
        }

}
