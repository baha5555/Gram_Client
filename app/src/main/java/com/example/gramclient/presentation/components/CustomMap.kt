package com.example.gramclient.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.preference.PreferenceManager
import android.provider.MediaStore.Images.Media.getBitmap
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.example.gramclient.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun CustomMap(context: Context) {
    lateinit var map: MapView
    AndroidView(factory = {
        View.inflate(it, R.layout.map, null)

    },
        update = {
            //val roadManager:RoadManager=OSRMRoadManager(it.context, "GramDriver/1.0")
            map=it.findViewById<MapView>(R.id.map)

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
            var mLocationOverlay = MyLocationNewOverlay(myLocationProvider, map)
            myLocationShow(it.context, mLocationOverlay)
            map.overlays.add(mLocationOverlay)

        }
    )




}

fun myLocationShow(context: Context?, mLocationOverlay: MyLocationNewOverlay) {
    var person: Bitmap = context?.let { getBitmap(it, R.drawable.ic_person) }!!
    val arrow: Bitmap = getBitmap(context, R.drawable.ic_navigation)!!
    mLocationOverlay?.setPersonHotspot(person.width / 2f, person.height / 2f)
    mLocationOverlay?.setPersonIcon(person)
    mLocationOverlay?.setDirectionArrow(person, arrow)
    mLocationOverlay?.enableMyLocation()

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