package one.xcorp.mvvm.didy.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.Multibinds
import one.xcorp.didy.scope.ActivityScope
import one.xcorp.mvvm.factory.ViewModelFactory

@Module
abstract class ViewModelModule {

    @Multibinds
    abstract fun viewModels(): Map<Class<out ViewModel>, ViewModel>

    @Binds
    @ActivityScope
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
