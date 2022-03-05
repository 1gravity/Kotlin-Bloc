//package com.genaku.reduce.sms
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.cancel
//
//class SmsViewModel: ViewModel() {
//
//    private val useCaseCoroutineScope : CoroutineScope = CoroutineScope(Job())
//
//    val loadingUseCase = DI.loadingUseCase
//    val smsUseCase = SmsUseCase(DI.repository, loadingUseCase, useCaseCoroutineScope)
//
//    init {
//        loadingUseCase.start(viewModelScope)
//    }
//
//    override fun onCleared() {
//        useCaseCoroutineScope.cancel()
//        super.onCleared()
//    }
//}