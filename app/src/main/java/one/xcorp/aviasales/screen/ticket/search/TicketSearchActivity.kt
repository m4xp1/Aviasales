package one.xcorp.aviasales.screen.ticket.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import one.xcorp.aviasales.R
import one.xcorp.aviasales.screen.ticket.route.model.AirportModel
import one.xcorp.aviasales.screen.ticket.search.marker.AirportMarkerFactory

class TicketSearchActivity : AppCompatActivity() {

    private val airportMarkerFactory by lazy { AirportMarkerFactory(layoutInflater) }

    private lateinit var departureAirport: AirportModel
    private lateinit var destinationAirport: AirportModel

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_search)

        departureAirport = requireNotNull(intent.getParcelableExtra(DEPARTURE_AIRPORT_KEY))
        destinationAirport = requireNotNull(intent.getParcelableExtra(DESTINATION_AIRPORT_KEY))

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(::onMapReady)
    }

    private fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMapToolbarEnabled = false

        addAirportMarker(departureAirport)
        addAirportMarker(destinationAirport)

        val latLngBounds = newLatLngBounds(
            getInitialMapExtent(),
            resources.displayMetrics.widthPixels,
            resources.displayMetrics.heightPixels,
            resources.getDimensionPixelSize(R.dimen.ticket_search_activity_map_extent_padding)
        )

        map.moveCamera(latLngBounds)
    }

    private fun getInitialMapExtent() = LatLngBounds.builder()
        .include(departureAirport.location)
        .include(destinationAirport.location)
        .build()

    private fun addAirportMarker(airport: AirportModel) = with(airport) {
        map.addMarker(airportMarkerFactory.create(iata, location))
    }

    companion object {

        private const val DEPARTURE_AIRPORT_KEY = "departure_airport"
        private const val DESTINATION_AIRPORT_KEY = "destination_airport"

        fun newIntent(
            context: Context,
            departureAirport: AirportModel,
            destinationAirport: AirportModel
        ) = Intent(context, TicketSearchActivity::class.java).apply {
            putExtra(DEPARTURE_AIRPORT_KEY, departureAirport)
            putExtra(DESTINATION_AIRPORT_KEY, destinationAirport)
        }
    }
}
