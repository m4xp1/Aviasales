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

    private val filter by lazy { WithoutFiltrationFilter() }

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
        return super.getItem(position) as CityModel
    }

    fun setItems(items: List<CityModel>) = clear().also { addAll(items) }

    override fun getFilter(): Filter {
        return filter
    }

    private inner class WithoutFiltrationFilter : Filter() {

        override fun convertResultToString(resultValue: Any?) = (resultValue as CityModel).name

        override fun performFiltering(constraint: CharSequence?): FilterResults? {
            val results = FilterResults()
            results.count = count
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            /* do nothing */
        }
    }
}
