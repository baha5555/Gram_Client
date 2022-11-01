package com.example.gramclient.presentation.components

import android.preference.PreferenceManager
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.gramclient.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay

@Composable
fun CustomMap(
) {
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

        }
    )
}
