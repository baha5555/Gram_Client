package com.gram.client.presentation.screens.map

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.res.ResourcesCompat
import com.gram.client.R
import com.gram.client.domain.mainScreen.Address
import com.gram.client.utils.Routes
import com.gram.client.utils.Values
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MapController(val context: Context) {
    private var zoomAnimation: ValueAnimator? = null
    private var targetZoom = 0.0

    @SuppressLint("SuspiciousIndentation")
    @OptIn(DelicateCoroutinesApi::class)
    fun showRoadAB(
        fromAddress: Address,
        toAddress: List<Address>?,
    ) {
        val roadManager: RoadManager = OSRMRoadManager(context, "GramDriver/1.0")

        GlobalScope.launch {
            try {
                //if( currentRoute== MapPointScreen().key || currentRoute== OrderExecutionMapPointScreen().key) return@launch
                if(Values.currentRoute.value == Routes.SEARCH_ADDRESS_SHEET || Values.currentRoute.value == Routes.MAP_POINT_SHEET) return@launch
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
                    //map.controller.animateTo(fromAddressPoint, 14.0, 1000)
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
    fun myLocationShow(mLocationOverlay: MyLocationNewOverlay) {
        val person: Bitmap = context?.let { getBitmap(R.drawable.ic_person) }!!
        val arrow: Bitmap = getBitmap(R.drawable.ic_navigation)!!
        mLocationOverlay.setPersonHotspot(person.width / 2f, person.height / 2f)
        mLocationOverlay.setPersonIcon(person)
        mLocationOverlay.setDirectionArrow(person, arrow)
        mLocationOverlay.enableMyLocation()
    }
    private fun getBitmap(resID: Int): Bitmap? {
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
}