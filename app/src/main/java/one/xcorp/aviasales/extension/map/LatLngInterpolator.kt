package one.xcorp.aviasales.extension.map

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

interface LatLngInterpolator {

    fun interpolate(from: LatLng, to: LatLng, fraction: Float): LatLng

    class Linear : LatLngInterpolator {

        override fun interpolate(from: LatLng, to: LatLng, fraction: Float): LatLng {
            val lat: Double = (to.latitude - from.latitude) * fraction + from.latitude
            val lng: Double = (to.longitude - from.longitude) * fraction + from.longitude
            return LatLng(lat, lng)
        }
    }

    class Spherical : LatLngInterpolator {

        override fun interpolate(from: LatLng, to: LatLng, fraction: Float): LatLng {
            return SphericalUtil.interpolate(from, to, fraction.toDouble())
        }
    }
}
