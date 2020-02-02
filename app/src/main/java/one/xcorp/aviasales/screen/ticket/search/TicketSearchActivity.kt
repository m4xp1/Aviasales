package one.xcorp.aviasales.screen.ticket.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getColor
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource
import com.google.android.gms.maps.model.MapStyleOptions.loadRawResourceStyle
import com.google.maps.android.ui.IconGenerator
import one.xcorp.aviasales.R
import one.xcorp.aviasales.R.attr.markerTextAppearance
import one.xcorp.aviasales.R.drawable.airport_marker_background
import one.xcorp.aviasales.R.style.MarkerTextAppearance
import one.xcorp.aviasales.extension.bearingTo
import one.xcorp.aviasales.extension.getThemeAttribute
import one.xcorp.aviasales.screen.ticket.route.mapper.toLatLng
import one.xcorp.aviasales.screen.ticket.route.model.AirportModel

class TicketSearchActivity : AppCompatActivity() {

    private lateinit var departureAirport: AirportModel
    private lateinit var destinationAirport: AirportModel

    private lateinit var googleMap: GoogleMap
    private lateinit var planeMarker: Marker

    private val dotLineGap by lazy {
        resources.getDimensionPixelSize(R.dimen.ticket_search_activity_map_route_gap).toFloat()
    }

    private val dotLineWidth by lazy {
        resources.getDimensionPixelSize(R.dimen.ticket_search_activity_map_route_width).toFloat()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_search)

        departureAirport = requireNotNull(intent.getParcelableExtra(KEY_DEPARTURE_AIRPORT))
        destinationAirport = requireNotNull(intent.getParcelableExtra(KEY_DESTINATION_AIRPORT))

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(::onMapReady)
    }

    private fun onMapReady(map: GoogleMap) {
        googleMap = map.apply {
            uiSettings.apply {
                isCompassEnabled = false
                isMapToolbarEnabled = false
            }
            setMapStyle(loadRawResourceStyle(applicationContext, R.raw.google_map_style))
        }

        val markerBounds = setInitialMapMarkers()
        setInitialCameraPosition(markerBounds)
    }

    private fun setInitialMapMarkers(): LatLngBounds {
        val departureLocation = departureAirport.location.toLatLng()
        val destinationLocation = destinationAirport.location.toLatLng()
        val bearing = departureLocation.bearingTo(destinationLocation)

        val pointsSet = mutableSetOf<LatLng>()

        addPlaneRoute(departureLocation, destinationLocation)
            .apply { pointsSet.addAll(points) }
        addAirportMarker(departureAirport)
            .apply { pointsSet.add(position) }
        addAirportMarker(destinationAirport)
            .apply { pointsSet.add(position) }
        planeMarker = addPlaneMarker(departureLocation, bearing)
            .apply { pointsSet.add(position) }

        val boundsBuilder = LatLngBounds.builder()
        pointsSet.forEach { boundsBuilder.include(it) }

        return boundsBuilder.build()
    }

    private fun setInitialCameraPosition(markerBounds: LatLngBounds) {
        val cameraUpdate = newLatLngBounds(
            markerBounds,
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.heightPixels,
            resources.getDimensionPixelSize(R.dimen.ticket_search_activity_map_extent_padding)
        )

        googleMap.moveCamera(cameraUpdate)
    }

    private fun addAirportMarker(airport: AirportModel): Marker {
        val iconGenerator = IconGenerator(this).apply {
            setContentPadding(0, 0, 0, 0)
            setTextAppearance(getThemeAttribute(markerTextAppearance, MarkerTextAppearance))
            setBackground(getDrawable(airport_marker_background))
        }

        val markerOptions = MarkerOptions()
            .position(airport.location.toLatLng())
            .icon(fromBitmap(iconGenerator.makeIcon(airport.iata)))
            .anchor(0.5f, 0.5f)
            .zIndex(Z_INDEX_MARKER)

        return googleMap.addMarker(markerOptions)
    }

    private fun addPlaneRoute(departureLocation: LatLng, destinationLocation: LatLng): Polyline {
        val polylineOptions = PolylineOptions()
            .add(departureLocation, destinationLocation)
            .geodesic(true)
            .color(getColor(this, R.color.plane_route))
            .pattern(listOf(Gap(dotLineGap), Dot()))
            .width(dotLineWidth)
            .zIndex(Z_INDEX_GRAPHIC)

        return googleMap.addPolyline(polylineOptions)
    }

    private fun addPlaneMarker(location: LatLng, bearing: Float): Marker {
        val markerOptions = MarkerOptions()
            .position(location)
            .rotation(bearing)
            .icon(fromResource(R.drawable.ic_plane))
            .anchor(0.5f, 0.5f)
            .zIndex(Z_INDEX_ANIMATED_MARKER)

        return googleMap.addMarker(markerOptions)
    }

    companion object {

        private const val Z_INDEX_GRAPHIC = 0f
        private const val Z_INDEX_MARKER = 1f
        private const val Z_INDEX_ANIMATED_MARKER = 2f

        private const val KEY_DEPARTURE_AIRPORT = "departure_airport"
        private const val KEY_DESTINATION_AIRPORT = "destination_airport"

        fun newIntent(
            context: Context,
            departureAirport: AirportModel,
            destinationAirport: AirportModel
        ) = Intent(context, TicketSearchActivity::class.java).apply {
            putExtra(KEY_DEPARTURE_AIRPORT, departureAirport)
            putExtra(KEY_DESTINATION_AIRPORT, destinationAirport)
        }
    }
}
