package com.onegravity.bloc

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class BaseActivity : AppCompatActivity() {

    protected fun <T : ViewDataBinding> bind(@LayoutRes layoutId: Int, bind2ViewModel: (T) -> Unit) {
        val binding = DataBindingUtil.setContentView<T>(this, layoutId)
        bind2ViewModel(binding)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

    @Suppress("UNCHECKED_CAST")
    protected inline fun <VM : ViewModel> factory(crossinline f: (context: ActivityBlocContext) -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>):T = f(activityBlocContext()) as T
        }

}