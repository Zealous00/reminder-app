package com.fekete.david.reminderapp.ui.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.fekete.david.reminderapp.data.entitiy.Reminder
import com.fekete.david.reminderapp.util.rememberMapViewWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.util.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.remember as remember

@Composable
fun ReminderLocation(navController: NavController) {
    val currentEntry = navController.currentBackStackEntry
    val serializedReminder = currentEntry?.arguments?.get("serializedReminder")
    var reminder = Reminder()
    if (serializedReminder != null) {
        reminder = Gson().fromJson(serializedReminder.toString(), Reminder::class.java)
    }

    val currentMarker = remember { mutableStateOf<Marker?>(null) }
    val mapView: MapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    AndroidView({ mapView }) {
        coroutineScope.launch {
            val map = mapView.awaitMap()
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isScrollGesturesEnabled = true
//            val location = LatLng(65.06, 25.47)

            val isEmptyLatitude = reminder.locationX.isEmpty()
            val isEmptyLongitude = reminder.locationY.isEmpty()
            val latitude = if (!isEmptyLatitude) reminder.locationX.toDouble() else 65.06
            val longitude = if (!isEmptyLongitude) reminder.locationY.toDouble() else 25.47

            val location = LatLng(latitude, longitude)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    10f
                )
            )


            if (!isEmptyLatitude && !isEmptyLongitude) {
                currentMarker.value = map.addMarker(
                    MarkerOptions().position(location)
                )
            }
            setMapLongClick(map, currentMarker, reminder, navController)
        }
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    currentMarker: MutableState<Marker?>,
    reminder: Reminder,
    navController: NavController
) {
    map.setOnMapLongClickListener { latlng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2$.2f",
            latlng.latitude,
            latlng.longitude
        )

        currentMarker.value?.remove()
        val newMarker = map.addMarker(
            MarkerOptions().position(latlng).title("Reminder location").snippet(snippet)
        )

        currentMarker.value = newMarker


        CoroutineScope(Dispatchers.Main).launch {
            reminder.locationX = latlng.latitude.toString()
            reminder.locationY = latlng.longitude.toString()
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("reminder", reminder)
            delay(500L)
            navController.popBackStack()
        }
    }
}