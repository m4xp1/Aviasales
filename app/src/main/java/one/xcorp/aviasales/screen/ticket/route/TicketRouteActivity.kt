package one.xcorp.aviasales.screen.ticket.route

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.widget.AutoCompleteTextView
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_ticket_route.*
import one.xcorp.aviasales.Application.Dependencies.applicationComponent
import one.xcorp.aviasales.R
import one.xcorp.aviasales.dagger.TicketRouteComponent
import one.xcorp.aviasales.dagger.TicketRouteComponent.Factory
import one.xcorp.aviasales.extension.*
import one.xcorp.aviasales.screen.ticket.route.adapter.CityAdapter
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.aviasales.screen.ticket.route.model.InputModel
import one.xcorp.aviasales.screen.ticket.route.model.InputModel.NotSelected
import one.xcorp.aviasales.screen.ticket.search.marker.AirportIconGenerator
import one.xcorp.didy.Injector
import one.xcorp.didy.holder.injectWith
import one.xcorp.lifecycle.observe
import one.xcorp.mvvm.didy.DidyActivity
import one.xcorp.mvvm.model.InputState
import one.xcorp.mvvm.model.InputState.Entered
import one.xcorp.mvvm.model.InputState.NotEntered
import one.xcorp.mvvm.obtainViewModel
import javax.inject.Inject

class TicketRouteActivity : DidyActivity() {

    @Inject
    lateinit var departureAdapter: CityAdapter
    @Inject
    lateinit var destinationAdapter: CityAdapter
    @Inject
    lateinit var airportIconGenerator: AirportIconGenerator

    private lateinit var viewModel: TicketRouteViewModel

    private val injector = Injector<TicketRouteComponent> { it.inject(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_route)
    }

    override fun onInject(savedInstanceState: Bundle?) = applicationComponent
        .ticketRouteComponentHolder
        .injectWith(injector, Factory::createComponent)

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        configureDepartureEdit()
        configureDestinationEdit()

        viewModel = obtainViewModel(viewModelFactory) {
            observe(departureCityCompletion, ::invalidateDepartureCompletion)
            observe(destinationCityCompletion, ::invalidateDestinationCompletion)
            observe(inputState, ::invalidateInputState)
            findButton.watchClicks(::findTickets)
        }
    }

    private fun configureDepartureEdit(): Unit = with(departureEdit) {
        setAdapter(departureAdapter)
        watchTouches(::showCompletion)
        watchFocusChanges(::changeCompletionVisibility)
        watchTextChanges {
            viewModel.setSelectedDepartureCity(null)
            viewModel.obtainDepartureCompletion(it)
        }
        watchItemClick(::selectDepartureItem)
        watchEditorAction(IME_ACTION_NEXT) {
            if (!isPopupShowing) {
                showCompletion()
            } else {
                selectDepartureItem(0)
            }
        }
    }

    private fun configureDestinationEdit(): Unit = with(destinationEdit) {
        setAdapter(destinationAdapter)
        watchTouches(::showCompletion)
        watchFocusChanges(::changeCompletionVisibility)
        watchTextChanges {
            viewModel.setSelectedDestinationCity(null)
            viewModel.obtainDestinationCompletion(it)
        }
        watchItemClick(::selectDestinationItem)
        watchEditorAction(IME_ACTION_DONE) {
            if (isPopupShowing) {
                selectDestinationItem(0)
            }
            findButton.performClick()
        }
    }

    private fun invalidateDepartureCompletion(items: List<CityModel>) =
        invalidateCityCompletion(departureEdit, departureAdapter, items)

    private fun invalidateDestinationCompletion(items: List<CityModel>) =
        invalidateCityCompletion(destinationEdit, destinationAdapter, items)

    private fun invalidateCityCompletion(
        edit: AutoCompleteTextView,
        adapter: CityAdapter,
        items: List<CityModel>
    ) {
        adapter.setItems(items)
        if (edit.isPopupShowing) {
            edit.refreshAutoCompleteResults()
        }
    }

    private fun selectDepartureItem(position: Int) {
        val city = selectCityItem(departureEdit, departureAdapter, position)
        viewModel.setSelectedDepartureCity(city)

        destinationEdit.requestFocus()
    }

    private fun selectDestinationItem(position: Int) {
        val city = selectCityItem(destinationEdit, destinationAdapter, position)
        viewModel.setSelectedDestinationCity(city)

        destinationEdit.dismissDropDown()
    }

    private fun selectCityItem(
        edit: AutoCompleteTextView,
        adapter: CityAdapter,
        position: Int
    ): CityModel {
        val city = adapter.getItem(position)
        val label = adapter.getLabel(city)

        edit.setText(label, false)
        edit.setSelection(label.length)

        return city
    }

    private fun invalidateInputState(input: InputModel) {
        departureView.applyCityInputState(input.departure)
        destinationView.applyCityInputState(input.destination)
    }

    private fun TextInputLayout.applyCityInputState(state: InputState<CityModel>): Unit? =
        when (state) {
            is NotEntered -> {
                hideError()
                editText?.setAirportLabel(null)
            }
            is Entered -> {
                hideError()
                editText?.setAirportLabel(state.value.getAirportName())
            }
            is InputState.Error -> when (state) {
                is NotSelected -> showError(R.string.ticket_route_activity_error_select_value)
                else -> showError()
            }
        }

    private fun TextView.setAirportLabel(airportName: String?) {
        val airportLabel = airportName?.let {
            BitmapDrawable(resources, airportIconGenerator.makeIcon(airportName))
        }
        updateCompoundDrawable(end = airportLabel)
    }
}
