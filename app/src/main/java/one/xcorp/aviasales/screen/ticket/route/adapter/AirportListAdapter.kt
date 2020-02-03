package one.xcorp.aviasales.screen.ticket.route.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import one.xcorp.aviasales.screen.ticket.route.model.AirportModel
import javax.inject.Inject

class AirportListAdapter @Inject constructor(
    private val inflater: LayoutInflater
) : ArrayAdapter<AirportModel>(inflater.context, 0) {

    private val filter by lazy { AirportFilter() }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder = if (convertView != null) {
            convertView.tag as AirportViewHolder
        } else {
            AirportViewHolder(inflater, parent).apply { itemView.tag = this }
        }

        viewHolder.bindTo(getItem(position))
        return viewHolder.itemView
    }

    override fun getItem(position: Int): AirportModel {
        return requireNotNull(super.getItem(position))
    }

    override fun getFilter(): Filter {
        return filter
    }

    private inner class AirportFilter : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            return null
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            /* do nothing */
        }
    }
}
