package one.xcorp.aviasales.screen.ticket.search.marker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
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

@SuppressLint("InflateParams")
class AirportMarkerFactory @Inject constructor(
    private val inflater: LayoutInflater
) {

    private val view: TextView by lazy {
        inflater.inflate(R.layout.airport_marker, null).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        } as TextView
    }

    private val measureSpec: Int = makeMeasureSpec(0, UNSPECIFIED)

    fun createOptions(label: CharSequence, position: LatLng): MarkerOptions {
        view.text = label
        view.refresh()

        return MarkerOptions()
            .position(position)
            .icon(BitmapDescriptorFactory.fromBitmap(view.drawToBitmap()))
            .anchor(0.5f, 0.5f)
    }

    private fun View.refresh() {
        measure(measureSpec, measureSpec)
        layout(0, 0, measuredWidth, measuredHeight)
    }
}
