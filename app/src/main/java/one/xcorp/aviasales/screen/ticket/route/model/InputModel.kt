package one.xcorp.aviasales.screen.ticket.route.model

import one.xcorp.mvvm.model.InputState

data class InputModel(
    val departure: InputState<CityModel>,
    val destination: InputState<CityModel>
) {

    object NotSelected : InputState.Error()
}
