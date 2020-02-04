package one.xcorp.aviasales.data.source.retrofit.city.mapper

import one.xcorp.aviasales.data.source.retrofit.city.dto.CityDto
import one.xcorp.aviasales.domain.entity.CityEntity

internal fun CityDto.toEntity() = CityEntity(
    name = city,
    fullName = fullName,
    iata = iata,
    location = location.toEntity()
)

internal fun List<CityDto>.toEntity() = map { it.toEntity() }
