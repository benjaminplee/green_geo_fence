package yardspoon.greengeofence

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

const val ONE_HOUR_Ms = 60 * 60 * 1000
const val ONE_DAY_Ms = 24 * 60 * 60 * 1000
const val TEN_MINUTES_Ms = 10 * 60 * 1000
const val ONE_MINUTE_Ms = 60 * 1000
const val TWO_MINUTES_Ms = 2 * 60 * 1000
const val FIVE_SECONDS_Ms = 5 * 1000

val FENCES = listOf(
        Fence("WheelHouse", 38.6233567, -90.1985937, 72F, ONE_DAY_Ms.toLong(), TWO_MINUTES_Ms, FIVE_SECONDS_Ms),
        Fence("CourtHouse", 38.62532, -90.1966915, 100F, ONE_DAY_Ms.toLong(), TWO_MINUTES_Ms, FIVE_SECONDS_Ms),
        Fence("FlyingSaucer", 38.623025, -90.1961385, 115F, ONE_DAY_Ms.toLong(), TWO_MINUTES_Ms, FIVE_SECONDS_Ms),
        Fence("Culinaria", 38.6293121, -90.1937474, 100F, ONE_DAY_Ms.toLong(), TWO_MINUTES_Ms, FIVE_SECONDS_Ms),
        Fence("Arch", 38.6246488, -90.1848053, 200F, ONE_DAY_Ms.toLong(), TWO_MINUTES_Ms, FIVE_SECONDS_Ms),
        Fence("Park Avenue", 38.63034, -90.1943134, 100F, ONE_DAY_Ms.toLong(), TWO_MINUTES_Ms, FIVE_SECONDS_Ms),
        Fence("City Garden", 38.6269703, -90.195089, 100F, ONE_DAY_Ms.toLong(), TWO_MINUTES_Ms, FIVE_SECONDS_Ms)
)

class MainActivity : AppCompatActivity(), PermissionListener {
    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        geofencingClient = LocationServices.getGeofencingClient(this)
    }

    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
        token?.continuePermissionRequest()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        finish()
    }

    private lateinit var geofencingClient: GeofencingClient
    private lateinit var eventBusSubscription: Disposable

    private val geofencePendingIntent: PendingIntent by lazy { createPendingIntent(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this)
                .check()

        fab.setOnClickListener { _ ->
            addGeoFences()
        }

        eventBusSubscription = appEventBus
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::postToLogContainer) { postToLogContainer("ERROR: ${it.message}") }
    }

    override fun onDestroy() {
        eventBusSubscription.dispose()
        super.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun postToLogContainer(msg: String) {
        logContainer.text = msg + "\n" + logContainer.text
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

        geofencingClient.addGeofences(buildGeofenceRequest2(FENCES), geofencePendingIntent)?.run {
            addOnSuccessListener {
                showMsg("Added geofences2")
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

        geofencingClient.removeGeofences(FENCES.map { it.id + "2" })?.run {
            addOnSuccessListener {
                showMsg("Removed geofences2")
            }
            addOnFailureListener {
                showMsg("Error removing geofences2: ${it.message}")
            }
        }
    }

    private fun showMsg(msg: String) {
        Log.i("MainActivity", msg)
        postToLogContainer(msg)
//        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
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