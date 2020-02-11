package one.xcorp.aviasales.screen.ticket.search

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import one.xcorp.aviasales.extension.map.LatLngInterpolator
import one.xcorp.aviasales.extension.map.animate
import one.xcorp.aviasales.extension.map.bearingTo
import one.xcorp.aviasales.extension.map.contains
import one.xcorp.aviasales.screen.ticket.route.mapper.toLatLng
import one.xcorp.aviasales.screen.ticket.route.model.CityModel
import one.xcorp.aviasales.screen.ticket.search.graphic.marker.AirportIconGenerator
import one.xcorp.aviasales.screen.ticket.search.graphic.route.RouteGenerator
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

    @Inject
    lateinit var routeGenerator: RouteGenerator

    @Inject
    lateinit var latLngInterpolator: LatLngInterpolator

    private lateinit var viewModel: TicketSearchViewModel

    private lateinit var googleMap: GoogleMap
    private lateinit var planeRoute: Polyline
    private lateinit var planeMarker: Marker

    private var planeMarkerAnimator: ValueAnimator? = null
    private var planeMarkerAnimatorDuration: Long? = null
    private var planeMarkerAnimatorPlayTime: Long? = null
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

        planeRoute = addPlaneRoute(departureLocation, destinationLocation)
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

    private fun setInitialCameraPosition(
        markerBounds: LatLngBounds,
        onCompletedSettingPosition: () -> Unit
    ) {
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

            val visibleRegion = googleMap.projection.visibleRegion.latLngBounds
            if (!visibleRegion.contains(markerBounds)) {
                isTrackingMarkerEnabled = true
                googleMap.moveCamera(
                    newLatLngZoom(
                        departureCity.location.toLatLng(),
                        googleMap.minZoomLevel
                    )
                )
            }

            onCompletedSettingPosition()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState.containsKey(STATE_PLANE_MARKER_ANIMATOR_DURATION)) {
            planeMarkerAnimatorDuration =
                savedInstanceState.getLong(STATE_PLANE_MARKER_ANIMATOR_DURATION)
        }
        if (savedInstanceState.containsKey(STATE_PLANE_MARKER_ANIMATOR_PLAY_TIME)) {
            planeMarkerAnimatorPlayTime =
                savedInstanceState.getLong(STATE_PLANE_MARKER_ANIMATOR_PLAY_TIME)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val duration = planeMarkerAnimator?.duration ?: planeMarkerAnimatorDuration
        if (duration != null) {
            outState.putLong(STATE_PLANE_MARKER_ANIMATOR_DURATION, duration)
        }
        val playTime = planeMarkerAnimator?.currentPlayTime ?: planeMarkerAnimatorPlayTime
        if (playTime != null) {
            outState.putLong(STATE_PLANE_MARKER_ANIMATOR_PLAY_TIME, playTime)
        }
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

    private fun addPlaneRoute(departure: LatLng, destination: LatLng): Polyline {
        val polylineOptions = routeGenerator.generate(googleMap, departure, destination)
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
        val duration = planeMarkerAnimatorDuration ?: SECONDS.toMillis(
            resources.getInteger(ticket_search_activity_average_animation_duration).toLong()
        )
        val playTime = planeMarkerAnimatorPlayTime ?: 0L
        animatePlaneMarker(duration, LinearOutSlowInInterpolator(), playTime)
    }

    private fun endPlaneMarkerAnimation() {

    }

    private fun animatePlaneMarker(
        duration: Long,
        interpolator: Interpolator,
        playTime: Long = 0L,
        endListener: (() -> Unit)? = null
    ) {
        planeMarkerAnimator?.cancel()

        planeMarkerAnimator = planeMarker.animate(planeRoute.points, latLngInterpolator).apply {
            setDuration(duration)
            setInterpolator(interpolator)
            if (isTrackingMarkerEnabled) {
                addUpdateListener { updateCameraPosition() }
            }
            endListener?.run { addListener(onEnd = { invoke() }) }
            start()
            currentPlayTime = playTime
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

        private const val STATE_PLANE_MARKER_ANIMATOR_DURATION = "plane_marker_animator_duration"
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
