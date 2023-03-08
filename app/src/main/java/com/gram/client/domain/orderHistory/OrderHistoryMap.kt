package com.gram.client.domain.orderHistory

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gram.client.R
import com.gram.client.presentation.MainActivity
import com.gram.client.presentation.screens.main.MainScreen
import com.gram.client.presentation.screens.main.SearchAddressScreen
import com.gram.client.presentation.screens.map.Markers
import com.gram.client.presentation.screens.map.UserTouchSurface
import com.gram.client.presentation.screens.order.OrderExecutionScreen
import com.gram.client.presentation.screens.order.SearchDriverScreen
import com.gram.client.ui.theme.BackgroundColor
import com.gram.client.ui.theme.PrimaryColor
import com.gram.client.utils.PreferencesName
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


lateinit var map: MapView

@SuppressLint("StaticFieldLeak")
lateinit var markers: Markers

private lateinit var userTouchSurface: UserTouchSurface
lateinit var mRotationGestureOverlay: RotationGestureOverlay
lateinit var mLocationOverlay: MyLocationNewOverlay

@SuppressLint("StaticFieldLeak")
lateinit var getAddressMarker: ImageView
var currentRoute: String? = null
val myLocationPoint = mutableStateOf(GeoPoint(0.0, 0.0))


@Composable
fun CustomMainMapHistory(
    WHICH_ADDRESS: MutableState<String>? = null,
    selectedOrder: Data
) {

    val isGet = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val prefs: SharedPreferences =
        context.getSharedPreferences(PreferencesName.APP_PREFERENCES, Context.MODE_PRIVATE)
    val navigator = LocalNavigator.currentOrThrow
    currentRoute = navigator.lastItem.key

    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val stateStatusGPS = remember {
        mutableStateOf(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        if (currentRoute == OrderExecutionScreen().key) {
            Log.i("showRoad", "run")
            //mainViewModel.updateToAddress(Address())
//            mainViewModel.updateFromAddress(Address())
            isGet.value = true
        }else if (currentRoute == MainScreen().key){
//            mainViewModel.showRoad()
        }
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

    if (stateStatusGPS.value) {
        val scope = rememberCoroutineScope()

        AndroidView(
            factory = {
                View.inflate(it, R.layout.map, null).apply {
                    map = this.findViewById(R.id.map)
                    markers = Markers(context, map)
                    userTouchSurface = this.findViewById(R.id.userTouchSurface)
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

                    map.minZoomLevel = 10.0
                    map.maxZoomLevel = 24.0

                    val myLocationProvider = GpsMyLocationProvider(it)
                    mLocationOverlay = MyLocationNewOverlay(myLocationProvider, map)
                    myLocationShow(it, mLocationOverlay)
                    selectedOrder.let {
                        it.from_address?.let {
                            showRoadAB(context, it, selectedOrder.to_addresses)
                        }
                    }
                    getAddressMarker.visibility = View.GONE
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
                        OrderExecutionScreen().key -> {
                            getAddressMarker.visibility = View.GONE
                            map.overlays.clear()
                            addOverlays()
                        }

                    }
                    setChangeLocationListener()
                }
            },
            update = {

            }
        )
    } else {
        val context = LocalContext.current
        val activity = (context as MainActivity)
        Dialog(onDismissRequest = { /*TODO*/ }) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Для продолжения работы необходимо включить GPS-приемник",
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = BackgroundColor,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            activity.finish()
                        }
                    ) {
                        Text(text = "Выход", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = PrimaryColor,
                            contentColor = Color.White
                        ),
                        onClick = {
                            Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS,
                                Uri.fromParts(
                                    "package",
                                    context.packageName,
                                    null
                                )
                            ).also { intent ->
                                try {
                                    context.startActivity(
                                        intent
                                    )
                                } catch (e: ActivityNotFoundException) {
                                    Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                    ).also { intentCatch ->
                                        context.startActivity(
                                            intentCatch
                                        )
                                    }
                                }

                            }
                        }
                    ) {
                        Text(text = "Настройки", fontSize = 18.sp, color = Color.White)
                    }
                }
            }
        }
    }
}

fun addOverlays() {
    map.overlays.add(mLocationOverlay)
    map.overlays.add(mRotationGestureOverlay)
}

fun showRoadAB(
    context: Context,
    fromAddress: FromAddress,
    toAddress: List<ToAddresse>?,
) {
    if(currentRoute== SearchAddressScreen().key) return
    val roadManager: RoadManager = OSRMRoadManager(context, "GramDriver/1.0")
    GlobalScope.launch {
        try {
            val waypoints = ArrayList<GeoPoint>()
            val fromAddressPoint = GeoPoint(0, 0)
            fromAddressPoint.latitude = fromAddress.lat.toDouble()
            fromAddressPoint.longitude = fromAddress.lng.toDouble()
            waypoints.add(fromAddressPoint)


            val toAddressesPoints = ArrayList<GeoPoint>()
            val toAddressesNames = ArrayList<String>()
            toAddress?.forEach { address ->
                val toAddressPoint = GeoPoint(0, 0)
                toAddressPoint.latitude = address.lat.toDouble()
                toAddressPoint.longitude = address.lng.toDouble()
                toAddressesNames.add(address.name)
                toAddressesPoints.add(toAddressPoint)
            }
            if (fromAddressPoint.latitude != 0.0 && toAddressesPoints[0].latitude != 0.0 && fromAddressPoint != toAddressesPoints[0])
                map.controller.setCenter(fromAddressPoint)
            toAddressesPoints.forEach {
                waypoints.add(it)
            }
            map.overlays.clear()
            val road = roadManager.getRoad(waypoints)
            val roadOverlay = RoadManager.buildRoadOverlay(road)
            val blueColorValue: Int = android.graphics.Color.parseColor("#009CC3")
            roadOverlay.color = blueColorValue
            roadOverlay.width = 15f
            roadOverlay.paint.strokeJoin = Paint.Join.ROUND
            roadOverlay.paint.strokeCap = Paint.Cap.ROUND

            map.overlays.add(roadOverlay)
            if (fromAddressPoint != toAddressesPoints[0]) {
                addMarker(
                    context,
                    map,
                    geoPoint = fromAddressPoint,
                    title = fromAddress.name,
                    R.drawable.ic_from_address_marker
                )
                toAddressesPoints.forEachIndexed { inx, it ->
                    addMarker(
                        context,
                        map,
                        geoPoint = it,
                        title = toAddressesNames[inx],
                        R.drawable.ic_to_address_marker
                    )
                }
            }
            addOverlays()
        } catch (_: Exception) {

        }
    }
}

private fun myLocationShow(context: Context?, mLocationOverlay: MyLocationNewOverlay) {
    val person: Bitmap = context?.let { getBitmap(it, R.drawable.ic_person) }!!
    val arrow: Bitmap = getBitmap(context, R.drawable.ic_navigation)!!
    mLocationOverlay.setPersonHotspot(person.width / 2f, person.height / 2f)
    mLocationOverlay.setPersonIcon(person)
    mLocationOverlay.setDirectionArrow(person, arrow)
    mLocationOverlay.enableMyLocation()

}

private fun getBitmap(context: Context, resID: Int): Bitmap? {
    val drawable = ResourcesCompat.getDrawable(context.resources, resID, null)
    val bitmap: Bitmap
    return try {
        bitmap = drawable?.let {
            Bitmap.createBitmap(
                it.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }!!
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    } catch (e: OutOfMemoryError) {
        null
    }
}

fun addMarker(context: Context, map: MapView, geoPoint: GeoPoint, title: String, icon: Int) {
    val firstMarker = Marker(map)
    firstMarker.position = geoPoint
    firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
    firstMarker.icon = ContextCompat.getDrawable(context, icon)
    firstMarker.title = title
    map.overlays.add(firstMarker)
}

fun View.margin(
    left: Float? = null,
    top: Float? = null,
    right: Float? = null,
    bottom: Float? = null
) {
    layoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = dpToPx(this) }
        top?.run { topMargin = dpToPx(this) }
        right?.run { rightMargin = dpToPx(this) }
        bottom?.run { bottomMargin = dpToPx(this) }
    }
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

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
