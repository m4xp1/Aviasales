package one.xcorp.aviasales.extension

import android.animation.ValueAnimator
import android.graphics.Point
import android.graphics.Rect
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.SphericalUtil

fun Rect.toProjection(projection: Projection): LatLngBounds {
    val southWest = projection.fromScreenLocation(Point(left, bottom))
    val northEast = projection.fromScreenLocation(Point(right, top))

    return LatLngBounds.builder()
        .include(southWest)
        .include(northEast)
        .build()
}

fun LatLngBounds.contains(bounds: LatLngBounds): Boolean =
    contains(bounds.southwest) && contains(bounds.northeast)

fun LatLng.interpolate(dest: LatLng, fraction: Float): LatLng =
    SphericalUtil.interpolate(this, dest, fraction.toDouble())

fun LatLng.bearingTo(dest: LatLng): Float = SphericalUtil.computeHeading(this, dest).toFloat()

fun Marker.animate(dest: LatLng): ValueAnimator = animate(listOf(dest))

fun Marker.animate(route: List<LatLng>): ValueAnimator {
    require(route.isNotEmpty()) { "Cannot animate marker on empty route" }

    val positions = arrayListOf(position, *route.toTypedArray())
    val values = (0..route.size).toList().map { it.toFloat() }

    val animator = ValueAnimator.ofFloat(*values.toFloatArray())
    animator.addUpdateListener { animation ->
        val value = animation.animatedValue as Float

        val index = value.toInt().coerceIn(route.indices)
        val fraction = value - index

        if (animation.currentPlayTime >= animation.duration) {
            position = positions[index].interpolate(route.last(), animation.animatedFraction)
        } else {
            position = positions[index].interpolate(positions[index + 1], fraction)
            if (fraction != 1f) { // keep latest bearing
                rotation = position.bearingTo(positions[index + 1])
            }
        }
    }

    return animator
}
