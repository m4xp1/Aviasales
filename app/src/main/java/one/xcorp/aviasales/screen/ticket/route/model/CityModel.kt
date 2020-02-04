package one.xcorp.aviasales.screen.ticket.route.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityModel(
    val name: String,
    val fullName: String,
    val iata: List<String>,
    val location: LocationModel
) : Parcelable {

    override fun toString(): String {
        return name
    }
}
