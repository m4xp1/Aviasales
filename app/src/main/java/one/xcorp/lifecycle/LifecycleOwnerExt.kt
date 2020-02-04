package one.xcorp.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T) -> Unit) =
    liveData.observe(this, NonNullObserver(body))

fun <T : Any, L : LiveData<T>> LifecycleOwner.observeNullable(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))
