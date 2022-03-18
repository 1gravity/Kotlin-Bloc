package com.onegravity.bloc.stock

import androidx.lifecycle.viewModelScope
import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.sample.stock.StockList.bloc
import com.onegravity.bloc.sample.stock.StockList.Action
import com.onegravity.bloc.toLiveData

class StockViewModel(context: ActivityBlocContext) : BaseViewModel(context) {

    private val bloc = bloc(viewModelContext)

    val state = bloc.toLiveData(viewModelScope)

    fun increment() {
        bloc.emit(Action.Increment(1))
    }

    fun decrement() {
        bloc.emit(Action.Decrement(1))
    }

}