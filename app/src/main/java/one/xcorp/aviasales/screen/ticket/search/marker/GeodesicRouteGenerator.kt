package one.xcorp.aviasales.screen.ticket.search.marker

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import javax.inject.Inject

class GeodesicRouteGenerator @Inject constructor() : RouteGenerator {

    override fun generate(
        googleMap: GoogleMap,
        departure: LatLng,
        destination: LatLng
    ): PolylineOptions = PolylineOptions()
        .add(departure, destination)
        .geodesic(true)
}
