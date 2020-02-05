package one.xcorp.aviasales.dagger

import dagger.BindsInstance
import dagger.Subcomponent
import one.xcorp.aviasales.dagger.module.ActivityModule
import one.xcorp.aviasales.dagger.module.TicketSearchModule
import one.xcorp.aviasales.dagger.qualifier.DepartureCity
import one.xcorp.aviasales.dagger.qualifier.DestinationCity
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.aviasales.screen.ticket.search.TicketSearchActivity
import one.xcorp.didy.holder.SingleComponentHolder
import one.xcorp.didy.scope.ActivityScope

@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class,
        TicketSearchModule::class
    ]
)
interface TicketSearchComponent {

    fun inject(instance: TicketSearchActivity)

    @Subcomponent.Factory
    interface Factory {

        fun createComponent(
            @BindsInstance @DepartureCity departureCity: CityModel,
            @BindsInstance @DestinationCity destinationCity: CityModel
        ): TicketSearchComponent
    }

    class Holder(factory: Factory) : SingleComponentHolder<Factory, TicketSearchComponent>(factory)
}
