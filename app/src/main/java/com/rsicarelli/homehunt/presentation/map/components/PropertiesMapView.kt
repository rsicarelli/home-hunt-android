package com.rsicarelli.homehunt.presentation.map.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.propertyDetail.components.rememberMapViewWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PropertiesMapView(
    modifier: Modifier = Modifier,
    onMarkerClick: (Property) -> Unit,
    locations: List<Property>,
) {
    val mapView = rememberMapViewWithLifecycle(false)
    val markers: HashMap<String, Marker> by remember { mutableStateOf(hashMapOf()) }

    AndroidView({ mapView }, modifier = modifier) { map ->
        CoroutineScope(Dispatchers.Main).launch {
            map.awaitMap().apply {
                if (markers.size != locations.size) {

                    val latLngBuilder = LatLngBounds.Builder()
                    locations.map { property ->
                        val latLng = LatLng(property.location.lat, property.location.lng)
                        latLngBuilder.include(latLng)
                        addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title(property.reference)
                        )?.let {
                            it.tag = property
                            markers[property.reference] = it
                        }
                    }

                    moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), 100))
                }

                setOnMarkerClickListener {
                    onMarkerClick(it.tag as Property)
                    false //allow map to center marker on screen
                }
            }
        }
    }
}