package com.rsicarelli.homehunt.presentation.map.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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

    AndroidView({ mapView }, modifier = modifier) { map ->
        CoroutineScope(Dispatchers.Main).launch {
            map.awaitMap().apply {

                val latLngBuilder = LatLngBounds.Builder()
                locations.forEach { property ->
                    val latLng = LatLng(property.location.lat, property.location.lng)
                    latLngBuilder.include(latLng)
                    addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(property.reference)
                    )?.let {
                        it.tag = property
                    }
                }

                setOnMarkerClickListener {
                    onMarkerClick(it.tag as Property)

                    false
                }


                moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), 100))
            }
        }
    }
}