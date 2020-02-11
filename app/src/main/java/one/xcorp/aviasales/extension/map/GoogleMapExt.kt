package one.xcorp.aviasales.extension.map

import android.animation.ValueAnimator
import android.graphics.Point
import android.graphics.Rect
import com.google.android.gms.maps.Projection
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.SphericalUtil
import one.xcorp.aviasales.extension.map.LatLngInterpolator.Spherical

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

fun LatLng.distanceTo(dest: LatLng): Double = SphericalUtil.computeDistanceBetween(this, dest)

fun LatLng.bearingTo(dest: LatLng): Float = SphericalUtil.computeHeading(this, dest).toFloat()

fun Marker.animate(dest: LatLng, interpolator: LatLngInterpolator = Spherical()): ValueAnimator =
    animate(listOf(dest), interpolator)

fun Marker.animate(
    route: List<LatLng>,
    interpolator: LatLngInterpolator = Spherical()
): ValueAnimator {
    require(route.isNotEmpty()) { "Cannot animate marker on empty route" }

    val points = arrayListOf(position, *route.toTypedArray())

    val distances = mutableListOf(0.0)
    for (i in 0 until points.lastIndex) {
        val distanceToNextPoint = points[i].distanceTo(points[i + 1])
        distances.add(distances.last() + distanceToNextPoint)
    }
    val values = distances
        .map { (it / distances.last()).toFloat() }
        .toFloatArray()

    val animator = ValueAnimator.ofFloat(*values)
    animator.addUpdateListener { animation ->
        val value = animation.animatedValue as Float
        val index = values.binarySearch(value)

        val from: Int
        val to: Int
        if (index < 0) {
            from = -(index + 2)
            to = -(index + 1)
        } else {
            from = index
            to = index + 1
        }

        if (animation.currentPlayTime >= animation.duration || value == 1f) {
            position = interpolator.interpolate(
                points[from], route.last(), animation.animatedFraction
            )
        } else {
            val fraction = (value - values[from]) / (values[to] - values[from])
            position = interpolator.interpolate(points[from], points[to], fraction)
            if (fraction != 1f) { // keep latest bearing
                rotation = position.bearingTo(points[to])
            }
        }
    }

    return animator
}
