package one.xcorp.aviasales.navigation.ticket

import androidx.appcompat.app.AppCompatActivity
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.aviasales.screen.ticket.search.TicketSearchActivity
import javax.inject.Inject

class ActivityTicketNavigator @Inject constructor(
    private val activity: AppCompatActivity
) : TicketNavigator {

    override fun launchTicketSearch(departureCity: CityModel, destinationCity: CityModel) {
        val intent = TicketSearchActivity.newIntent(activity, departureCity, destinationCity)
        activity.startActivity(intent)
    }
}
