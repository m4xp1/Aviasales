package one.xcorp.aviasales.data.source.retrofit.city.dto

import com.google.gson.annotations.SerializedName

internal data class CityDto(
    val city: String,
    @SerializedName("fullname")
    val fullName: String,
    val iata: List<String>,
    val location: LocationDto
)
