package one.xcorp.aviasales.screen.ticket.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import io.reactivex.Observable
import one.xcorp.aviasales.R
import one.xcorp.aviasales.dagger.qualifier.DepartureCity
import one.xcorp.aviasales.dagger.qualifier.DestinationCity
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.aviasales.screen.ticket.search.model.StateModel
import one.xcorp.aviasales.screen.ticket.search.model.StateModel.Loading
import one.xcorp.aviasales.screen.ticket.search.model.StateModel.Success
import one.xcorp.mvvm.rx.RxViewModel
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject
import kotlin.random.Random.Default.nextLong

class TicketSearchViewModel @Inject constructor(
    context: Context,
    @DepartureCity
    private val departureCity: CityModel,
    @DestinationCity
    private val destinationCity: CityModel
) : RxViewModel() {

    val loadingState: LiveData<StateModel>

    private val maxDuration = context.resources
        .getInteger(R.integer.ticket_search_activity_average_animation_duration)
        .let { it * 0.6f }
        .toLong()

    init {
        loadingState = searchTickets(nextLong(maxDuration)).toLiveData()
    }

    private fun searchTickets(delay: Long): Observable<StateModel> =
        Observable.just<StateModel>(Loading)
            .concatWith(
                Observable.interval(delay, SECONDS)
                    .map<StateModel> { Success }
                    .take(1)
            ).doOnNext {
                when (it) {
                    is Loading -> Log.i(TAG, "Ticket search started: departureCity=$departureCity, destinationCity=$destinationCity")
                    is Success -> Log.i(TAG, "Ticket search completed: duration=$delay")
                }
            }

    companion object {

        private const val TAG = "TicketSearch"
    }
}
