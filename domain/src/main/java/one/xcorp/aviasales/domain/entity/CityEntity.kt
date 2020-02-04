package one.xcorp.aviasales.domain.entity

data class CityEntity(
    val name: String,
    val fullName: String,
    val iata: List<String>,
    val location: LocationEntity
)
