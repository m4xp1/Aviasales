package one.xcorp.mvvm

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory

inline fun <reified T : ViewModel> FragmentActivity.obtainViewModel(
    factory: Factory
) = ViewModelProvider(this, factory)[T::class.java]

inline fun <reified T : ViewModel> FragmentActivity.obtainViewModel(
    factory: Factory,
    body: T.() -> Unit
) = ViewModelProvider(this, factory)[T::class.java].apply(body)
