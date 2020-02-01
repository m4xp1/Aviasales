package one.xcorp.aviasales.screen.ticket.route.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AirportModel(
    val city: String,
    val iata: String,
    val location: LatLng
) : Parcelable
