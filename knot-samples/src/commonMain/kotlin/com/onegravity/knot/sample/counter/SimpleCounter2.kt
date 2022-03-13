//package com.onegravity.knot.sample.counter
//
//import com.onegravity.knot.SideEffect
//import com.onegravity.knot.context.KnotContext
//import com.onegravity.knot.knot
//import org.reduxkotlin.*
//
//typealias ThunkMiddleware<State> = Middleware<State>
//typealias Middleware<State> = (store: Store<State>) -> (next: Dispatcher) -> (action: Any) -> Any
////typealias Thunk<State> = (dispatch: Dispatcher, getState: GetState<State>, extraArg: Any?) -> Any
//fun <State> middleware(dispatch: (Store<State>, next: Dispatcher, action: Any) -> Any): Middleware<State> =
//
//object SimpleCounter2 {
//
//    val t: ThunkMiddleware<Int> = middleware<Int> { store, next, action ->
//        val result = next(action)
//        result
//    }
//
//    sealed class Event {
//        data class Increment(val value: Int = 1): Event()
//        data class Decrement(val value: Int = 1): Event()
//    }
//
//    val test: Thunk<Int> = { dispatch, getState ->
//        dispatch(Int)
//    }
//
//    val test = createThunkMiddleware<>()
//
//    val loggerMiddleware: Middleware<Int> = middleware<Int> { store, next, action ->
//        val result = next(action)
//        result
//    }
//
//    fun test() {
//        val dispatch: Dispatcher = { something -> something }
//        val tmp1: (next: Dispatcher) -> (action: Any) -> Any = loggerMiddleware(reduxStore)
//        val tmp2: (action: Any) -> Any = tmp1(dispatch)
//        val tmp3: Any = tmp2(Event.Increment(1))
//
//        val tmp = loggerMiddleware(reduxStore)(dispatch)(Event.Increment(1))
//    }
//
//    fun knot(context: KnotContext) =
//
//        knot<State, Event>(context, 1) {
//            step<State, Event> { state, event, dispatch ->
//                dispatch(Loading)
//                // do asynchronous stuff
//                dispatch(Complete)
//            }
//
//            step<State> { state, event, dispatch ->
//                dispatch(Loading)
//                // do asynchronous stuff
//                dispatch(Complete)
//            }
//
//            reduce<State> { state, event, dispatch ->
//                when (event) {
//                    is Event.Increment -> state.plus(event.value)
//                    is Event.Decrement -> state.minus(event.value).coerceAtLeast(0).toEffect()
//                }
//            }
//        }
//
////        middleware<State, Event> { state, next, event ->
////            when (event) {
////                Event.SomeEvent -> {
////                    // Do stuff
////                    next(event)
////                }
////            }
////        }
////
////        thunk<Int> { dispatch, getState, extraArgument ->
////            dispatch(Loading)
////            // do asynchronous stuff
////            dispatch(Complete)
////        }
////
////        thunk<State> { state, dispatch ->
////            dispatch(Loading)
////            // do asynchronous stuff
////            dispatch(Complete)
////        }
//
//        reduce { state, proposal ->
//            when (event) {
//                is Event.Increment -> state.plus(event.value)
//                is Event.Decrement -> state.minus(event.value).coerceAtLeast(0).toEffect()
//            }
//        }
//    }
//
//    private fun bonus(value: Int) = SideEffect<Event> {
//        if (value.mod(10) == 0) Event.Increment(4) else null
//    }
//}
