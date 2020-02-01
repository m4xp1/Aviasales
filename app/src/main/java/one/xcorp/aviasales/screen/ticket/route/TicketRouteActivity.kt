package one.xcorp.aviasales.screen.ticket.route

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import one.xcorp.aviasales.R
import one.xcorp.aviasales.screen.ticket.route.model.AirportModel
import one.xcorp.aviasales.screen.ticket.search.TicketSearchActivity

class TicketRouteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_route)

        val departureAirport = AirportModel("St. Petersburg", "LED", LatLng(59.942652, 30.311771))
        val destinationAirport = AirportModel("Moscow", "MOW", LatLng(55.755788, 37.627144))

        startActivity(TicketSearchActivity.newIntent(this, departureAirport, destinationAirport))
    }
}
