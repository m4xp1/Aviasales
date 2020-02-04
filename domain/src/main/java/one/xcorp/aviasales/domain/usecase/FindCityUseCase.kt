package one.xcorp.aviasales.domain.usecase

import io.reactivex.Single
import one.xcorp.aviasales.domain.entity.CityEntity
import one.xcorp.aviasales.domain.repository.CityRepository
import javax.inject.Inject

class FindCityUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {

    operator fun invoke(query: String): Single<List<CityEntity>> =
        cityRepository.find(query)
}
