package one.xcorp.aviasales.screen.ticket.route

import one.xcorp.aviasales.domain.usecase.city.find.FindCityUseCase
import one.xcorp.mvvm.rx.RxViewModel
import javax.inject.Inject

class TicketRouteViewModel @Inject constructor(
    private val findCity: FindCityUseCase
) : RxViewModel() {

}
