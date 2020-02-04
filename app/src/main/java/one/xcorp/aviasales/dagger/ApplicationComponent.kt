package one.xcorp.aviasales.dagger

import android.app.Application
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import one.xcorp.aviasales.dagger.module.ApplicationModule
import one.xcorp.aviasales.data.dagger.DataComponent
import one.xcorp.didy.scope.ApplicationScope

@ApplicationScope
@Component(
    dependencies = [
        DataComponent::class
    ],
    modules = [
        ApplicationModule::class
    ]
)
interface ApplicationComponent {

    val context: Context

    val ticketRouteComponentFactory: TicketRouteComponent.Factory

    val ticketRouteComponentHolder: TicketRouteComponent.Holder

    @Component.Factory
    interface Factory {

        fun createComponent(
            @BindsInstance application: Application,
            dataComponent: DataComponent
        ): ApplicationComponent
    }
}
