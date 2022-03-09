package com.genaku.reduce

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class BaseActivity : AppCompatActivity {

    constructor(): super()

    constructor(@LayoutRes contentLayoutId: Int): super(contentLayoutId)

    @Suppress("UNCHECKED_CAST")
    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>):T = f() as T
        }

}