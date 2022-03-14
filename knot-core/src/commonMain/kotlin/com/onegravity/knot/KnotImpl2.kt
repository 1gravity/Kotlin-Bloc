package com.onegravity.knot

import co.touchlab.kermit.Logger
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.KnotState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext

class KnotImpl2<State, Action, Proposal: Any>(
    context: KnotContext,
    private val knotState: KnotState<State, Proposal>,
    private val reducer: Reducer2<State, Action, Proposal>,
    private val dispatcherReduce: CoroutineContext = Dispatchers.Default
) : Knot<State, Action, Proposal, Nothing> {

    private val actions = Channel<Action>(UNLIMITED)

    init {
        val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

        context.lifecycle.doOnCreate {
            Logger.withTag("knot").d("onCreate -> start Knot")
            start(coroutineScope)
        }
        context.lifecycle.doOnDestroy {
            Logger.withTag("knot").d("onDestroy -> stop Knot")
            coroutineScope.cancel("Stop Knot")
        }
    }

    override val value = knotState.value

    override fun emit(value: Action) {
        Logger.withTag("knot").d("emit $value")
        actions.trySend(value)
    }

    override suspend fun collect(collector: FlowCollector<State>) {
        knotState.collect(collector)
    }

    private fun start(coroutineScope: CoroutineScope) {
        val dispatcher: Dispatcher<Proposal> = { action -> knotState.emit(action) }

        coroutineScope.launch(dispatcherReduce) {
            for (action in actions) {
                Logger.withTag("knot").d("processing action $action")
                reducer.invoke(knotState.value, action, dispatcher)
            }
        }

    }

}
