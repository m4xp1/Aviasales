package one.xcorp.aviasales.extension.map

import android.animation.ValueAnimator
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.SphericalUtil
import one.xcorp.aviasales.extension.map.LatLngInterpolator.Spherical

fun LatLngBounds.contains(bounds: LatLngBounds): Boolean =
    contains(bounds.southwest) && contains(bounds.northeast)

fun LatLng.bearingTo(dest: LatLng): Float = SphericalUtil.computeHeading(this, dest).toFloat()

fun Marker.animate(dest: LatLng, interpolator: LatLngInterpolator = Spherical()): ValueAnimator =
    animate(listOf(position, dest), interpolator)

fun Marker.animate(
    route: List<LatLng>,
    interpolator: LatLngInterpolator = Spherical()
): ValueAnimator {
    require(route.isNotEmpty()) { "Cannot animate marker on empty route" }

    val values = route.indices.toList().map { it.toFloat() }
    val animator = ValueAnimator.ofFloat(*values.toFloatArray())
    animator.addUpdateListener { animation ->
        val value = animation.animatedValue as Float

        val index = value.toInt().coerceIn(route.indices)
        val fraction = value - index

        if (animation.currentPlayTime >= animation.duration) {
            position = interpolator.interpolate(
                route[index], route.last(), animation.animatedFraction
            )
        } else {
            position = interpolator.interpolate(route[index], route[index + 1], fraction)
            if (fraction != 1f) { // keep latest bearing
                rotation = position.bearingTo(route[index + 1])
            }
        }
    }

    return animator
}
