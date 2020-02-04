package one.xcorp.aviasales.domain.usecase.city.find

import io.reactivex.Single
import one.xcorp.aviasales.domain.entity.CityEntity

interface FindCityUseCase {

    operator fun invoke(query: String): Single<List<CityEntity>>
}
