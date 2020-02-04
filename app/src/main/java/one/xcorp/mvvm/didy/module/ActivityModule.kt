package one.xcorp.mvvm.didy.module

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import one.xcorp.didy.provider.WeakProvider
import one.xcorp.didy.scope.ActivityScope

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class ActivityModule {

    @Provides
    @ActivityScope
    fun activityProvider(): WeakProvider<AppCompatActivity> = WeakProvider()

    @Provides
    fun activity(activityProvider: WeakProvider<AppCompatActivity>): AppCompatActivity {
        return requireNotNull(activityProvider.get()) {
            "Can't provide Activity, it is not created or not attached into provider."
        }
    }
}
