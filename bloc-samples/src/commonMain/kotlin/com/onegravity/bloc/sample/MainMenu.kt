package com.onegravity.bloc.sample

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.blocState

object MainMenu {
    enum class ActionState {
        MainMenu,
        Counter1,
        Counter2,
        Books,
        Calculator,
        Posts
    }

    fun bloc(context: BlocContext) = bloc<ActionState, ActionState, ActionState, ActionState>(
        context,
        blocState(ActionState.MainMenu)
    ) {
        sideEffect { action }
        state { action }
        // does the same thing:
        // reduce { Effect(action, action) }
    }
}
