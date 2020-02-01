package one.xcorp.aviasales.screen.ticket.search.marker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View.MeasureSpec.UNSPECIFIED
import android.view.View.MeasureSpec.makeMeasureSpec
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.core.view.drawToBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import one.xcorp.aviasales.R
import javax.inject.Inject

class AirportMarkerOptionsFactory @Inject constructor(
    private val inflater: LayoutInflater
) {

    private val view: TextView by lazy { inflateAirportMarkerView() }
    private val measureSpec: Int = makeMeasureSpec(0, UNSPECIFIED)

    fun create(label: CharSequence, position: LatLng): MarkerOptions {
        invalidateView(label)

        return MarkerOptions()
            .position(position)
            .icon(BitmapDescriptorFactory.fromBitmap(view.drawToBitmap()))
    }

    private fun invalidateView(label: CharSequence) = view.apply {
        text = label

        measure(measureSpec, measureSpec)
        layout(0, 0, measuredWidth, measuredHeight)
    }

    @SuppressLint("InflateParams")
    private fun inflateAirportMarkerView(): TextView {
        val view = inflater.inflate(R.layout.airport_marker, null) as TextView
        view.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        return view
    }
}
