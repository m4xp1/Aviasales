package one.xcorp.aviasales.data.repository

import io.reactivex.Single
import one.xcorp.aviasales.data.source.retrofit.city.CityRetrofitSource
import one.xcorp.aviasales.domain.entity.CityEntity
import one.xcorp.aviasales.domain.repository.CityRepository
import javax.inject.Inject

internal class CityRepositoryImpl @Inject constructor(
    private val cityNetworkSource: CityRetrofitSource
) : CityRepository {

    override fun find(query: String): Single<List<CityEntity>> {
        return cityNetworkSource.find(query)
    }
}
