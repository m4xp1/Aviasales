package one.xcorp.aviasales.screen.ticket.search

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.animation.Interpolator
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat.getColor
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.google.android.gms.maps.CameraUpdateFactory.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromBitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory.fromResource
import com.google.android.gms.maps.model.MapStyleOptions.loadRawResourceStyle
import one.xcorp.aviasales.Application.Dependencies.applicationComponent
import one.xcorp.aviasales.R
import one.xcorp.aviasales.R.integer.ticket_search_activity_average_animation_duration
import one.xcorp.aviasales.R.integer.ticket_search_activity_final_animation_duration
import one.xcorp.aviasales.dagger.TicketSearchComponent
import one.xcorp.aviasales.dagger.qualifier.DepartureCity
import one.xcorp.aviasales.dagger.qualifier.DestinationCity
import one.xcorp.aviasales.extension.getDisplayRect
import one.xcorp.aviasales.extension.map.animate
import one.xcorp.aviasales.extension.map.bearingTo
import one.xcorp.aviasales.extension.map.contains
import one.xcorp.aviasales.extension.map.toProjection
import one.xcorp.aviasales.screen.ticket.route.mapper.toLatLng
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.aviasales.screen.ticket.search.marker.AirportIconGenerator
import one.xcorp.aviasales.screen.ticket.search.model.StateModel
import one.xcorp.didy.Injector
import one.xcorp.didy.holder.injectWith
import one.xcorp.lifecycle.observe
import one.xcorp.mvvm.didy.DidyActivity
import one.xcorp.mvvm.obtainViewModel
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class TicketSearchActivity : DidyActivity() {

    @Inject
    @DepartureCity
    lateinit var departureCity: CityModel

    @Inject
    @DestinationCity
    lateinit var destinationCity: CityModel

    @Inject
    lateinit var airportIconGenerator: AirportIconGenerator

    private lateinit var viewModel: TicketSearchViewModel

    private lateinit var googleMap: GoogleMap
    private lateinit var planeMarker: Marker

    private var planeMarkerAnimator: ValueAnimator? = null
    private var planeMarkerAnimatorPlayTime: Long = 0L
    private var isTrackingMarkerEnabled = false
    private var isOnTouchStarted = false

    private val dotLineGap by lazy {
        resources.getDimensionPixelSize(R.dimen.ticket_search_activity_map_route_gap).toFloat()
    }

    private val dotLineWidth by lazy {
        resources.getDimensionPixelSize(R.dimen.ticket_search_activity_map_route_width).toFloat()
    }

    private val injector = Injector<TicketSearchComponent> { it.inject(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_search)

        viewModel = obtainViewModel(viewModelFactory)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(::onMapReady)
    }

    override fun onInject(savedInstanceState: Bundle?) = applicationComponent
        .ticketSearchComponentHolder
        .injectWith(injector) {
            createComponent(
                requireNotNull(intent.getParcelableExtra(KEY_DEPARTURE_CITY)),
                requireNotNull(intent.getParcelableExtra(KEY_DESTINATION_CITY))
            )
        }

    private fun onMapReady(map: GoogleMap) {
        googleMap = map.apply {
            uiSettings.apply {
                isCompassEnabled = false
                isRotateGesturesEnabled = false
                isMapToolbarEnabled = false
            }
            setMapStyle(loadRawResourceStyle(applicationContext, R.raw.google_map_style))
        }

        val markerBounds = setInitialMapMarkers()
        setInitialCameraPosition(markerBounds) {
            observe(viewModel.loadingState, ::invalidateLoadingState)
        }
    }

    private fun setInitialMapMarkers(): LatLngBounds {
        val departureLocation = departureCity.location.toLatLng()
        val destinationLocation = destinationCity.location.toLatLng()
        val bearing = departureLocation.bearingTo(destinationLocation)

        val pointsSet = mutableSetOf<LatLng>()

        addPlaneRoute(departureLocation, destinationLocation)
            .apply { pointsSet.addAll(points) }
        addAirportMarker(departureCity)
            .apply { pointsSet.add(position) }
        addAirportMarker(destinationCity)
            .apply { pointsSet.add(position) }
        planeMarker = addPlaneMarker(departureLocation, bearing)
            .apply { pointsSet.add(position) }

        val boundsBuilder = LatLngBounds.builder()
        pointsSet.forEach { boundsBuilder.include(it) }

        return boundsBuilder.build()
    }

    private fun setInitialCameraPosition(markerBounds: LatLngBounds, idleListener: () -> Unit) {
        googleMap.moveCamera(
            newLatLngBounds(
                markerBounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                resources.getDimensionPixelSize(R.dimen.ticket_search_activity_map_extent_padding)
            )
        )

        googleMap.setOnCameraIdleListener {
            googleMap.setOnCameraIdleListener(null)

            val displayBounds = getDisplayRect().toProjection(googleMap.projection)
            if (!displayBounds.contains(markerBounds)) {
                isTrackingMarkerEnabled = true
                googleMap.moveCamera(
                    newLatLngZoom(
                        departureCity.location.toLatLng(),
                        googleMap.minZoomLevel
                    )
                )
            }

            idleListener()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        planeMarkerAnimatorPlayTime =
            savedInstanceState.getLong(STATE_PLANE_MARKER_ANIMATOR_PLAY_TIME)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(
            STATE_PLANE_MARKER_ANIMATOR_PLAY_TIME,
            planeMarkerAnimator?.currentPlayTime ?: planeMarkerAnimatorPlayTime
        )
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action and ACTION_MASK) {
            ACTION_DOWN -> isOnTouchStarted = true
            ACTION_UP -> isOnTouchStarted = false
        }
        return super.dispatchTouchEvent(event)
    }

    private fun addAirportMarker(city: CityModel): Marker {
        val markerOptions = MarkerOptions()
            .position(city.location.toLatLng())
            .icon(fromBitmap(airportIconGenerator.makeIcon(city.getAirportName())))
            .anchor(0.5f, 0.5f)
            .zIndex(Z_INDEX_MARKER)

        return googleMap.addMarker(markerOptions)
    }

    private fun addPlaneRoute(departureLocation: LatLng, destinationLocation: LatLng): Polyline {
        val polylineOptions = PolylineOptions()
            .add(departureLocation, destinationLocation)
            .geodesic(true)
            .color(getColor(this, R.color.plane_route))
            .pattern(listOf(Gap(dotLineGap), Dot()))
            .width(dotLineWidth)
            .zIndex(Z_INDEX_GRAPHIC)

        return googleMap.addPolyline(polylineOptions)
    }

    private fun addPlaneMarker(location: LatLng, bearing: Float): Marker {
        val markerOptions = MarkerOptions()
            .position(location)
            .rotation(bearing)
            .icon(fromResource(R.drawable.ic_plane))
            .anchor(0.5f, 0.5f)
            .zIndex(Z_INDEX_ANIMATED_MARKER)

        return googleMap.addMarker(markerOptions)
    }

    private fun invalidateLoadingState(state: StateModel): Unit = when (state) {
        is StateModel.Loading -> startPlaneMarkerAnimation()
        is StateModel.Success -> endPlaneMarkerAnimation()
    }

    private fun startPlaneMarkerAnimation() {
        val duration = resources.getInteger(ticket_search_activity_average_animation_duration)
        animatePlaneMarker(
            SECONDS.toMillis(duration.toLong()),
            LinearOutSlowInInterpolator(),
            planeMarkerAnimatorPlayTime
        )
        planeMarkerAnimatorPlayTime = 0L
    }

    private fun endPlaneMarkerAnimation() {
        val duration = resources.getInteger(ticket_search_activity_final_animation_duration)
        animatePlaneMarker(
            SECONDS.toMillis(duration.toLong()),
            FastOutLinearInInterpolator(),
            endListener = ::finish
        )
    }

    private fun animatePlaneMarker(
        duration: Long,
        interpolator: Interpolator,
        playTime: Long = 0L,
        endListener: (() -> Unit)? = null
    ) {
        planeMarkerAnimator?.cancel()

        val destinationLocation = destinationCity.location.toLatLng()
        planeMarkerAnimator = planeMarker.animate(destinationLocation).apply {
            setDuration(duration)
            setInterpolator(interpolator)
            currentPlayTime = playTime
            if (isTrackingMarkerEnabled) {
                addUpdateListener { updateCameraPosition() }
            }
            endListener?.run { addListener(onEnd = { invoke() }) }
            start()
        }
    }

    private fun updateCameraPosition() {
        if (!isOnTouchStarted) {
            googleMap.moveCamera(newLatLng(planeMarker.position))
        }
    }

    override fun onDestroy() {
        planeMarkerAnimator?.cancel()

        super.onDestroy()
    }

    companion object {

        private const val Z_INDEX_GRAPHIC = 0f
        private const val Z_INDEX_MARKER = 1f
        private const val Z_INDEX_ANIMATED_MARKER = 2f

        private const val KEY_DEPARTURE_CITY = "departure_city"
        private const val KEY_DESTINATION_CITY = "destination_city"

        private const val STATE_PLANE_MARKER_ANIMATOR_PLAY_TIME = "plane_marker_animator_play_time"

        fun newIntent(
            context: Context,
            departureCity: CityModel,
            destinationCity: CityModel
        ) = Intent(context, TicketSearchActivity::class.java).apply {
            putExtra(KEY_DEPARTURE_CITY, departureCity)
            putExtra(KEY_DESTINATION_CITY, destinationCity)
        }
    }
}
