package com.onegravity.bloc.sample

import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.bloc
import com.onegravity.bloc.state.blocState

object MainMenu {
    enum class ActionState {
        MainMenu,
        MainMenuCompose,
        Counter,
        CounterCompose,
        CounterRedux,
        Books,
        Calculator,
        CalculatorNoVM,
        Posts,
        PostsCompose,
        ToDo
    }

    fun bloc(context: BlocContext) = bloc<ActionState, ActionState, ActionState>(
        context,
        blocState(ActionState.MainMenu)
    ) {
        sideEffect { action }   // the navigation happens as side effect
        reduce { action }        // not really necessary
        // does the same thing:
        // reduce { Effect(action, action) }
    }
}
