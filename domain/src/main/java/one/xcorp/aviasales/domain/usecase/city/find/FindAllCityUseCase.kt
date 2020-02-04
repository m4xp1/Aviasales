package one.xcorp.aviasales.domain.usecase.city.find

import io.reactivex.Single
import one.xcorp.aviasales.domain.entity.CityEntity
import one.xcorp.aviasales.domain.repository.CityRepository
import javax.inject.Inject

class FindAllCityUseCase @Inject constructor(
    private val cityRepository: CityRepository
) : FindCityUseCase {

    override operator fun invoke(query: String): Single<List<CityEntity>> =
        cityRepository.find(query)
}
