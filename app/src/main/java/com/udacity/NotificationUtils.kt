package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)

    // TODO: Step 1.12 create PendingIntent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val cicadaImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.cicada
    )

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(cicadaImage)
        .bigLargeIcon(null)

    // Build the notification
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        //.setContentIntent(contentPendingIntent)
        .setStyle(bigPicStyle)
        .addAction(
            R.drawable.ic_baseline_check_circle_24,
            applicationContext.getString(R.string.icon_label),
            contentPendingIntent
        ) 


    // Deliver the notification
    notify(NOTIFICATION_ID, builder.build())
}

