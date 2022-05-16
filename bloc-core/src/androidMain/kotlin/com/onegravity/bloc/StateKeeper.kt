package com.onegravity.bloc

import android.os.Bundle
import androidx.savedstate.SavedStateRegistry
import com.arkivanov.essenty.statekeeper.StateKeeper
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import com.onegravity.bloc.utils.logger

private const val KEY_STATE = "STATE_KEEPER_STATE"

/**
 * This is taken from Essenty to work around an IllegalArgumentException:
 * https://github.com/arkivanov/Essenty/issues/48
 */
internal fun stateKeeper(
    savedStateRegistry: SavedStateRegistry,
    isSavingAllowed: () -> Boolean = { true }
): StateKeeper {
    val dispatcher = StateKeeperDispatcher(savedStateRegistry.consumeRestoredStateForKey(KEY_STATE)?.getParcelable(
        KEY_STATE
    ))

    try {
        savedStateRegistry.registerSavedStateProvider(KEY_STATE) {
            Bundle().apply {
                if (isSavingAllowed()) {
                    putParcelable(KEY_STATE, dispatcher.save())
                }
            }
        }
    } catch (e: IllegalArgumentException) {
        logger.i("SavedStateRegistry.SavedStateProvider already registered -> it's fine!")
    }

    return dispatcher
}
