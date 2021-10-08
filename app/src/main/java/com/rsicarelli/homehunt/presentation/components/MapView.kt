package com.rsicarelli.homehunt.presentation.components

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import com.rsicarelli.homehunt.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/*
* From https://medium.com/geekculture/google-maps-in-jetpack-compose-android-ae7b1ad84e9
* */
@Composable
fun MapView(
    modifier: Modifier = Modifier,
    lat: Double,
    lng: Double,
    isLiteMode: Boolean = false
) {
    val mapView = rememberMapViewWithLifecycle(isLiteMode)

    AndroidView({ mapView }, modifier = modifier) { map ->
        CoroutineScope(Dispatchers.Main).launch {
            map.awaitMap().apply {
                val propertyLocation = LatLng(lat, lng)

                uiSettings.isZoomControlsEnabled = true
                uiSettings.isMapToolbarEnabled = false
                uiSettings.setAllGesturesEnabled(false)

                moveCamera(CameraUpdateFactory.newLatLngZoom(propertyLocation, 14f))

                val markerOptions = MarkerOptions()
                    .position(propertyLocation)

                addMarker(markerOptions)
            }
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(isLiteMode: Boolean): MapView {
    val context = LocalContext.current
    val mapView = remember {
        val options = GoogleMapOptions()
            .liteMode(isLiteMode)
        MapView(context, options).apply {
            id = R.id.map
//            isClickable = false
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }