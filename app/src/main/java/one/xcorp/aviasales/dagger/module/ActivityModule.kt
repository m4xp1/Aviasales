package one.xcorp.aviasales.dagger.module

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import one.xcorp.mvvm.didy.module.ActivityModule

@Module(
    includes = [
        ActivityModule::class
    ]
)
class ActivityModule {

    @Provides
    fun layoutInflater(activity: AppCompatActivity): LayoutInflater {
        return activity.layoutInflater
    }
}
