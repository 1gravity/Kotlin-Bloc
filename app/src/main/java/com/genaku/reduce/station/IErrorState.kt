package com.genaku.reduce.station

interface IErrorState {
    class FailureState(val error: Error)
}

interface IFailureChange {
    class Failure(val error: Error)
}

interface IFailureAction {

}

//sealed class CaseState {
//    data class CaseContent(val cases: List<Case>): CaseState()
//    data class CaseError(override val error: Error): CaseState(), IErrorState
//}
//
//data class Case(val value: String)
//
//fun getErrorKnot(errorState: CoroutineKnotState<IErrorState>) = coroutineKnot<IErrorState, IFailureChange, IFailureAction> {
//
//    knotState = errorState
//
//    changes { intent ->
//        IErrorState.FailureState(intent)
//    }
//
//}