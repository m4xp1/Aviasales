package one.xcorp.aviasales.screen.ticket.search.graphic.route

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

class GeodesicRouteGenerator : RouteGenerator {

    override fun generate(
        googleMap: GoogleMap,
        departure: LatLng,
        destination: LatLng
    ): PolylineOptions = PolylineOptions()
        .add(departure, destination)
        .geodesic(true)
}
