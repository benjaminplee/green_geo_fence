package yardspoon.greengeofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices

import kotlinx.android.synthetic.main.activity_main.*

const val ONE_HOUR_Ms = 60 * 60 * 1000
const val TEN_MINUTES_Ms = 10 * 60 * 1000
const val ONE_MINUTE_Ms = 60 * 1000
const val FIVE_SECONDS_Ms = 5 * 1000

val BLOCK_A = Fence("BLOCK-A", 38.6233567, -90.1985937, 72F, ONE_HOUR_Ms.toLong(), ONE_MINUTE_Ms, FIVE_SECONDS_Ms)
val BLOCK_B = Fence("BLOCK-B", 38.62532, -90.1966915, 100F, ONE_HOUR_Ms.toLong(), ONE_MINUTE_Ms, FIVE_SECONDS_Ms)
val BLOCK_C = Fence("BLOCK-C", 38.623025, -90.1961385, 115F, ONE_HOUR_Ms.toLong(), ONE_MINUTE_Ms, FIVE_SECONDS_Ms)
val FENCES = listOf(BLOCK_A, BLOCK_B, BLOCK_C)

class MainActivity : AppCompatActivity() {

    private lateinit var geofencingClient: GeofencingClient

    private val geofencePendingIntent: PendingIntent by lazy { createPendingIntent(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        geofencingClient = LocationServices.getGeofencingClient(this)

        fab.setOnClickListener { _ ->
            addGeoFences()
        }
    }

    @SuppressLint("MissingPermission")
    private fun addGeoFences() {
        geofencingClient.addGeofences(buildGeofenceRequest(FENCES), geofencePendingIntent)?.run {
            addOnSuccessListener {
                showMsg("Added geofences")
            }
            addOnFailureListener {
                showMsg("Failure adding geofences: ${it.message}")
            }
        }
    }

    private fun removeGeoFences() {
        geofencingClient.removeGeofences(FENCES.map { it.id })?.run {
            addOnSuccessListener {
                showMsg("Removed geofences")
            }
            addOnFailureListener {
                showMsg("Error removing geofences: ${it.message}")
            }
        }

    }

    private fun showMsg(msg: String) {
        Log.i("MainActivity", msg)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
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
            R.id.action_remove_all -> {
                removeGeoFences()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}