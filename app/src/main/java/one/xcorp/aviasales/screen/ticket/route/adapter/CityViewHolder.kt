package one.xcorp.aviasales.screen.ticket.route.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_item_city.view.*
import one.xcorp.aviasales.R
import one.xcorp.aviasales.screen.ticket.route.model.CityModel

class CityViewHolder private constructor(
    val itemView: View
) {

    fun bindTo(item: CityModel) {
        itemView.fullNameView.text = item.fullName

        val iata = item.iata.firstOrNull()
        if (iata == null) {
            itemView.iataView.text = null
            itemView.iataView.visibility = INVISIBLE
        } else {
            itemView.iataView.text = iata
            itemView.iataView.visibility = VISIBLE
        }
    }

    companion object {

        operator fun invoke(inflater: LayoutInflater, parent: ViewGroup): CityViewHolder {
            val view = inflater.inflate(R.layout.list_item_city, parent, false)
            return CityViewHolder(view)
        }
    }
}
