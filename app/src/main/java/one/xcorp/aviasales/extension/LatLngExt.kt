package one.xcorp.aviasales.extension

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

fun LatLng.interpolate(dest: LatLng, fraction: Float): LatLng =
    SphericalUtil.interpolate(this, dest, fraction.toDouble())

fun LatLng.bearingTo(dest: LatLng): Float = SphericalUtil.computeHeading(this, dest).toFloat()
