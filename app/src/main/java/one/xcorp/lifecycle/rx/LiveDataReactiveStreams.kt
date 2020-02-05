package one.xcorp.lifecycle.rx

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.*
import io.reactivex.BackpressureStrategy.LATEST
import io.reactivex.disposables.Disposable

fun <T> Single<T>.toLiveData(
    block: (Disposable) -> Boolean
) = toFlowable().toLiveData(block)

fun <T> Maybe<T>.toLiveData(
    block: (Disposable) -> Boolean
) = toFlowable().toLiveData(block)

fun <T> Observable<T>.toLiveData(
    block: (Disposable) -> Boolean,
    strategy: BackpressureStrategy = LATEST
) = toFlowable(strategy).toLiveData(block)

fun <T> Flowable<T>.toLiveData(block: (Disposable) -> Boolean): LiveData<T> {
    return PublisherLiveData<T> { liveData ->
        block.invoke(subscribe {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                liveData.value = it
            } else {
                liveData.postValue(it)
            }
        })
    }
}

private class PublisherLiveData<T>(
    private val subscribe: (MutableLiveData<T>) -> Unit
) : MutableLiveData<T>() {

    private var isSubscribed = false

    override fun onActive() {
        super.onActive()

        if (!isSubscribed) {
            subscribe(this)
            isSubscribed = true
        }
    }
}
