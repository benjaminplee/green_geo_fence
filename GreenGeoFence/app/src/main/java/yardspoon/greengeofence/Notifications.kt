package yardspoon.greengeofence

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import java.util.*

private const val NOTIFICATION_CHANNEL_ID = "GREENGEOFENCE_DEFAULT"
private const val NOTIFICATION_CHANNEL_NAME = "Geo Fence Events"
private const val NOTIFICATION_CHANNEL_DESC = "Events for geo fence entry, loiter, and leave."

fun Context.launchNotification(title: String = "Hello", text: String = "World"): Int {
    setupNotificationChannelsForOreoAndHigher(this)

    return showNotification(
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(text))
//                    .setContentIntent(intent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
    )
}

private fun setupNotificationChannelsForOreoAndHigher(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        setupNotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH, NOTIFICATION_CHANNEL_DESC, context)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun setupNotificationChannel(channelId: String, name: String?, importance: Int, description: String?, context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    val channel = NotificationChannel(channelId, name, importance)
    channel.description = description
    // Register the channel with the system; you can't change the importance
    // or other notification behaviors after this
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
}

private fun showNotification(builder: NotificationCompat.Builder): Int {
    val id = Random().nextInt()
    NotificationManagerCompat.from(builder.mContext).notify(id, builder.build())
    return id
}