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
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.example.gramclient.R
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
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
private lateinit var map: MapView
private var requiredCenter: GeoPoint? = null
@Composable
fun CustomMainMap(mainViewModel: Lazy<MainViewModel>) {
    AndroidView(factory = {
        View.inflate(it, R.layout.map, null)

    },
        update = {
            //val roadManager:RoadManager=OSRMRoadManager(it.context, "GramDriver/1.0")
            map=it.findViewById(R.id.map)
            val btnLocation=it.findViewById<ImageButton>(R.id.btnLocation)
            

            Configuration.getInstance()
                .load(it.context, PreferenceManager.getDefaultSharedPreferences(it.context))
            map.setTileSource(TileSourceFactory.MAPNIK)
            val startPoint = GeoPoint(40.27803692395751, 69.62923931506361)
            val mapController = map.controller
            mapController.setZoom(18.0)
            map.controller.setCenter(startPoint)

            val mRotationGestureOverlay = RotationGestureOverlay(it.context, map)
            mRotationGestureOverlay.isEnabled = true
            map.overlays.add(mRotationGestureOverlay)

            map.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            map.setMultiTouchControls(true)

            map.minZoomLevel = 14.0
            map.maxZoomLevel = 22.0

            val myLocationProvider = GpsMyLocationProvider(it.context)
            val mLocationOverlay = MyLocationNewOverlay(myLocationProvider, map)
            myLocationShow(it.context, mLocationOverlay)
            map.overlays.add(mLocationOverlay)
            btnLocation.setOnClickListener{
                try {
                    if (map.overlays.contains(mLocationOverlay)) {
                        val myLocation: GeoPoint = mLocationOverlay.myLocation
                        if (myLocation.latitude != 0.0 && myLocation.longitude != 0.0) {
                            //Log.d("MyApp3", "jumpToLocation " + myLocation.getLatitude() + ";" + myLocation.getLongitude());
                            jump(myLocation)
                            return@setOnClickListener
                        }
                    }
                }catch (_: Exception){
                }
            }
            showRoadAB(it.context, mainViewModel)
        }
    )
}
@OptIn(DelicateCoroutinesApi::class)
fun showRoadAB(
    context: Context,
    mainViewModel: Lazy<MainViewModel>
) {
    val roadManager: RoadManager = OSRMRoadManager(context, "GramDriver/1.0")
    GlobalScope.launch {
        try {
            val waypoints = java.util.ArrayList<GeoPoint>()
            val geoPoint =  GeoPoint(mainViewModel.value.from_address.value?.lat?.toInt() ?: 0,
                mainViewModel.value.from_address.value?.lng?.toInt() ?: 0
            )
            if(geoPoint.latitude.toInt()!=0 && geoPoint.longitude.toInt()!=0) {
                waypoints.add(geoPoint)
            }else{
                return@launch
            }
            val geoPoint2 = GeoPoint(mainViewModel.value.to_address.value?.get(0)?.lat?.toInt() ?: 0,mainViewModel.value.to_address.value?.get(0)?.lng?.toInt() ?: 0)
            if(geoPoint2.latitude.toInt()!=0 && geoPoint2.longitude.toInt()!=0) else return@launch
            waypoints.add(geoPoint2)

            map.overlays.clear()
//            listOfLngLat.forEach { pair ->
//                val geoPoint = GeoPoint(pair.first.first.toDouble(), pair.first.second.toDouble())
//                waypoints.add(geoPoint)
//            }
            val road = roadManager.getRoad(waypoints)
            val roadOverlay = RoadManager.buildRoadOverlay(road)
            val blueColorValue: Int = Color.parseColor("#36457C")
            roadOverlay.color = blueColorValue
            roadOverlay.width = 15f
            roadOverlay.paint.strokeJoin = Paint.Join.ROUND
            roadOverlay.paint.strokeCap = Paint.Cap.ROUND
            // roadOverlay.setGeodesic(true)
            map.overlays.add(roadOverlay)
        } catch (e: Exception) {

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
