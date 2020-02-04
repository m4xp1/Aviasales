package one.xcorp.aviasales.screen.ticket.route.mapper

import one.xcorp.aviasales.domain.entity.CityEntity
import one.xcorp.aviasales.screen.ticket.route.model.CityModel

fun CityEntity.toCityModel() = CityModel(
    name = name,
    fullName = fullName,
    iata = iata,
    location = location.toLocationModel()
)

fun List<CityEntity>.toCityModel() = map { it.toCityModel() }
