package one.xcorp.aviasales.screen.ticket.route.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CityModel(
    val name: String,
    val fullName: String,
    val iata: List<String>,
    val location: LocationModel
) : Parcelable {

    fun getAirportName(): String =
        iata.firstOrNull() ?: name.take(3).toUpperCase(Locale.getDefault())
}
