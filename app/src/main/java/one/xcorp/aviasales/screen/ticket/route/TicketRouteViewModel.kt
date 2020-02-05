package one.xcorp.aviasales.screen.ticket.route

import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.lifecycle.LiveData
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import one.xcorp.aviasales.Application.Dependencies.applicationComponent
import one.xcorp.aviasales.domain.usecase.city.find.FindCityUseCase
import one.xcorp.aviasales.screen.ticket.route.mapper.toCityModel
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.aviasales.screen.ticket.route.model.InputModel
import one.xcorp.aviasales.screen.ticket.route.model.InputModel.NotSelected
import one.xcorp.mvvm.model.InputState
import one.xcorp.mvvm.model.InputState.NotEntered
import one.xcorp.mvvm.rx.RxViewModel
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class TicketRouteViewModel @Inject constructor(
    private val findCity: FindCityUseCase
) : RxViewModel() {

    val departureCityCompletion: LiveData<List<CityModel>>
    val destinationCityCompletion: LiveData<List<CityModel>>
    val inputState: LiveData<InputModel>

    private val departureCityCompletionSubject = PublishSubject.create<String>()
    private val destinationCityCompletionSubject = PublishSubject.create<String>()
    private val inputStateSubject = PublishSubject.create<InputModel>()

    init {
        departureCityCompletion =
            findCityObservable(departureCityCompletionSubject).toLiveData()
        destinationCityCompletion =
            findCityObservable(destinationCityCompletionSubject).toLiveData()
        inputState = inputStateSubject.startWith(InputModel()).toLiveData()
    }

    fun obtainDepartureCompletion(query: String) =
        departureCityCompletionSubject.onNext(query)

    fun obtainDestinationCompletion(query: String) =
        destinationCityCompletionSubject.onNext(query)

    fun setSelectedDepartureCity(city: CityModel?): Unit? = inputState.value?.run {
        inputStateSubject.onNext(copy(departure = InputState(city)))
    }

    fun setSelectedDestinationCity(city: CityModel?): Unit? = inputState.value?.run {
        inputStateSubject.onNext(copy(destination = InputState(city)))
    }

    fun findTickets(): Unit? = inputState.value?.let { currentInputState ->
        var checkedInputState = currentInputState
        if (currentInputState.departure == NotEntered) {
            checkedInputState = checkedInputState.copy(departure = NotSelected)
        }
        if (currentInputState.destination == NotEntered) {
            checkedInputState = checkedInputState.copy(destination = NotSelected)
        }

        if (checkedInputState.isContainsErrors) {
            inputStateSubject.onNext(checkedInputState)
        } else {
            Toast.makeText(applicationComponent.context, "findTickets", LENGTH_SHORT).show()
        }
    }

    private fun findCityObservable(subject: Subject<String>) = subject
        .debounce(SEARCH_DELAY_IN_MILLIS, MILLISECONDS)
        .switchMapSingle { findCity(it) }
        .map { it.toCityModel() }
        .retry()

    companion object {

        private const val SEARCH_DELAY_IN_MILLIS = 500L
    }
}
