package one.xcorp.aviasales.data.source.retrofit.city.response

import one.xcorp.aviasales.data.source.retrofit.city.dto.CityDto

internal data class AutocompleteResponse(
    val cities: List<CityDto>
)
