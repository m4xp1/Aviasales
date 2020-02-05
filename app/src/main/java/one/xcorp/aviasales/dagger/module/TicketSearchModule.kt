package one.xcorp.aviasales.dagger.module

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import one.xcorp.aviasales.screen.ticket.search.TicketSearchViewModel
import one.xcorp.mvvm.factory.ViewModelKey

@Module
class TicketSearchModule {

    @Provides
    @IntoMap
    @ViewModelKey(TicketSearchViewModel::class)
    fun ticketSearchViewModel(viewModel: TicketSearchViewModel): ViewModel = viewModel
}
