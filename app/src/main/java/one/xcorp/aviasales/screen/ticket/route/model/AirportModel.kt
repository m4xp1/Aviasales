package one.xcorp.aviasales.screen.ticket.route.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirportModel(
    val fullName: String,
    val iata: String,
    val location: LocationModel
) : Parcelable
