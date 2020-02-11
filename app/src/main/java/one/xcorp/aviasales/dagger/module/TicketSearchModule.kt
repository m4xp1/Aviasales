package one.xcorp.aviasales.dagger.module

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import one.xcorp.aviasales.extension.map.LatLngInterpolator
import one.xcorp.aviasales.screen.ticket.search.TicketSearchViewModel
import one.xcorp.aviasales.screen.ticket.search.graphic.route.BezierRouteGenerator
import one.xcorp.aviasales.screen.ticket.search.graphic.route.GeodesicRouteGenerator
import one.xcorp.aviasales.screen.ticket.search.graphic.route.RouteGenerator
import one.xcorp.mvvm.factory.ViewModelKey

@Module
class TicketSearchModule {

    @Provides
    fun routeGenerator(): RouteGenerator = BezierRouteGenerator()

    @Provides
    fun latLngInterpolator(): LatLngInterpolator = LatLngInterpolator.Linear()

    @Provides
    @IntoMap
    @ViewModelKey(TicketSearchViewModel::class)
    fun ticketSearchViewModel(viewModel: TicketSearchViewModel): ViewModel = viewModel
}
