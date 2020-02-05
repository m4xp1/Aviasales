package one.xcorp.aviasales.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import one.xcorp.aviasales.dagger.TicketRouteComponent
import one.xcorp.aviasales.dagger.TicketSearchComponent
import one.xcorp.didy.scope.ApplicationScope

@Module
class ApplicationModule {

    @Provides
    @ApplicationScope
    fun context(application: Application): Context = application

    @Provides
    @ApplicationScope
    fun ticketRouteComponentHolder(
        factory: TicketRouteComponent.Factory
    ): TicketRouteComponent.Holder = TicketRouteComponent.Holder(factory)

    @Provides
    @ApplicationScope
    fun ticketSearchComponentHolder(
        factory: TicketSearchComponent.Factory
    ): TicketSearchComponent.Holder = TicketSearchComponent.Holder(factory)
}
