package com.rsicarelli.homehunt.presentation.map.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory.*
import com.google.maps.android.ktx.awaitMap
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.propertyDetail.components.rememberMapViewWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PropertiesMapView(
    modifier: Modifier = Modifier,
    onMarkerClick: (Property) -> Unit,
    onMapClick: () -> Unit,
    locations: List<Property>,
) {
    val mapView = rememberMapViewWithLifecycle(false)
    val markers: HashMap<String, Marker> by remember { mutableStateOf(hashMapOf()) }
    val context = LocalContext.current

    AndroidView({ mapView }, modifier = modifier) { map ->
        CoroutineScope(Dispatchers.Main).launch {
            map.awaitMap().apply {
                uiSettings.isMapToolbarEnabled = false

                val viewedMarker = context.getBitmapDescriptor(R.drawable.ic_round_marker_grey)
                val notViewedMarker = context.getBitmapDescriptor(R.drawable.ic_round_marker_blue)

                if (markers.size != locations.size) {
                    val markersMap = hashMapOf<String, Marker>()
                    val latLngBuilder1 = LatLngBounds.Builder()
                    locations.map { property ->
                        val latLng = LatLng(property.location.lat, property.location.lng)
                        val icon = if (property.viewedByMe()) viewedMarker else notViewedMarker

                        latLngBuilder1.include(latLng)

                        addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .icon(icon)
                        )?.let {
                            it.tag = property
                            markersMap[property.reference] = it
                        }
                    }
                    markers.putAll(markersMap)
                    moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder1.build(), 100))
                }

                setOnMarkerClickListener {
                    it.setIcon(viewedMarker)
                    onMarkerClick(it.tag as Property)
                    false //allow map to center marker on screen
                }

                setOnMapClickListener {
                    onMapClick()
                }
            }
        }
    }
}

private fun Context.getBitmapDescriptor(id: Int): BitmapDescriptor? {
    return ContextCompat.getDrawable(this, id)?.let {
        it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
        val bm: Bitmap =
            Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        it.draw(canvas)
        return fromBitmap(bm)
    }
}