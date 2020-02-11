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
    animate(listOf(dest), interpolator)

fun Marker.animate(
    route: List<LatLng>,
    interpolator: LatLngInterpolator = Spherical()
): ValueAnimator {
    require(route.isNotEmpty()) { "Cannot animate marker on empty route" }

    val positions = arrayListOf(position, *route.toTypedArray())
    val values = (0..route.size).toList().map { it.toFloat() }

    val animator = ValueAnimator.ofFloat(*values.toFloatArray())
    animator.addUpdateListener { animation ->
        val value = animation.animatedValue as Float

        val index = value.toInt().coerceIn(route.indices)
        val fraction = value - index

        if (animation.currentPlayTime >= animation.duration) {
            position = interpolator.interpolate(
                positions[index], route.last(), animation.animatedFraction
            )
        } else {
            position = interpolator.interpolate(positions[index], positions[index + 1], fraction)
            if (fraction != 1f) { // keep latest bearing
                rotation = position.bearingTo(positions[index + 1])
            }
        }
    }

    return animator
}
