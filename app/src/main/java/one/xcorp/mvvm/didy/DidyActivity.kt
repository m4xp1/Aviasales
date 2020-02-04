package one.xcorp.mvvm.didy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import one.xcorp.didy.holder.ComponentHolder
import one.xcorp.didy.provider.WeakProvider
import javax.inject.Inject

abstract class DidyActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var componentHolder: ComponentHolder<*, *>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        componentHolder = onInject(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    open fun onInject(savedInstanceState: Bundle?): ComponentHolder<*, *>? = null

    @Inject
    fun onBind(activityProvider: WeakProvider<AppCompatActivity>) {
        activityProvider.set(this)
    }

    override fun onDestroy() {
        if (isFinishing) {
            componentHolder?.release()
        }
        super.onDestroy()
    }
}
