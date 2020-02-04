package one.xcorp.aviasales.domain.usecase.city.find

import io.reactivex.Single
import one.xcorp.aviasales.domain.entity.CityEntity
import javax.inject.Inject

class FindCityWithAirportsUseCase @Inject constructor(
    private val findAllCity: FindAllCityUseCase
) : FindCityUseCase {

    override operator fun invoke(query: String): Single<List<CityEntity>> =
        findAllCity(query).map { cities ->
            cities.filter { it.iata.isNotEmpty() }
        }
}
