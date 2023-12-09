package com.gram.client.presentation.screens.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.location.LocationManager
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.components.voyager.MapPointScreen
import com.gram.client.presentation.screens.main.MainScreen
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.order.OrderExecutionScreen
import com.gram.client.presentation.screens.order.OrderExecutionViewModel
import com.gram.client.presentation.screens.order.SearchDriverScreen
import com.gram.client.utils.PreferencesName
import com.gram.client.utils.Values
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun ActiveMap() {
    val orderExecutionViewModel: OrderExecutionViewModel = hiltViewModel()
    val WHICH_ADDRESS= Values.WhichAddress
    val context = LocalContext.current
    val prefs: SharedPreferences =
        context.getSharedPreferences(PreferencesName.APP_PREFERENCES, Context.MODE_PRIVATE)

    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val stateStatusGPS = remember {
        mutableStateOf(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }
    val navigator = LocalNavigator.currentOrThrow
    currentRoute = navigator.lastItem.key
    val mapControllers = MapController(context)

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            stateStatusGPS.value = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Log.d("mapLive", "ON_CREATE")
                }
                Lifecycle.Event.ON_START -> {
                    Log.d("mapLive", "ON_START")
                }
                Lifecycle.Event.ON_RESUME -> {
                    Log.d("mapLive", "ON_RESUME")
                    stateStatusGPS.value = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                }
                Lifecycle.Event.ON_PAUSE -> {
                    Log.d("mapLive", "ON_PAUSE")
                }
                Lifecycle.Event.ON_STOP -> {
                    Log.d("mapLive", "ON_STOP")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    Log.d("mapLive", "ON_DESTROY")
                }
                else -> return@LifecycleEventObserver
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            prefs.edit().putLong("mapPosX", map.mapCenter.latitude.toBits())
                .putLong("mapPosY", map.mapCenter.longitude.toBits())
                .putLong("mapPosZ", map.zoomLevelDouble.toBits()).apply()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        factory = {
            View.inflate(it, R.layout.map, null).apply {
                map = this.findViewById(R.id.map)
                markers = Markers(context, map)
                getAddressMarker = this.findViewById(R.id.getAddressMarker)
                Configuration.getInstance()
                    .load(it, PreferenceManager.getDefaultSharedPreferences(it))
                map.setTileSource(TileSourceFactory.MAPNIK)
                val mapController = map.controller
                mapController.setZoom(Double.fromBits(prefs.getLong("mapPosZ", 18.0.toBits())))
                val startPoint = GeoPoint(
                    Double.fromBits(prefs.getLong("mapPosX", 40.27803692395751.toBits())),
                    Double.fromBits(prefs.getLong("mapPosY", 69.62923931506361.toBits()))
                )
                map.controller.setCenter(startPoint)
                mRotationGestureOverlay = RotationGestureOverlay(it, map)
                mRotationGestureOverlay.isEnabled = false

                map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                map.setMultiTouchControls(true)

                btnZoomIn = this.findViewById(R.id.btnZoomIn)
                btnZoomOut = this.findViewById(R.id.btnZoomOut)
                btnZoomIn.setOnClickListener {
                    btnZoomOut.visibility = View.VISIBLE
                    mapControllers.changeZoom(+1.0)
                    if (map.zoomLevelDouble == 21.0) {
                        btnZoomIn.visibility = View.INVISIBLE
                    }
                }
                btnZoomOut.setOnClickListener {
                    btnZoomIn.visibility = View.VISIBLE
                    mapControllers.changeZoom(-1.0)
                    if (map.zoomLevelDouble == 15.0) {
                        btnZoomOut.visibility = View.INVISIBLE
                    }
                }
                map.minZoomLevel = 16.0
                map.maxZoomLevel = 20.0

                val myLocationProvider = GpsMyLocationProvider(it)
                mLocationOverlay = MyLocationNewOverlay(myLocationProvider, map)
                mapControllers.myLocationShow(mLocationOverlay)
                when (currentRoute) {
                    MainScreen().key -> {
                        getAddressMarker.visibility = View.GONE
                        map.overlays.clear()
                        addOverlays()
                    }
                    SearchDriverScreen().key -> {
                        getAddressMarker.visibility = View.GONE
                        map.overlays.clear()
                        addOverlays()
                    }
                    SearchAddressScreen().key -> {
                        map.overlays.clear()
                        addOverlays()
                    }
                    MapPointScreen().key -> {
                        map.overlays.clear()
                        addOverlays()
                    }
                    OrderExecutionScreen().key -> {
                        getAddressMarker.visibility = View.GONE
                        map.overlays.clear()
                        addOverlays()
                    }

                }
            }
        },
        update = {
            when (currentRoute) {
                MainScreen().key -> {

                }
                OrderExecutionScreen().key -> {

                    if (Values.DriverLocation.value != GeoPoint(0.0, 0.0)) {
                        //markers.addDriverMarker(Values.DriverLocation.value, "")
                    }
                }
            }
        }
    )
}
private fun setChangeLocationListener() {
    mLocationOverlay.myLocationProvider.startLocationProvider { location: Location, source: IMyLocationProvider? ->
        printoutDebugInfo(location)
        mLocationOverlay.onLocationChanged(
            location, source
        )
    }
    map.overlays.add(mLocationOverlay)
}
@SuppressLint("WrongConstant")
private fun printoutDebugInfo(
    l1: Location?,
) {
    val location: Location? = l1 ?: mLocationOverlay.lastFix
    //Log.i("myLocation", "" + location)
    if (location != null) {
        myLocationPoint.value.latitude = location.latitude
        myLocationPoint.value.longitude = location.longitude
    }
    if ((location != null)) {
        var pOrientation = 360 - location.bearing
        if (pOrientation < 0) pOrientation += 360f
        if (pOrientation > 360) pOrientation -= 360f
        pOrientation = (pOrientation.toInt().toFloat()) / 5
        pOrientation = (pOrientation.toInt().toFloat()) * 5
        //markers.addDriverMarker(geoPoint = GeoPoint(location.latitude, location.longitude),"", pOrientation)

        if (location.speed >= 0.01) {
            //map.controller.animateTo(mLocationOverlay.myLocation, map.zoomLevelDouble, 1000, pOrientation)
        }
    }
}
