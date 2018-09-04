package yardspoon.greengeofence

import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest

data class Fence(
        val id: String,
        val latitude: Double,
        val longitude: Double,
        val radiusMeters: Float,
        val expirationMs: Long,
        val loiterDelayMs: Int,
        val responsivenessMs: Int
)

fun buildGeofenceRequest(fences: List<Fence>): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
        addGeofences(fences.map {
            Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(it.id)

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            it.latitude,
                            it.longitude,
                            it.radiusMeters
                    )

                    .setNotificationResponsiveness(it.responsivenessMs)

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(it.expirationMs)

                    .setLoiteringDelay(it.loiterDelayMs)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT)

                    .build()
        })
    }.build()
}

fun buildGeofenceRequest2(fences: List<Fence>): GeofencingRequest {
    return GeofencingRequest.Builder().apply {
        setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL)
        addGeofences(fences.map {
            Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(it.id + "2")

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            it.latitude,
                            it.longitude,
                            it.radiusMeters
                    )

//                    .setNotificationResponsiveness(it.responsivenessMs)

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(it.expirationMs)

                    .setLoiteringDelay(it.loiterDelayMs)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_DWELL or Geofence.GEOFENCE_TRANSITION_EXIT)

                    .build()
        })
    }.build()
}
