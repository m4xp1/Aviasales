package one.xcorp.aviasales.screen.ticket.search.model

sealed class StateModel {

    object Loading : StateModel()

    object Success : StateModel()
}
