package one.xcorp.aviasales.navigation.ticket

import one.xcorp.aviasales.screen.ticket.route.model.CityModel

interface TicketNavigator {

    fun launchTicketSearch(departureCity: CityModel, destinationCity: CityModel)
}
