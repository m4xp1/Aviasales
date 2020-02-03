package one.xcorp.aviasales.screen.ticket.search.marker

import android.content.Context
import android.graphics.Bitmap
import com.google.maps.android.ui.IconGenerator
import one.xcorp.aviasales.R.attr.markerTextAppearance
import one.xcorp.aviasales.R.drawable.airport_marker_background
import one.xcorp.aviasales.R.style.MarkerTextAppearance
import one.xcorp.aviasales.extension.getThemeAttribute
import javax.inject.Inject

class AirportIconGenerator @Inject constructor(
    context: Context
) {

    private val iconGenerator = IconGenerator(context).apply {
        setContentPadding(0, 0, 0, 0)
        setTextAppearance(context.getThemeAttribute(markerTextAppearance, MarkerTextAppearance))
        setBackground(context.getDrawable(airport_marker_background))
    }

    fun makeIcon(iata: String): Bitmap = iconGenerator.makeIcon(iata)
}
