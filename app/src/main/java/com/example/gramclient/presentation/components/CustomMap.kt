package com.example.gramclient.presentation.components

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
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
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
lateinit var map: MapView
private var requiredCenter: GeoPoint? = null
@Composable
fun CustomMap() {
    AndroidView(factory = {
        View.inflate(it, R.layout.map, null)

    },
        update = {
            //val roadManager:RoadManager=OSRMRoadManager(it.context, "GramDriver/1.0")
            map=it.findViewById<MapView>(R.id.map)
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
                        val myLocation: GeoPoint = mLocationOverlay.getMyLocation()
                        if (myLocation.latitude != 0.0 && myLocation.longitude != 0.0) {
                            //Log.d("MyApp3", "jumpToLocation " + myLocation.getLatitude() + ";" + myLocation.getLongitude());
                            jump(myLocation)
                            return@setOnClickListener
                        }
                    }
                }catch (e: Exception){

                }
            }
        }
    )




}

fun myLocationShow(context: Context?, mLocationOverlay: MyLocationNewOverlay) {
    val person: Bitmap = context?.let { getBitmap(it, R.drawable.ic_person) }!!
    val arrow: Bitmap = getBitmap(context, R.drawable.ic_navigation)!!
    mLocationOverlay.setPersonHotspot(person.width / 2f, person.height / 2f)
    mLocationOverlay.setPersonIcon(person)
    mLocationOverlay.setDirectionArrow(person, arrow)
    mLocationOverlay.enableMyLocation()

}

fun getBitmap(context: Context, resID: Int): Bitmap? {
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
    if (map.getWidth() == 0 || map.getHeight() == 0) { // no animation possible
        map.getController().setCenter(targetPoint)
        return
    }
    val startPoint = GeoPoint(map.getMapCenter())
    val startZoom: Double = map.getZoomLevelDouble()
    val targetZoom = if (requestedZoom == 0.0) startZoom else requestedZoom
    val points: MutableList<GeoPoint> = ArrayList()
    points.add(startPoint)
    points.add(targetPoint)
    val boundingBox1 = BoundingBox.fromGeoPoints(points)
    val intermediateZoom =
        MapView.getTileSystem().getBoundingBoxZoom(boundingBox1, map.getWidth(), map.getHeight())
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
                map.getController().setZoom(startZoom + (intermediateZoom - startZoom) * fraction)
            } catch (ignored: Exception) {
            }
        }
        zoomOut.interpolator = AccelerateInterpolator()
        val move = ValueAnimator.ofFloat(0f, 1f)
        move.duration = 500.toLong()
        move.addUpdateListener { updatedAnimation: ValueAnimator ->
            val fraction = updatedAnimation.animatedFraction
            map.getController().setCenter(
                GeoPoint(
                    startPoint.getLatitude() + (targetPoint.latitude - startPoint.getLatitude()) * fraction,
                    startPoint.getLongitude() + (targetPoint.longitude - startPoint.getLongitude()) * fraction
                )
            )
        }
        move.interpolator = AccelerateDecelerateInterpolator()
        val zoomIn = ValueAnimator.ofFloat(0f, 1f)
        zoomIn.duration = speed.toLong()
        zoomIn.addUpdateListener { updatedAnimation: ValueAnimator ->
            val fraction = updatedAnimation.animatedFraction
            map.getController()
                .setZoom(intermediateZoom + (targetZoom - intermediateZoom) * fraction)
            map.getController().setCenter(targetPoint) // important
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
            map.getController().setCenter(
                GeoPoint(
                    startPoint.getLatitude() + (targetPoint.latitude - startPoint.getLatitude()) * fraction,
                    startPoint.getLongitude() + (targetPoint.longitude - startPoint.getLongitude()) * fraction
                )
            )
            if (targetZoom != startZoom) map.getController()
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
