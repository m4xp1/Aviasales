package one.xcorp.aviasales.data.dagger

import dagger.BindsInstance
import dagger.Component
import one.xcorp.aviasales.data.CityApiConfiguration
import one.xcorp.aviasales.data.dagger.module.NetworkModule
import one.xcorp.aviasales.data.dagger.module.RepositoryModule
import one.xcorp.aviasales.data.dagger.module.RetrofitModule
import one.xcorp.aviasales.data.dagger.scope.DataScope
import one.xcorp.aviasales.domain.repository.CityRepository

@DataScope
@Component(
    modules = [
        NetworkModule::class,
        RetrofitModule::class,
        RepositoryModule::class
    ]
)
interface DataComponent {

    val cityRepository: CityRepository

    @Component.Factory
    interface Factory {

        fun createComponent(
            @BindsInstance cityApiConfiguration: CityApiConfiguration
        ): DataComponent
    }
}
