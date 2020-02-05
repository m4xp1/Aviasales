package one.xcorp.aviasales.dagger.module

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import one.xcorp.aviasales.domain.usecase.city.find.FindCityUseCase
import one.xcorp.aviasales.domain.usecase.city.find.FindCityWithAirportsUseCase
import one.xcorp.aviasales.navigation.ticket.ActivityTicketNavigator
import one.xcorp.aviasales.navigation.ticket.TicketNavigator
import one.xcorp.aviasales.screen.ticket.route.TicketRouteViewModel
import one.xcorp.mvvm.factory.ViewModelKey

@Module
class TicketRouteModule {

    @Provides
    fun ticketNavigator(navigator: ActivityTicketNavigator): TicketNavigator = navigator

    @Provides
    fun findCityUseCase(useCase: FindCityWithAirportsUseCase): FindCityUseCase = useCase

    @Provides
    @IntoMap
    @ViewModelKey(TicketRouteViewModel::class)
    fun cardsViewViewModel(viewModel: TicketRouteViewModel): ViewModel = viewModel
}
