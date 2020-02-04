package one.xcorp.aviasales.dagger

import dagger.Subcomponent
import one.xcorp.aviasales.dagger.module.ActivityModule
import one.xcorp.aviasales.dagger.module.TicketRouteModule
import one.xcorp.aviasales.screen.ticket.route.TicketRouteActivity
import one.xcorp.didy.holder.SingleComponentHolder
import one.xcorp.didy.scope.ActivityScope

@ActivityScope
@Subcomponent(
    modules = [
        ActivityModule::class,
        TicketRouteModule::class
    ]
)
interface TicketRouteComponent {

    fun inject(instance: TicketRouteActivity)

    @Subcomponent.Factory
    interface Factory {

        fun createComponent(): TicketRouteComponent
    }

    class Holder(factory: Factory) : SingleComponentHolder<Factory, TicketRouteComponent>(factory)
}
