package one.xcorp.aviasales.screen.ticket.search.graphic.marker

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import com.google.maps.android.ui.IconGenerator
import one.xcorp.aviasales.R.attr.markerTextAppearance
import one.xcorp.aviasales.R.drawable.airport_marker_background
import one.xcorp.aviasales.R.style.MarkerTextAppearance
import one.xcorp.aviasales.extension.getThemeAttribute
import javax.inject.Inject

class AirportIconGenerator @Inject constructor(
    activity: AppCompatActivity
) {

    private val iconGenerator = IconGenerator(activity).apply {
        setContentPadding(0, 0, 0, 0)
        setTextAppearance(activity.getThemeAttribute(markerTextAppearance, MarkerTextAppearance))
        setBackground(activity.getDrawable(airport_marker_background))
    }

    fun makeIcon(iata: String): Bitmap = iconGenerator.makeIcon(iata)
}
