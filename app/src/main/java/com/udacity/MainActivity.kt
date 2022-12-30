package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

    var repoName: String = ""
    var downloadStatus: String = ""

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private var repository: Repository = Repository.NONE
    private enum class Repository(val url: String) {
        NONE(""),
        BUMPTECH("https://github.com/bumptech/glide"),
        UDACITY("https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"),
        RETROFIT("https://github.com/square/retrofit")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.buttonState = ButtonState.Start
        custom_button.setOnClickListener {
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            custom_button.buttonState = ButtonState.Completed
            downloadStatus = "SUCCESS"
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            createChannel(getString(R.string.download_notification_channel_id), getString(R.string.download_notification_channel_name))
            val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager

            notificationManager.sendNotification(
                this@MainActivity.getText(R.string.notification_description).toString(),
                applicationContext
            )

        }
    }

    private fun download() {
        Log.d("WWD", "in download")
      // loadingButton.buttonState = ButtonState.Clicked
        if (repository == Repository.NONE) {
            Toast.makeText(this, "PLEASE SELECT A URL ABOVE", Toast.LENGTH_LONG).show()
            return;
        }
        custom_button.buttonState = ButtonState.Loading
        var downloadUrl = repository.url + "archive/master.zip"
        val request =
            DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

    fun onRadioButtonClicked(view: View) {
        Log.d("WWD", "radio button clicked")
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_glide ->
                    if (checked) {
                        repository = Repository.BUMPTECH
                        repoName = "BUMPTECH"
                    }
                R.id.radio_udacity ->
                    if (checked) {
                        repository = Repository.UDACITY
                        repoName = "UDACITY"
                    }
                R.id.radio_retrofit ->
                    if (checked) {
                        repository = Repository.RETROFIT
                        repoName = "RETROFIT"
                    }
                else -> {
                    repository = Repository.NONE
                    repoName = "NONE"
                }

            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)
            val notificationManager = this.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }

}
