package one.xcorp.aviasales.screen.ticket.route.model

import one.xcorp.mvvm.model.InputState
import one.xcorp.mvvm.model.InputState.NotEntered

data class InputModel(
    val departure: InputState<CityModel> = NotEntered,
    val destination: InputState<CityModel> = NotEntered
) {

    object NotSelected : InputState.Error()
}
