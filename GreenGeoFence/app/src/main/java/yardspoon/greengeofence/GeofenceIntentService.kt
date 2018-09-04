package yardspoon.greengeofence

import android.app.IntentService
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceIntentService(name: String) : IntentService(name) {

    constructor() : this("GeofenceIntentService") // Used by system to launch the service

    private val TAG = "GeofenceIntentService:$name"

    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            notify("PROBLEM WITH INTENT", "Error: ${GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)}")
            return
        }

        notify("Geofence Event", "${geofencingEvent.triggeringGeofences.joinToString("|") { it.requestId }} : ${toPrettyName(geofencingEvent.geofenceTransition)}")
    }

    private fun toPrettyName(geofenceTransition: Int): String = when (geofenceTransition) {
        Geofence.GEOFENCE_TRANSITION_ENTER -> "ENTER"
        Geofence.GEOFENCE_TRANSITION_DWELL -> "DWELL"
        Geofence.GEOFENCE_TRANSITION_EXIT -> "EXIT"
        else -> "CONFUSED"
    }

    private fun notify(title: String, text: String) {
        Log.i(TAG, title + " - " + text)
        applicationContext.launchNotification(title, text)
    }

}

fun createPendingIntent(context: Context): PendingIntent {
    val intent = Intent(context, GeofenceIntentService::class.java)
    // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
    // addGeofences() and removeGeofences().
    return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
}

