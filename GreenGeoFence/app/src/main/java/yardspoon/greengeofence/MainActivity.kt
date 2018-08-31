package yardspoon.greengeofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var geofencingClient: GeofencingClient

    private val geofenceId = "BENTESTID"
    private val geofenceLatitude = 38.625901 // 43.1065596
    private val geofenceLongitude = -90.1964245 // -77.5431836
    private val geofenceRadiusInMeters = 66.0F //150.0F
    private val geofenceExpirationInMiliseconds = 10L * 60L * 1000L
    private val geofenceLoiterDelayInMiliseconds = 60 * 1000

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceIntentService::class.java)
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        LocationServices.GeofencingApi

        geofencingClient = LocationServices.getGeofencingClient(this)

        fab.setOnClickListener { _ ->
            addGeoFence()
        }
    }

    private fun showMsg(msg: String) {
        log(msg)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun log(msg: String) {
        Log.i("MainActivity", msg)
    }

    @SuppressLint("MissingPermission")
    private fun addGeoFence() {
        geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
            addOnSuccessListener {
                showMsg("Added geofence for 10 minutes")
            }
            addOnFailureListener {
                showMsg("Failure adding geofence: ${it.message}")
            }
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(listOf(Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(geofenceId)

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            geofenceLatitude,
                            geofenceLongitude,
                            geofenceRadiusInMeters
                    )

                    .setNotificationResponsiveness(10 * 1000)

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(geofenceExpirationInMiliseconds)

                    .setLoiteringDelay(geofenceLoiterDelayInMiliseconds)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build()))
        }.build()
    }

    override fun onDestroy() {
        geofencingClient.removeGeofences(geofencePendingIntent)?.run {
            addOnSuccessListener {
                log("Removed geofences")
            }
            addOnFailureListener {
                log("Error removing geofences: ${it.message}")
            }
        }
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
