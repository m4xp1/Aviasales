package one.xcorp.aviasales.screen.ticket.route

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.inputmethod.EditorInfo.IME_ACTION_DONE
import android.widget.Adapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_ticket_route.*
import one.xcorp.aviasales.R
import one.xcorp.aviasales.R.string.ticket_route_activity_error_hint_input_value
import one.xcorp.aviasales.extension.hideError
import one.xcorp.aviasales.extension.showError
import one.xcorp.aviasales.extension.updateCompoundDrawable
import one.xcorp.aviasales.screen.ticket.route.adapter.CityAdapter
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.aviasales.screen.ticket.search.marker.AirportIconGenerator

class TicketRouteActivity : AppCompatActivity() {

    private val airportIconGenerator by lazy { AirportIconGenerator(this) }

    private val departureAdapter by lazy { CityAdapter(layoutInflater) }
    private val destinationAdapter by lazy { CityAdapter(layoutInflater) }

    private var departureCity: CityModel? = null
    private var destinationCity: CityModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_route)

        departureEdit.setAdapter(departureAdapter)
        watchFocusChanges(departureEdit)
        departureEdit.setOnItemClickListener { _, _, position, _ ->
            invalidateDepartureSelection(position)
            destinationEdit.requestFocus()
        }
        watchTextChanges(departureView, ::invalidateDepartureSelection)

        destinationEdit.setAdapter(destinationAdapter)
        watchFocusChanges(destinationEdit)
        destinationEdit.setOnItemClickListener { _, _, position, _ ->
            invalidateDestinationSelection(position)
        }
        watchTextChanges(destinationView, ::invalidateDestinationSelection)
        destinationEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_DONE) {
                destinationEdit.dismissDropDown()
                findButton.performClick()
                true
            } else false
        }

        findButton.setOnClickListener { onStartFindTickets() }
    }

    private fun invalidateDepartureSelection(position: Int) {
        departureCity = if (position == -1) null else departureAdapter.getItem(position)
        setAirportLabel(departureEdit, departureCity?.getAirportName())
    }

    private fun invalidateDestinationSelection(position: Int) {
        destinationCity = if (position == -1) null else destinationAdapter.getItem(position)
        setAirportLabel(destinationEdit, destinationCity?.getAirportName())
    }

    private fun setAirportLabel(view: TextView, airportName: String?) {
        val airportLabel = airportName?.let {
            BitmapDrawable(resources, airportIconGenerator.makeIcon(airportName))
        }
        view.updateCompoundDrawable(end = airportLabel)
    }

    private fun watchTextChanges(
        view: TextInputLayout,
        invalidateItemSelection: (Int) -> Unit
    ) = view.editText?.let { edit ->
        edit.addTextChangedListener(afterTextChanged = {
            view.hideError()

            val adapter = (edit as? AutoCompleteTextView)?.adapter
            adapter?.let {
                val position = adapter.findItem(edit.text.toString())
                invalidateItemSelection(position)
            }
        })
    }

    private fun watchFocusChanges(view: AutoCompleteTextView) {
        view.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                view.showDropDown()
            } else {
                view.dismissDropDown()
            }
        }
    }

    private fun onStartFindTickets() {
        departureView.hideError()
        destinationView.hideError()

        if (destinationCity == null) {
            destinationView.showError(ticket_route_activity_error_hint_input_value)
            destinationEdit.requestFocus()
        }
        if (departureCity == null) {
            departureView.showError(ticket_route_activity_error_hint_input_value)
            departureView.requestFocus()
        }

        if (departureView.isErrorEnabled || destinationView.isErrorEnabled) {
            return
        }
    }

    private fun Adapter.findItem(item: String): Int = (0 until count)
        .map { getItem(it) }
        .indexOfFirst { it.toString() == item }
}
