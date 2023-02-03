@file:Suppress("DEPRECATION")

package com.example.gramclient.presentation.components

import TwoFingerDrag
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.location.LocationManager
import android.net.Uri
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
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
import com.example.gramclient.R
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.MainActivity
import com.example.gramclient.presentation.screens.main.MainScreen
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.main.SearchAddressScreen
import com.example.gramclient.presentation.screens.map.UserTouchSurface
import com.example.gramclient.presentation.screens.order.OrderExecutionScreen
import com.example.gramclient.presentation.screens.order.SearchDriverScreen
import com.example.gramclient.ui.theme.BackgroundColor
import com.example.gramclient.ui.theme.PrimaryColor
import com.example.gramclient.utils.Constants
import com.example.gramclient.utils.PreferencesName
import com.example.gramclient.utils.RoutesName
import com.example.gramclient.utils.Values
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

lateinit var fromAddres2: Address
lateinit var toAddress2: List<Address>
private lateinit var map: MapView

private lateinit var userTouchSurface: UserTouchSurface
lateinit var mRotationGestureOverlay: RotationGestureOverlay
lateinit var mLocationOverlay: MyLocationNewOverlay

@SuppressLint("StaticFieldLeak")
lateinit var btnLocation: ImageButton

@SuppressLint("StaticFieldLeak")
lateinit var getAddressMarker: ImageView
var currentRoute: String? = null

@Composable
fun CustomMainMap(
    mainViewModel: MainViewModel,
    WHICH_ADDRESS: MutableState<String>? = null
) {
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences(PreferencesName.APP_PREFERENCES, Context.MODE_PRIVATE)
    val navigator = LocalNavigator.currentOrThrow
    currentRoute = navigator.lastItem.key

    val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    val stateStatusGPS = remember {
        mutableStateOf(manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    }
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
    if (stateStatusGPS.value) {
        val toAddress by Values.ToAddress
        val fromAddress by Values.FromAddress
        toAddress2 = toAddress
        fromAddres2 = fromAddress
        val scope = rememberCoroutineScope()
        val startPointForMarker = remember {
            mutableStateOf(
                GeoPoint(
                    if (toAddress[0].address_lat != "") toAddress[0].address_lat.toDouble() else 40.27803692395751,
                    if (toAddress[0].address_lng != "") toAddress[0].address_lng.toDouble() else 69.62923931506361
                )
            )
        }

        AndroidView(
            factory = {
                View.inflate(it, R.layout.map, null).apply {
                    map = this.findViewById(R.id.map)
                    userTouchSurface = this.findViewById(R.id.userTouchSurface)
                    btnLocation = this.findViewById(R.id.btnLocation)
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
                    when (currentRoute) {
                        MainScreen().key -> {
                            btnLocation.margin(0f, 0f, 0f, 355f)
                            btnLocation.visibility = View.GONE
                            getAddressMarker.visibility = View.GONE
                            map.overlays.clear()
                            addOverlays()
                            showRoadAB(it, fromAddress, toAddress)
                        }
                        SearchDriverScreen().key, OrderExecutionScreen().key -> {
                            btnLocation.visibility = View.GONE
                            getAddressMarker.visibility = View.GONE
                            map.overlays.clear()
                            addOverlays()
                            showRoadAB(it, fromAddress, toAddress)
                        }
                        SearchAddressScreen().key -> {
                            map.overlays.clear()
                            addOverlays()
                        }
                    }
                    btnLocation.setOnClickListener {
                        map.controller.animateTo(mLocationOverlay.myLocation)
                        if(mLocationOverlay.myLocation!=null){
                            scope.launch {
                                if (currentRoute == RoutesName.SEARCH_ADDRESS_SCREEN) {
                                    if(WHICH_ADDRESS != null)
                                        mainViewModel.getAddressFromMap(
                                            mLocationOverlay.myLocation.longitude,
                                            mLocationOverlay.myLocation.latitude,
                                            WHICH_ADDRESS
                                        )
                                }
                            }
                        }
                    }
                }
            },
            update = {
                when (currentRoute) {
                    MainScreen().key -> {
                        if(fromAddress!= fromAddres2 || toAddress!= toAddress2) showRoadAB(it.context, fromAddress, toAddress)
                    }
                    SearchDriverScreen().key, OrderExecutionScreen().key -> {
                        //showRoadAB(it.context, fromAddress, toAddress)
                    }
                    SearchAddressScreen().key -> {
                        map.overlays.clear()
                        userTouchSurface.setCallback(
                            TwoFingerDrag(
                                context,
                                object : TwoFingerDrag.Listener {
                                    override fun onOneFinger(event: MotionEvent?) {
                                        map.dispatchTouchEvent(event)
                                        if (event != null) {
                                            when (event.action) {
                                                MotionEvent.ACTION_MOVE -> {
                                                    Log.e(
                                                        "singleTapConfirmedHelper",
                                                        "${map.mapCenter.latitude}-${map.mapCenter.longitude}"
                                                    )
                                                }
                                                MotionEvent.ACTION_DOWN -> {
                                                    Log.e("singleTapConfirmedHelper", "Action was DOWN")

                                                }
                                                MotionEvent.ACTION_UP -> {
                                                    Log.e("singleTapConfirmedHelper", "Action was UP")
                                                    if (WHICH_ADDRESS != null) {
                                                        mainViewModel.getAddressFromMap(
                                                            map.mapCenter.longitude,
                                                            map.mapCenter.latitude,
                                                            WHICH_ADDRESS
                                                        )
                                                    }
                                                    Log.e("singleTapConfirmedHelper", "$toAddress")
                                                    map.postInvalidate()
                                                    startPointForMarker.value = GeoPoint(
                                                        map.mapCenter.latitude,
                                                        map.mapCenter.longitude
                                                    )
                                                }
                                                MotionEvent.ACTION_CANCEL -> {
                                                    Log.e(
                                                        "singleTapConfirmedHelper",
                                                        "Action was CANCEL"
                                                    )

                                                }
                                                MotionEvent.ACTION_OUTSIDE -> {
                                                    Log.e(
                                                        "singleTapConfirmedHelper",
                                                        "Movement occurred outside bounds of current screen element"
                                                    )

                                                }
                                                else -> {
                                                    Log.e("singleTapConfirmedHelper", "ACTION_CANCEL")
                                                }
                                            }
                                        }
                                    }

                                    override fun onTwoFingers(event: MotionEvent?) {
                                        map.dispatchTouchEvent(event)
                                        if (event != null) {
                                            when (event.action) {
                                                MotionEvent.ACTION_MOVE -> {
                                                    //                                    Log.e("singleTapConfirmedHelper", "${map.mapCenter.latitude}-${map.mapCenter.longitude}")
                                                }
                                                MotionEvent.ACTION_POINTER_2_DOWN -> {
                                                    Log.e(
                                                        "singleTapConfirmedHelper",
                                                        "Action was ACTION_POINTER_2_DOWN"
                                                    )
                                                }
                                                MotionEvent.TOOL_TYPE_FINGER -> {
                                                    Log.e(
                                                        "singleTapConfirmedHelper",
                                                        "Action was TOOL_TYPE_FINGER"
                                                    )
                                                }
                                                MotionEvent.ACTION_POINTER_2_UP -> {
                                                    Log.e("singleTapConfirmedHelper", "Action was UP")
                                                    if (WHICH_ADDRESS != null) {
                                                        mainViewModel.getAddressFromMap(
                                                            map.mapCenter.longitude,
                                                            map.mapCenter.latitude,
                                                            WHICH_ADDRESS
                                                        )
                                                    }
                                                    Log.e("singleTapConfirmedHelper", "$toAddress")
                                                    map.postInvalidate()
                                                    startPointForMarker.value = GeoPoint(
                                                        map.mapCenter.latitude,
                                                        map.mapCenter.longitude
                                                    )
                                                }
                                                else -> {
                                                    Log.e("singleTapConfirmedHelper", "ACTION_CANCEL")
                                                }
                                            }
                                        }
                                    }
                                })
                        )
                        map.overlays.add(
                            MapEventsOverlay(object : MapEventsReceiver {
                                override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                                    map.postInvalidate()
                                    startPointForMarker.value = GeoPoint(p.latitude, p.longitude)
                                    if (WHICH_ADDRESS != null) {
                                        mainViewModel.getAddressFromMap(
                                            map.mapCenter.longitude,
                                            map.mapCenter.latitude,
                                            WHICH_ADDRESS
                                        )
                                    }
                                    return true
                                }

                                override fun longPressHelper(p: GeoPoint): Boolean {
                                    return false
                                }
                            })
                        )
                        addOverlays()
                    }
                }
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
                    .background(White)
                    .padding(20.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Для продолжения работы необходимо включить GPS-приемник",
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth(),
                    color = Black
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = BackgroundColor, contentColor = Black),
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryColor, contentColor = White),
                        onClick = { Intent(
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

                        } }
                    ) {
                        Text(text = "Настройки", fontSize = 18.sp, color = White)
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
    fromAddress: Address,
    toAddress: List<Address>,
) {
    val roadManager: RoadManager = OSRMRoadManager(context, "GramDriver/1.0")
    GlobalScope.launch {
        try {
            val waypoints = ArrayList<GeoPoint>()
            val fromAddressPoint = GeoPoint(0, 0)
            fromAddressPoint.latitude = fromAddress.address_lat.toDouble()
            fromAddressPoint.longitude = fromAddress.address_lng.toDouble()
            waypoints.add(fromAddressPoint)


            val toAddressesPoints = ArrayList<GeoPoint>()
            val toAddressesNames = ArrayList<String>()
            toAddress.forEach { address ->
                val toAddressPoint = GeoPoint(0, 0)
                toAddressPoint.latitude = address.address_lat.toDouble()
                toAddressPoint.longitude = address.address_lng.toDouble()
                toAddressesNames.add(address.address)
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
            val blueColorValue: Int = Color.parseColor("#36457C")
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
                    title = fromAddress.address,
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

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
fun Context.dpToPx(dp: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()
