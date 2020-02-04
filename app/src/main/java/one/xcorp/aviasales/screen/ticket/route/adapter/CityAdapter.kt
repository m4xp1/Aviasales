package one.xcorp.aviasales.screen.ticket.route.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import javax.inject.Inject

class CityAdapter @Inject constructor(
    private val inflater: LayoutInflater
) : ArrayAdapter<CityModel>(inflater.context, 0) {

    private val filter by lazy { OffFilter() }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder = if (convertView != null) {
            convertView.tag as CityViewHolder
        } else {
            CityViewHolder(inflater, parent).apply { itemView.tag = this }
        }

        viewHolder.bindTo(getItem(position))
        return viewHolder.itemView
    }

    override fun getItem(position: Int): CityModel {
        return requireNotNull(super.getItem(position))
    }

    override fun getFilter(): Filter {
        return filter
    }

    private inner class OffFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            return null
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            /* do nothing */
        }
    }
}
