package com.onegravity.bloc.sample

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.blocState

object MainMenu {
    sealed class ActionState {
        object MainMenu : ActionState()
        object Counter1 : ActionState()
        object Counter2 : ActionState()
        object Books : ActionState()
        object Calculator : ActionState()
        object Posts : ActionState()
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
