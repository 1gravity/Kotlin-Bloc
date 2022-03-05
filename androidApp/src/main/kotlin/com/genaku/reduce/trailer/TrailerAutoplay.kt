//package com.genaku.reduce.trailer
//
//import com.onegravity.knot.*
//import kotlinx.coroutines.*
//import kotlinx.coroutines.flow.StateFlow
//import java.lang.Exception
//import kotlin.coroutines.CoroutineContext
//
//interface ITrailerState
//
//sealed class TrailerState : State, ITrailerState
//
////ничего не происходит
//object IdleState : TrailerState()
//
//// показывать баннер: 2 секунды, если урл трейлера есть / 5 секунд, если урл трейлера нет
//data class PreviewState(val id: String, val url: String) : TrailerState()
//
//// период preview закончился, можно показывать трейлер
//data class PlayableState(val id: String, val url: String) : TrailerState()
//
//// проигрывает трейлер (запустить трейлер на проигрывание)
//data class PlayingState(val id: String, val url: String) : TrailerState()
//
//
//sealed class TrailerIntent(open val id: String)
//data class StartTrailerIntent(override val id: String) : TrailerIntent(id)
//data class PlayUrlReadyIntent(override val id: String, val url: String) : TrailerIntent(id)
//data class PlayUrlFailedIntent(override val id: String) : TrailerIntent(id)
//data class PreviewTimeIsFinishedIntent(override val id: String) : TrailerIntent(id)
//data class BannerTimeIsFinishedIntent(override val id: String) : TrailerIntent(id)
//data class TrailerIsFinishedIntent(override val id: String) : TrailerIntent(id)
//
//sealed class TrailerEvent
//object NextBannerEvent : TrailerEvent()
//data class StopTrailerEvent(val id: String) : TrailerEvent()
//
//class TrailerAutoplayModel(
//    private val repository: TrailerRepository,
//    private val eventTransmitter: TrailerEventTransmitter,
//    private val previewPeriod: Long = PREVIEW_PERIOD,
//    private val bannerPeriod: Long = BANNER_PERIOD,
//    dispatcher: CoroutineContext = Dispatchers.Default
//) {
//
//    val state: StateFlow<ITrailerState>
//        get() = knot.state
//
//    private var _scope: CoroutineScope? = null
//
//    private val knot = suspendKnot<TrailerState, TrailerIntent> {
//
//        dispatcher(dispatcher)
//
//        initialState = IdleState
//
//        reduce { state, intent ->
//            when (state) {
//                IdleState -> when (intent) {
//                    is StartTrailerIntent -> {
//                        PreviewState(
//                            intent.id,
//                            ""
//                        ) + restartTimers(intent.id) + getPlayUrl(intent.id)
//                    }
//                    else -> state.toEffect
//                }
//
//                is PreviewState -> when (intent) {
//                    is StartTrailerIntent -> if (state.isSameTrailer(intent)) {
//                        state.toEffect
//                    } else {
//                        PreviewState(
//                            intent.id,
//                            ""
//                        ) + restartTimers(intent.id) + stopLoadUrl(state.id) + getPlayUrl(intent.id)
//                    }
//                    is PlayUrlReadyIntent -> if (state.isSameTrailer(intent)) {
//                        PreviewState(intent.id, intent.url).toEffect
//                    } else {
//                        state.toEffect
//                    }
//                    is PlayUrlFailedIntent -> state.toEffect
//                    is PreviewTimeIsFinishedIntent -> if (state.isSameTrailer(intent)) {
//                        if (state.url.isNotBlank()) {
//                            PlayingState(state.id, state.url).toEffect
//                        } else {
//                            PlayableState(state.id, state.url).toEffect
//                        }
//                    } else {
//                        state.toEffect
//                    }
//                    is BannerTimeIsFinishedIntent -> if (state.isSameTrailer(intent)) {
//                        IdleState + nextBanner()
//                    } else {
//                        state.toEffect
//                    }
//                    is TrailerIsFinishedIntent -> if (state.isSameTrailer(intent)) {
//                        IdleState + nextBanner()
//                    } else {
//                        state.toEffect
//                    }
//                }
//
//                is PlayableState -> when (intent) {
//                    is StartTrailerIntent -> if (state.isSameTrailer(intent)) {
//                        state.toEffect
//                    } else {
//                        PreviewState(
//                            intent.id,
//                            ""
//                        ).toEffect + restartTimers(intent.id) + getPlayUrl(intent.id)
//                    }
//                    is PlayUrlReadyIntent -> if (state.isSameTrailer(intent)) {
//                        PlayableState(state.id, intent.url).toEffect
//                    } else {
//                        state.toEffect
//                    }
//                    is PlayUrlFailedIntent -> state.toEffect
//                    is PreviewTimeIsFinishedIntent -> {
//                        if (state.url.isNotBlank()) {
//                            PlayingState(state.id, state.url) + stopTimers()
//                        } else {
//                            state.toEffect
//                        }
//                    }
//                    is BannerTimeIsFinishedIntent -> if (state.isSameTrailer(intent)) {
//                        IdleState + nextBanner()
//                    } else {
//                        state.toEffect
//                    }
//                    is TrailerIsFinishedIntent -> if (state.isSameTrailer(intent)) {
//                        IdleState + nextBanner()
//                    } else {
//                        state.toEffect
//                    }
//                }
//
//                is PlayingState -> when (intent) {
//                    is StartTrailerIntent -> if (state.isSameTrailer(intent)) {
//                        state.toEffect
//                    } else {
//                        PreviewState(
//                            intent.id,
//                            ""
//                        ) + stopTrailer(state.id) + restartTimers(intent.id) + getPlayUrl(intent.id)
//                    }
//                    is TrailerIsFinishedIntent -> if (state.isSameTrailer(intent)) {
//                        IdleState + nextBanner()
//                    } else {
//                        state.toEffect
//                    }
//                    else -> state.toEffect
//                }
//            }
//        }
//    }
//
//    fun start(scope: CoroutineScope) {
//        _scope = scope
//        knot.start(scope)
//    }
//
//    fun stop() {
//        knot.stop()
//    }
//
//    fun startBanner(id: String) {
//        knot.offerIntent(StartTrailerIntent(id))
//    }
//
//    fun stopBanner(id: String) {
//        knot.offerIntent(TrailerIsFinishedIntent(id))
//    }
//
//    private fun PreviewState.isSameTrailer(intent: TrailerIntent) =
//        this.id == intent.id
//
//    private fun PlayingState.isSameTrailer(intent: TrailerIntent) =
//        this.id == intent.id
//
//    private fun PlayableState.isSameTrailer(intent: TrailerIntent) =
//        this.id == intent.id
//
//    private var previewTimerJob: Job? = null
//    private var bannerTimerJob: Job? = null
//
//    private fun restartTimers(id: String): SuspendSideEffect<TrailerIntent> = SuspendSideEffect {
//        _scope?.run {
//            previewTimerJob?.cancel()
//            previewTimerJob = launch {
//                delay(previewPeriod)
//                knot.offerIntent(PreviewTimeIsFinishedIntent(id))
//            }
//            bannerTimerJob?.cancel()
//            bannerTimerJob = launch {
//                delay(bannerPeriod)
//                knot.offerIntent(BannerTimeIsFinishedIntent(id))
//            }
//        }
//        null
//    }
//
//    private fun stopTimers(): SuspendSideEffect<TrailerIntent> = SuspendSideEffect {
//        previewTimerJob?.cancel()
//        previewTimerJob = null
//        bannerTimerJob?.cancel()
//        bannerTimerJob = null
//        null
//    }
//
//    private suspend fun getPlayUrl(id: String): SuspendSideEffect<TrailerIntent> =
//        SuspendSideEffect {
//            try {
//                val url = repository.getTrailerUrl(id)
//                PlayUrlReadyIntent(id, url)
//            } catch (e: Exception) {
//                PlayUrlFailedIntent(id)
//            }
//        }
//
//    private fun stopLoadUrl(id: String): SuspendSideEffect<TrailerIntent> = SuspendSideEffect {
//        repository.stopRequest(id)
//        null
//    }
//
//    private fun nextBanner(): SuspendSideEffect<TrailerIntent> = SuspendSideEffect {
//        eventTransmitter.sendEvent(NextBannerEvent)
//        null
//    }
//
//    private fun stopTrailer(id: String): SuspendSideEffect<TrailerIntent> = SuspendSideEffect {
//        eventTransmitter.sendEvent(StopTrailerEvent(id))
//        null
//    }
//
//    companion object {
//        private const val PREVIEW_PERIOD = 2000L
//        private const val BANNER_PERIOD = 5000L
//    }
//}
//
//interface TrailerRepository {
//    suspend fun getTrailerUrl(id: String): String
//    fun stopRequest(id: String)
//}
//
//interface TrailerEventTransmitter {
//    fun sendEvent(event: TrailerEvent)
//}