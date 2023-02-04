package com.example.gramclient.presentation.screens.map

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
class Markers(private val context: Context, private val map: MapView) {
    val inxDriverMarker = mutableStateOf(-1)
    fun addMarker(geoPoint: GeoPoint, title: String, icon: Int) {
        val firstMarker = Marker(map)
        firstMarker.position = geoPoint
        firstMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        firstMarker.icon = ContextCompat.getDrawable(context, icon)
        firstMarker.title = title
        map.overlays.add(firstMarker)
    }
    fun addDriverMarker(geoPoint: GeoPoint, title: String, icon: Int) {
        val marker = Marker(map)
        marker.position = geoPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        marker.icon = ContextCompat.getDrawable(context, icon)
        marker.title = title
        if(inxDriverMarker.value == -1){
            map.overlays.add(marker)
            inxDriverMarker.value = map.overlays.lastIndexOf(marker)
        }else{
            map.overlays.set(inxDriverMarker.value, marker)
        }
        Log.i("addMarker", "inx-"+ inxDriverMarker.value)
    }
}