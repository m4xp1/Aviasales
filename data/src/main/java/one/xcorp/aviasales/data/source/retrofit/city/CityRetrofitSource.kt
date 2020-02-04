package one.xcorp.aviasales.data.source.retrofit.city

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import one.xcorp.aviasales.data.source.retrofit.city.mapper.toEntity
import one.xcorp.aviasales.domain.entity.CityEntity
import java.util.*
import javax.inject.Inject

internal class CityRetrofitSource @Inject constructor(
    private val cityApi: CityRetrofitApi
) {

    fun find(query: String): Single<List<CityEntity>> {
        return cityApi.autocomplete(query, Locale.getDefault().language)
            .subscribeOn(Schedulers.io())
            .map { it.cities.toEntity() }
    }
}
