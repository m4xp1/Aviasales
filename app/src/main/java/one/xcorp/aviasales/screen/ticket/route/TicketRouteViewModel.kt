package one.xcorp.aviasales.screen.ticket.route

import androidx.lifecycle.LiveData
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import one.xcorp.aviasales.domain.usecase.city.find.FindCityUseCase
import one.xcorp.aviasales.screen.ticket.route.mapper.toCityModel
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.mvvm.rx.RxViewModel
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class TicketRouteViewModel @Inject constructor(
    private val findCity: FindCityUseCase
) : RxViewModel() {

    val departureCityCompletion: LiveData<List<CityModel>>
    val destinationCityCompletion: LiveData<List<CityModel>>

    private val departureCityCompletionSubject = PublishSubject.create<String>()
    private val destinationCityCompletionSubject = PublishSubject.create<String>()

    init {
        departureCityCompletion =
            findCityObservable(departureCityCompletionSubject).toLiveData()
        destinationCityCompletion =
            findCityObservable(destinationCityCompletionSubject).toLiveData()
    }

    fun obtainDepartureCompletion(query: String) = departureCityCompletionSubject.onNext(query)

    fun obtainDestinationCompletion(query: String) = destinationCityCompletionSubject.onNext(query)

    private fun findCityObservable(subject: Subject<String>) = subject
        .debounce(SEARCH_DELAY_IN_MILLIS, MILLISECONDS)
        .switchMapSingle { findCity(it) }
        .map { it.toCityModel() }
        .retry()

    companion object {

        private const val SEARCH_DELAY_IN_MILLIS = 500L
    }
}
