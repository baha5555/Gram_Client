@file:Suppress("DEPRECATION")

package com.example.gramclient.presentation.components

import TwoFingerDrag
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.content.SharedPreferences
import android.graphics.*
import android.preference.PreferenceManager
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.utils.RoutesName
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.screens.main.MainViewModel
import com.example.gramclient.presentation.screens.map.UserTouchSurface
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

private lateinit var map: MapView

private lateinit var userTouchSurface: UserTouchSurface
private var requiredCenter: GeoPoint? = null
lateinit var mRotationGestureOverlay: RotationGestureOverlay
lateinit var mLocationOverlay: MyLocationNewOverlay

lateinit var btnLocation: ImageButton
lateinit var getAddressMarker: ImageView
 var currentRoute: String?=null

@Composable
fun CustomMainMap(
    mainViewModel: MainViewModel,
    navController: NavHostController,
    WHICH_ADDRESS: MutableState<String>?=null
) {

    val toAddress by mainViewModel.toAddress
    val fromAddress by mainViewModel.fromAddress

    val context= LocalContext.current
    val startPointForMarker=remember{ mutableStateOf(GeoPoint(if(toAddress[0].lat != "") toAddress[0].lat.toDouble() else 40.27803692395751, if(toAddress[0].lng != "") toAddress[0].lng.toDouble() else 69.62923931506361)) }
    val zoomLevel=remember{ mutableStateOf(18.0) }

    AndroidView(
        factory = {
            View.inflate(it, R.layout.map, null).apply {
                map = this.findViewById(R.id.map)
                userTouchSurface=this.findViewById(R.id.userTouchSurface)
                btnLocation = this.findViewById<ImageButton>(R.id.btnLocation)
                getAddressMarker = this.findViewById<ImageView>(R.id.getAddressMarker)
                currentRoute = navController.currentBackStackEntry?.destination?.route

                Configuration.getInstance().load(it, PreferenceManager.getDefaultSharedPreferences(it))
                map.setTileSource(TileSourceFactory.MAPNIK)
                val startPoint = startPointForMarker.value
                val mapController = map.controller
                mapController.setZoom(zoomLevel.value)
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
                btnLocation.setOnClickListener {
                    try {
                        if (map.overlays.contains(mLocationOverlay)) {
                            val myLocation: GeoPoint = mLocationOverlay.myLocation
                            if (myLocation.latitude != 0.0 && myLocation.longitude != 0.0) {
                                jump(myLocation)
                                return@setOnClickListener
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
                if(currentRoute== RoutesName.MAIN_SCREEN){
                    btnLocation.margin(0f, 0f, 0f, 355f)
                    btnLocation.visibility=View.GONE
                    getAddressMarker.visibility=View.GONE

                    map.overlays.clear()
                    addOverlays()
                    showRoadAB(it, fromAddress, toAddress)
                }
                else if(currentRoute== RoutesName.SEARCH_DRIVER_SCREEN){
                    btnLocation.visibility = View.GONE
                    getAddressMarker.visibility=View.GONE

                    map.overlays.clear()
                    addOverlays()
                    showRoadAB(it, fromAddress, toAddress)
                }
                else if(currentRoute== RoutesName.SEARCH_ADDRESS_SCREEN){
                    map.overlays.clear()
                    addOverlays()
                }
            }
        },
        update = {
            if(currentRoute== RoutesName.MAIN_SCREEN){
                showRoadAB(it.context, fromAddress, toAddress)
            }
            else if(currentRoute== RoutesName.SEARCH_DRIVER_SCREEN) {
                showRoadAB(it.context, fromAddress, toAddress)
            }
            else if(currentRoute== RoutesName.SEARCH_ADDRESS_SCREEN){
                map.overlays.clear()

//                val matrixA = ColorMatrix()
//                matrixA.setSaturation(0.9f)
//                val matrixB = ColorMatrix()
//                matrixB.setScale(1.12f, 1.13f, 1.13f, 1.0f)
//                matrixA.setConcat(matrixB, matrixA)
//                val filter = ColorMatrixColorFilter(matrixA)
//                map.overlayManager.tilesOverlay.setColorFilter(filter)

                userTouchSurface.setCallback(TwoFingerDrag(context, object : TwoFingerDrag.Listener {
                    override fun onOneFinger(event: MotionEvent?) {
                        map.dispatchTouchEvent(event)
                        if (event != null) {
                            when (event.action) {
                                MotionEvent.ACTION_MOVE-> {
                                    Log.e("singleTapConfirmedHelper", "${map.mapCenter.latitude}-${map.mapCenter.longitude}")
                                }
                                MotionEvent.ACTION_DOWN -> {
                                    Log.e("singleTapConfirmedHelper", "Action was DOWN")

                                }
                                MotionEvent.ACTION_UP -> {
                                    Log.e("singleTapConfirmedHelper", "Action was UP")
                                    if(WHICH_ADDRESS != null) {
                                        mainViewModel.getAddressFromMap(
                                            map.mapCenter.longitude, map.mapCenter.latitude, WHICH_ADDRESS
                                        )
                                    }
                                    Log.e("singleTapConfirmedHelper", "${toAddress}")
                                    map.postInvalidate()
                                    startPointForMarker.value=GeoPoint(map.mapCenter.latitude, map.mapCenter.longitude)
                                    zoomLevel.value=map.zoomLevelDouble
                                }
                                MotionEvent.ACTION_CANCEL -> {
                                    Log.e("singleTapConfirmedHelper", "Action was CANCEL")

                                }
                                MotionEvent.ACTION_OUTSIDE -> {
                                    Log.e("singleTapConfirmedHelper", "Movement occurred outside bounds of current screen element")

                                }
                                else-> {
                                    Log.e("singleTapConfirmedHelper", "ACTION_CANCEL")
                                }
                            }
                        }
                    }
                    override fun onTwoFingers(event: MotionEvent?) {
                        map.dispatchTouchEvent(event)
                        if (event != null) {
                            when (event.action) {
                                MotionEvent.ACTION_MOVE-> {
//                                    Log.e("singleTapConfirmedHelper", "${map.mapCenter.latitude}-${map.mapCenter.longitude}")
                                }
                                MotionEvent.ACTION_POINTER_2_DOWN -> {
                                    Log.e("singleTapConfirmedHelper", "Action was ACTION_POINTER_2_DOWN")
                                }
                                MotionEvent.TOOL_TYPE_FINGER -> {
                                    Log.e("singleTapConfirmedHelper", "Action was TOOL_TYPE_FINGER")
                                }
                                MotionEvent.ACTION_POINTER_2_UP -> {
                                    Log.e("singleTapConfirmedHelper", "Action was UP")
                                    if(WHICH_ADDRESS != null) {
                                        mainViewModel.getAddressFromMap(
                                            map.mapCenter.longitude, map.mapCenter.latitude, WHICH_ADDRESS
                                        )
                                    }
                                    Log.e("singleTapConfirmedHelper", "${toAddress}")
                                    map.postInvalidate()
                                    startPointForMarker.value=GeoPoint(map.mapCenter.latitude, map.mapCenter.longitude)
                                    zoomLevel.value=map.zoomLevelDouble
                                }
                                else-> {
                                    Log.e("singleTapConfirmedHelper", "ACTION_CANCEL")
                                }
                            }
                        }
                    }
                }))

                map.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
                    override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
//                        Toast.makeText(context, "${p.longitude}-${p.latitude}", Toast.LENGTH_SHORT).show()
                        map.postInvalidate()
                        startPointForMarker.value=GeoPoint(p.latitude, p.longitude)
                        if(WHICH_ADDRESS != null) {
                            mainViewModel.getAddressFromMap(
                                map.mapCenter.longitude, map.mapCenter.latitude, WHICH_ADDRESS
                            )
                        }
                        zoomLevel.value=map.zoomLevelDouble
                        return true
                    }
                    override fun longPressHelper(p: GeoPoint): Boolean {
                        return false
                    }
                    })
                )
            }
        }
    )
}

fun addOverlays() {
    map.overlays.add(mLocationOverlay)
    map.overlays.add(mRotationGestureOverlay)
}

@OptIn(DelicateCoroutinesApi::class)
fun showRoadAB(
    context: Context,
    fromAddress: Address,
    toAddress: List<Address>,
) {
    val roadManager: RoadManager = OSRMRoadManager(context, "GramDriver/1.0")
    GlobalScope.launch {
        try {
            val waypoints = ArrayList<GeoPoint>()
//            val startPoint = GeoPoint(40.27803692395751, 69.62923931506361)
//            val startPoint2 = GeoPoint(41.27803692395751, 70.62923931506361)
            val fromAddressPoint: GeoPoint = GeoPoint(0, 0)
            fromAddressPoint.latitude =
                fromAddress.lat.toDouble() ?: 0.0
            fromAddressPoint.longitude =
                fromAddress.lng.toDouble() ?: 0.0
            waypoints.add(fromAddressPoint)


            val toAddressesPoints = ArrayList<GeoPoint>()
            val toAddressesNames = ArrayList<String>()
            toAddress.forEach { address ->
                val toAddressPoint: GeoPoint = GeoPoint(0, 0)
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
            val blueColorValue: Int = Color.parseColor("#36457C")
            roadOverlay.color = blueColorValue
            roadOverlay.width = 15f
            roadOverlay.paint.strokeJoin = Paint.Join.ROUND
            roadOverlay.paint.strokeCap = Paint.Cap.ROUND

            map.overlays.add(roadOverlay)
            if (fromAddressPoint != toAddressesPoints[0]) {
                fromAddress.let {
                    addMarker(
                        context,
                        map,
                        geoPoint = fromAddressPoint,
                        title = it.name,
                        R.drawable.ic_from_address_marker
                    )
                }
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
        // Handle the error
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

private fun jump(targetPoint: GeoPoint) {
    jump(targetPoint, 0.0)
}

private fun jump(targetPoint: GeoPoint, requestedZoom: Double) {
    setRequiredCenter(targetPoint)
    if (map.width == 0 || map.height == 0) { // no animation possible
        map.controller.setCenter(targetPoint)
        return
    }
    val startPoint = GeoPoint(map.mapCenter)
    val startZoom: Double = map.zoomLevelDouble
    val targetZoom = if (requestedZoom == 0.0) startZoom else requestedZoom
    val points: MutableList<GeoPoint> = ArrayList()
    points.add(startPoint)
    points.add(targetPoint)
    val boundingBox1 = BoundingBox.fromGeoPoints(points)
    val intermediateZoom =
        MapView.getTileSystem().getBoundingBoxZoom(boundingBox1, map.width, map.height)
    //        final GeoPoint intermediatePoint = boundingBox1.getCenterWithDateLine();
//        if (BuildConfig.DEBUG)
//            Toast.makeText(this, "Zooms " + startZoom + " " + intermediateZoom, Toast.LENGTH_SHORT).show();
    if (intermediateZoom < startZoom) {
        //Log.d("MyApp2", "zoom out animation start [" + startZoom + "] intermediate [" + intermediateZoom + "]");
        val zoomOut = ValueAnimator.ofFloat(0f, 1f)
        val speed: Int =
            if (startZoom - intermediateZoom < 5) 250 else 500
        zoomOut.duration = speed.toLong()
        zoomOut.addUpdateListener { updatedAnimation: ValueAnimator ->
            val fraction = updatedAnimation.animatedFraction
            try { // for an unknown reason here happens a crash in BoundingBox.set
                map.controller.setZoom(startZoom + (intermediateZoom - startZoom) * fraction)
            } catch (ignored: Exception) {
            }
        }
        zoomOut.interpolator = AccelerateInterpolator()
        val move = ValueAnimator.ofFloat(0f, 1f)
        move.duration = 500.toLong()
        move.addUpdateListener { updatedAnimation: ValueAnimator ->
            val fraction = updatedAnimation.animatedFraction
            map.controller.setCenter(
                GeoPoint(
                    startPoint.latitude + (targetPoint.latitude - startPoint.latitude) * fraction,
                    startPoint.longitude + (targetPoint.longitude - startPoint.longitude) * fraction
                )
            )
        }
        move.interpolator = AccelerateDecelerateInterpolator()
        val zoomIn = ValueAnimator.ofFloat(0f, 1f)
        zoomIn.duration = speed.toLong()
        zoomIn.addUpdateListener { updatedAnimation: ValueAnimator ->
            val fraction = updatedAnimation.animatedFraction
            map.controller
                .setZoom(intermediateZoom + (targetZoom - intermediateZoom) * fraction)
            map.controller.setCenter(targetPoint) // important
        }
        zoomOut.interpolator = DecelerateInterpolator()
        val animation = AnimatorSet()
        animation.playSequentially(zoomOut, move, zoomIn)
        animation.start()
    } else {
        val move = ValueAnimator.ofFloat(0f, 1f)
        move.duration = 500.toLong()
        move.addUpdateListener { updatedAnimation: ValueAnimator ->
            val fraction = updatedAnimation.animatedFraction
            map.controller.setCenter(
                GeoPoint(
                    startPoint.latitude + (targetPoint.latitude - startPoint.latitude) * fraction,
                    startPoint.longitude + (targetPoint.longitude - startPoint.longitude) * fraction
                )
            )
            if (targetZoom != startZoom) map.controller
                .setZoom(startZoom + (targetZoom - startZoom) * fraction)
        }
        move.interpolator = AccelerateDecelerateInterpolator()
        move.start()
    }
}

private fun setRequiredCenter(newCenter: GeoPoint) {
    requiredCenter = GeoPoint(newCenter)
//        Log.d("MyApp3", "setRequiredCenter " + newCenter.getLatitude() + " " + newCenter.getLongitude());
}

fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
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
fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

//class MapEventsReceiverImpl : MapEventsReceiver {
//    var lat=0.0
//    var lng=0.0
//
//    fun getLocation(lat:Double, lng:Double):MapLocation{
//        return MapLocation(lat, lng)
//    }
//
//    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
//        lat=p?.latitude ?: 0.0
//        lng=p?.longitude ?: 0.0
//        Log.e("singleTapConfirmedHelper", "${lat} - ${lng}")
//        getLocation(lat, lng)
//        return true
//    }
//    override fun longPressHelper(p: GeoPoint?): Boolean {
//        Log.e("longPressHelper", "${p?.latitude} - ${p?.longitude}")
//        return false
//    }
//    data class MapLocation(
//        val latitude: Double,
//        val longitude: Double
//    )
//}
