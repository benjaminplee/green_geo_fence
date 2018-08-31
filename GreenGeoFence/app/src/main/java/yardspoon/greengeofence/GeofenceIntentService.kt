package yardspoon.greengeofence

import android.app.IntentService
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceIntentService(name: String) : IntentService(name) {

    private val TAG = "GeofenceItentService:$name"

    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent.hasError()) {
            showMsg("Error with geofence intent!")
            return
        }

        showMsg("Received geofence event! ${toPrettyName(geofencingEvent.geofenceTransition)}")
    }

    private fun toPrettyName(geofenceTransition: Int): String = when(geofenceTransition) {
        Geofence.GEOFENCE_TRANSITION_ENTER -> "ENTER"
        Geofence.GEOFENCE_TRANSITION_DWELL -> "DWELL"
        Geofence.GEOFENCE_TRANSITION_EXIT -> "EXIT"
        else -> "CONFUSED"
    }

    private fun showMsg(msg: String) {
        Log.i(TAG, msg)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

}
