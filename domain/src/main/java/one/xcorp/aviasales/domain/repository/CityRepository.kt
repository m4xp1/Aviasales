package one.xcorp.aviasales.domain.repository

import io.reactivex.Single
import one.xcorp.aviasales.domain.entity.CityEntity

interface CityRepository {

    fun find(query: String): Single<List<CityEntity>>
}
