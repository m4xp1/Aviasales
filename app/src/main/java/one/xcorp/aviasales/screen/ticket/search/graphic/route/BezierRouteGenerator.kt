package one.xcorp.aviasales.screen.ticket.search.graphic.route

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import one.xcorp.aviasales.extension.angleTo
import one.xcorp.aviasales.extension.cubicTo
import one.xcorp.aviasales.extension.distanceTo
import one.xcorp.aviasales.extension.offset

class BezierRouteGenerator : RouteGenerator {

    override fun generate(
        googleMap: GoogleMap,
        departure: LatLng,
        destination: LatLng
    ): PolylineOptions {
        val cameraPosition = googleMap.cameraPosition
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(21f))
        val projection = googleMap.projection
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        val startPoint = projection.toScreenLocation(departure)
        val endPoint = projection.toScreenLocation(destination)

        val distance = startPoint.distanceTo(endPoint)
        val angle = startPoint.angleTo(endPoint)

        val referencePoint1 = startPoint.offset(distance / 2.5, angle - 63)
        val referencePoint2 = endPoint.offset(distance / 2.5, angle + 117)

        val curve = startPoint.cubicTo(referencePoint1, referencePoint2, endPoint, 1000f)
            .distinct()
            .map { projection.fromScreenLocation(it) }

        return PolylineOptions().addAll(curve)
    }
}
