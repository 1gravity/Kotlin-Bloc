package com.onegravity.bloc.sample

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.MainMenuCompose.State
import com.onegravity.bloc.sample.MainMenuCompose.MenuEntry

typealias MenuBloc = Bloc<State, MenuEntry, MenuEntry, State>

object MainMenuCompose {

    enum class MenuEntry {
        MainMenu,
        Counter,
        CounterRedux,
        Books,
        Calculator,
        CalculatorNoViewModel,
        Posts,
        PostsCompose
    }

    @Suppress("ArrayInDataClass")
    data class State(val allMenus: Array<MenuEntry> = MenuEntry.values())

    /**
     * All this does is emit the selected menu item as side effect without state update.
     * Obviously this is just for demo purposes.
     */
    fun bloc(context: BlocContext) = bloc<State, MenuEntry, MenuEntry>(context, State()) {
        sideEffect { action }
    }

}
