package one.xcorp.mvvm.rx

import androidx.lifecycle.ViewModel
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import one.xcorp.lifecycle.rx.toLiveData

abstract class RxViewModel : ViewModel() {

    protected val viewModelDisposables = CompositeDisposable()

    protected fun <T> Single<T>.toLiveData() = toLiveData(viewModelDisposables::add)

    protected fun <T> Maybe<T>.toLiveData() = toLiveData(viewModelDisposables::add)

    protected fun <T> Observable<T>.toLiveData(
        strategy: BackpressureStrategy = BackpressureStrategy.LATEST
    ) = toLiveData(viewModelDisposables::add, strategy)

    protected fun <T> Flowable<T>.toLiveData() = toLiveData(viewModelDisposables::add)

    override fun onCleared() {
        viewModelDisposables.dispose()
    }
}
