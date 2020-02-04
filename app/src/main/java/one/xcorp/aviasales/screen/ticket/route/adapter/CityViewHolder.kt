package one.xcorp.aviasales.screen.ticket.route.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_city.view.*
import one.xcorp.aviasales.R
import one.xcorp.aviasales.screen.ticket.route.model.AirportModel

class CityViewHolder private constructor(
    val itemView: View
) {

    fun bindTo(item: AirportModel) {
        itemView.fullNameView.text = item.fullName
        itemView.iataView.text = item.iata
    }

    companion object {

        operator fun invoke(inflater: LayoutInflater, parent: ViewGroup): CityViewHolder {
            val view = inflater.inflate(R.layout.list_item_city, parent, false)
            return CityViewHolder(view)
        }
    }
}
