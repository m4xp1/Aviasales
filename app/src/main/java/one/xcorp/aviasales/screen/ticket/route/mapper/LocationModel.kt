package one.xcorp.aviasales.screen.ticket.route.mapper

import com.google.android.gms.maps.model.LatLng
import one.xcorp.aviasales.screen.ticket.route.model.LocationModel

fun LocationModel.toLatLng() = LatLng(latitude, longitude)

fun List<LocationModel>.toLatLng() = map { it.toLatLng() }
