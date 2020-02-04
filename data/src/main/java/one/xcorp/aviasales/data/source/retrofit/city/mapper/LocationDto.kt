package one.xcorp.aviasales.data.source.retrofit.city.mapper

import one.xcorp.aviasales.data.source.retrofit.city.dto.LocationDto
import one.xcorp.aviasales.domain.entity.LocationEntity

internal fun LocationDto.toEntity() = LocationEntity(
    latitude = lat,
    longitude = lon
)
