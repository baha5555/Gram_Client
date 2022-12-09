@file:Suppress("DEPRECATION")

package com.example.gramclient.presentation.components

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavHostController
import com.example.gramclient.R
import com.example.gramclient.RoutesName
import com.example.gramclient.domain.mainScreen.Address
import com.example.gramclient.presentation.mainScreen.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

private lateinit var map: MapView
private var requiredCenter: GeoPoint? = null
lateinit var mRotationGestureOverlay: RotationGestureOverlay
lateinit var mLocationOverlay: MyLocationNewOverlay

@Composable
fun CustomMainMap(mainViewModel: MainViewModel, navController: NavHostController) {

    val fromAddress=mainViewModel.from_address.observeAsState()
    val toAddress=mainViewModel.to_address.observeAsState()

    AndroidView(factory = {
        View.inflate(it, R.layout.map, null)

    },
        update = {
            //val roadManager:RoadManager=OSRMRoadManager(it.context, "GramDriver/1.0")
            map = it.findViewById(R.id.map)
            val btnLocation = it.findViewById<ImageButton>(R.id.btnLocation)
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if(currentRoute==RoutesName.MAIN_SCREEN){
                btnLocation.margin(0f, 0f, 0f, 355f)
                btnLocation.visibility=View.GONE
            }else if(currentRoute==RoutesName.SEARCH_DRIVER_SCREEN){
                btnLocation.visibility=View.GONE
            }


            Configuration.getInstance()
                .load(it.context, PreferenceManager.getDefaultSharedPreferences(it.context))
            map.setTileSource(TileSourceFactory.MAPNIK)
            val startPoint = GeoPoint(40.27803692395751, 69.62923931506361)
            val mapController = map.controller
            mapController.setZoom(18.0)
            map.controller.setCenter(startPoint)

            mRotationGestureOverlay = RotationGestureOverlay(it.context, map)
            mRotationGestureOverlay.isEnabled = true

            map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            map.setMultiTouchControls(true)

            map.minZoomLevel = 10.0
            map.maxZoomLevel = 24.0

            val myLocationProvider = GpsMyLocationProvider(it.context)
            mLocationOverlay = MyLocationNewOverlay(myLocationProvider, map)
            myLocationShow(it.context, mLocationOverlay)
            btnLocation.setOnClickListener {
                try {
                    if (map.overlays.contains(mLocationOverlay)) {
                        val myLocation: GeoPoint = mLocationOverlay.myLocation
                        if (myLocation.latitude != 0.0 && myLocation.longitude != 0.0) {
                            //Log.d("MyApp3", "jumpToLocation " + myLocation.getLatitude() + ";" + myLocation.getLongitude());
                            jump(myLocation)
                            return@setOnClickListener
                        }
                    }
                } catch (_: Exception) {
                }
            }
            map.overlays.clear()
            addOverlays()
            showRoadAB(it.context, fromAddress, toAddress)
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
    fromAddress: State<Address?>,
    toAddress: State<MutableList<Address>?>,
) {
    val roadManager: RoadManager = OSRMRoadManager(context, "GramDriver/1.0")
    GlobalScope.launch {
        try {
            val waypoints = ArrayList<GeoPoint>()
//            val startPoint = GeoPoint(40.27803692395751, 69.62923931506361)
//            val startPoint2 = GeoPoint(41.27803692395751, 70.62923931506361)
            val fromAddressPoint: GeoPoint = GeoPoint(0, 0)
            fromAddressPoint.latitude =
                fromAddress.value?.lat?.toDouble() ?: 0.0
            fromAddressPoint.longitude =
                fromAddress.value?.lng?.toDouble() ?: 0.0
            waypoints.add(fromAddressPoint)


            val toAddressesPoints = ArrayList<GeoPoint>()
            val toAddressesNames = ArrayList<String>()
            toAddress.value?.forEach { address ->
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
                fromAddress.value?.let {
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

