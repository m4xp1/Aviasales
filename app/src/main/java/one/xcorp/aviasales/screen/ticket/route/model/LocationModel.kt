package one.xcorp.aviasales.screen.ticket.route.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationModel(
    val latitude: Double,
    val longitude: Double
) : Parcelable
