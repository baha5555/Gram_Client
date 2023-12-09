package com.gram.client.presentation.screens.map

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.gram.client.R
import com.gram.client.domain.mainScreen.Address
import com.gram.client.presentation.components.voyager.MapPointScreen
import com.gram.client.presentation.components.voyager.OrderExecutionMapPointScreen
import com.gram.client.presentation.screens.main.SearchAddressScreen
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

class MapController(val context: Context) {
    fun showRoadAB(
        fromAddress: State<Address>,
        toAddress: SnapshotStateList<Address>,
    ) {
        if(currentRoute==SearchAddressScreen().key || currentRoute==MapPointScreen().key || currentRoute==OrderExecutionMapPointScreen().key) return
        val roadManager: RoadManager = OSRMRoadManager(context, "GramDriver/1.0")
        Log.d("showRoad", ""+fromAddress.value)

        GlobalScope.launch {
            try {
                val waypoints = ArrayList<GeoPoint>()
                val fromAddressPoint = GeoPoint(0, 0)
                fromAddressPoint.latitude = fromAddress.value.lat.toDouble()
                fromAddressPoint.longitude = fromAddress.value.lng.toDouble()
                waypoints.add(fromAddressPoint)


                val toAddressesPoints = ArrayList<GeoPoint>()
                val toAddressesNames = ArrayList<String>()
                toAddress.forEach { address ->
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
                val blueColorValue: Int = Color.parseColor("#009CC3")
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
                        title = fromAddress.value.name,
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
}