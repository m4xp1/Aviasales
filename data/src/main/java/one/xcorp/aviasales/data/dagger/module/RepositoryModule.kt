package one.xcorp.aviasales.data.dagger.module

import dagger.Binds
import dagger.Module
import one.xcorp.aviasales.data.dagger.scope.DataScope
import one.xcorp.aviasales.data.repository.CityRepositoryImpl
import one.xcorp.aviasales.domain.repository.CityRepository

@Module
internal abstract class RepositoryModule {

    @Binds
    @DataScope
    abstract fun cityRepository(repository: CityRepositoryImpl): CityRepository
}
