package com.onegravity.bloc.sample

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.context.BlocContext

class MainMenu4iOS(context: BlocContext) :
    Bloc<MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState, MainMenu.ActionState>
    by MainMenu.bloc(context)
