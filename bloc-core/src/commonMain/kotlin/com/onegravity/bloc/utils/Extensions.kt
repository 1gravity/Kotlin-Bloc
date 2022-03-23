package com.onegravity.bloc.utils

import com.arkivanov.essenty.lifecycle.*
import com.badoo.reaktive.disposable.scope.DisposableScope
import com.badoo.reaktive.disposable.scope.doOnDispose
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.select.select
import com.onegravity.bloc.state.reduxBlocState
import kotlinx.coroutines.*
import org.reduxkotlin.Store

/**
 * Create a DisposableScope that is disposed when the Bloc is destroyed (onDestroy called)
 */
fun BlocContext.disposableScope() = DisposableScope()
    .apply { lifecycle.doOnDestroy(::dispose) }

/**
 * Add a state selection subscription to a DisposableScope so it will be properly disposed when
 * the DisposableScope is disposed. Call this as:
 * ```
 *    selectScoped(store, selector) { model ->
 *       // do stuff with the selected model
 *    }
 * ```
 */
fun <State: Any, SelectedState: Any> DisposableScope.selectScoped(
    store: Store<State>,
    selector: (State) -> SelectedState,
    block: (selectedState: SelectedState) -> Unit
) {
    store
        .select(selector) { block(it) }
        .scope { unsubscribe -> unsubscribe() }
}

/**
 * Add a state selection subscription to a DisposableScope so it will be properly disposed when
 * the DisposableScope is disposed. Call this as:
 * ```
 *    store.selectScoped(disposableScope, selector) { model ->
 *       // do stuff with the selected model
 *    }
 * ```
 */
fun <State: Any, SelectedState: Any> Store<State>.selectScoped(
    disposableScope: DisposableScope,
    selector: (State) -> SelectedState,
    block: (selectedState: SelectedState) -> Unit
) {
    val unsubscribe = select(selector) { block(it) }
    disposableScope.doOnDispose { unsubscribe() }
}

/**
 * Extension function to convert a Redux store to a ReduxBlocState:
 * ```
 *    store.toBlocState(context, initialValue)
 * ```
 */
fun <State: Any, Proposal: Any> Store<State>.toBlocState(
    context: BlocContext,
    initialState: State,
) = reduxBlocState<State, Proposal, State>(context.disposableScope(), this) {
    this.initialState = initialState
    select { it }
}

/**
 * Extension function to convert a Redux store to a ReduxBlocState:
 * ```
 *    store.toBlocState(context, initialValue) { /* select function */ }
 * ```
 */
fun <State: Any, Proposal: Any, ReduxModel: Any> Store<ReduxModel>.toBlocState(
    context: BlocContext,
    initialState: State,
    selector: Selector<ReduxModel, State>
) = reduxBlocState<State, Proposal, ReduxModel>(context.disposableScope(), this) {
    this.initialState = initialState
    select(selector)
}

/**
 * Extension function to convert a Redux store to a ReduxBlocState:
 * ```
 *    store.toBlocState(context, initialValue, { /* select function */ },  { /* map function */ })
 * ```
 */
fun <State: Any, Proposal: Any, Model: Any, ReduxModel: Any> Store<ReduxModel>.toBlocState(
    context: BlocContext,
    initialState: State,
    selector: Selector<ReduxModel, Model>,
    mapper: Mapper<Model, State>
) = reduxBlocState<State, Proposal, Model, ReduxModel>(context.disposableScope(), this) {
    this.initialState = initialState
    select(selector)
    map(mapper)
}

fun <State, Action: Any, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.subscribe(
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    blocContext.lifecycle.doOnCreate {
        logger.d("onCreate -> start subscription")
        state?.let {
            coroutineScope.launch {
                collect { state(it) }
            }
        }
        sideEffect?.let {
            coroutineScope.launch {
                sideEffects.collect { sideEffect(it) }
            }
        }
    }

    blocContext.lifecycle.doOnDestroy {
        logger.d("onDestroy -> stop subscription")
        coroutineScope.cancel("Stop Subscription")
    }
}

/**
 * If a components implements the BlocObservableOwner interface it needs to provide
 * ```
 *   val observable: BlocObservable<State, SideEffect>
 * ```
 * This extension functions converts a Bloc into that BlocObservable:
 * ```
 *   override val observable = bloc.toObservable()
 * ```
 */
fun <State, Action : Any, SideEffect, Proposal> Bloc<State, Action, SideEffect, Proposal>.toObservable() =
    object : BlocObservable<State, SideEffect> {
        override fun subscribe(
            state: (suspend (state: State) -> Unit)?,
            sideEffect: (suspend (sideEffect: SideEffect) -> Unit)?
        ) {
            this@toObservable.subscribe(state, sideEffect)
        }
    }

/**
 * Call in a component to observe state and side effect updates in a BlocObservableOwner
 * (BlocObservableOwner in Android is typically a ViewModel, the observing component a Fragment or
 * an Activity):
 * ```
 *   component.subscribe(state = ::render, sideEffect = ::sideEffect)
 * ```
 */
fun <State, SideEffect> BlocObservableOwner<State, SideEffect>.subscribe(
    state: (suspend (state: State) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SideEffect) -> Unit)? = null
) {
    observable.subscribe(state, sideEffect)
}
