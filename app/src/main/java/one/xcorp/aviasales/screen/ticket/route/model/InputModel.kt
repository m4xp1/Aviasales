package one.xcorp.aviasales.screen.ticket.route.model

import one.xcorp.mvvm.model.InputState
import one.xcorp.mvvm.model.InputState.Error
import one.xcorp.mvvm.model.InputState.NotEntered

data class InputModel(
    val departure: InputState<CityModel> = NotEntered,
    val destination: InputState<CityModel> = NotEntered
) {

    val isContainsErrors: Boolean
        get() = arrayOf(departure, destination).any { it is Error }

    object NotSelected : Error()
}
