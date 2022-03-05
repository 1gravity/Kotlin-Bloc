//package com.genaku.reduce.traffic
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//
//class TrafficViewModel : ViewModel() {
//
//    private val street1 = Street(100)
//    private val street2 = Street(200)
//    private val street3 = Street(300)
//
//    private val tl1 = TrafficLight(
//        coroutineScope = viewModelScope,
//        delay = 30,
//        limit = 10
//    )
//    private val tl2 = TrafficLight(
//        coroutineScope = viewModelScope,
//        delay = 20,
//        limit = 5
//    )
//    private val tl3 = TrafficLight(
//        coroutineScope = viewModelScope,
//        delay = 10
//    )
//
//    init {
//        initStreets()
//        start()
//    }
//
//    private fun initStreets() {
//        street1.setTrafficLight(tl1)
//        street2.setTrafficLight(tl2)
//        street3.setTrafficLight(tl3)
//
//        tl1.setStreetIn(street1)
//        tl1.setStreetOut(street2)
//        tl2.setStreetIn(street2)
//        tl2.setStreetOut(street3)
//        tl3.setStreetIn(street3)
//        tl3.setStreetOut(street1)
//    }
//
//    private fun start() {
//        street1.start(viewModelScope)
//        street2.start(viewModelScope)
//        street3.start(viewModelScope)
//        tl1.start()
//        tl2.start()
//        tl3.start()
//    }
//
//    val street1State
//        get() = street1.state
//
//    val street2State
//        get() = street2.state
//
//    val street3State
//        get() = street3.state
//
//    val tl1State
//        get() = tl1.state
//
//    val tl2State
//        get() = tl2.state
//
//    val tl3State
//        get() = tl3.state
//
//    fun addCar() {
//        street1.carIn()
//    }
//
//}