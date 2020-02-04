package one.xcorp.aviasales.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import one.xcorp.didy.scope.ApplicationScope

@Module
class ApplicationModule {

    @Provides
    @ApplicationScope
    fun context(application: Application): Context = application
}
