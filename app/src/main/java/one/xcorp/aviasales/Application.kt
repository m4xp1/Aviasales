package one.xcorp.aviasales

import one.xcorp.aviasales.BuildConfig.CITY_API_URL
import one.xcorp.aviasales.dagger.ApplicationComponent
import one.xcorp.aviasales.dagger.DaggerApplicationComponent
import one.xcorp.aviasales.data.CityApiConfiguration
import one.xcorp.aviasales.data.dagger.DaggerDataComponent

class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
        initializeGraph(this)
    }

    companion object Dependencies {

        val applicationComponent: ApplicationComponent by lazy {
            DaggerApplicationComponent.factory().createComponent(
                application,
                createDataComponent()
            )
        }

        private lateinit var application: Application

        private fun initializeGraph(application: Application) {
            this.application = application
        }

        private fun createDataComponent() = DaggerDataComponent.factory().createComponent(
            CityApiConfiguration(CITY_API_URL)
        )
    }
}
