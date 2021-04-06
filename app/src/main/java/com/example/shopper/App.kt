package com.example.shopper

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

/**
 * This class creates a Notification Channel for Shopper. Notification Channels became
 * necessary starting with Android Oreo (API 26) to be able to show notifications. The
 * Notification Channel for Shopper will be created one time as soon as the application
 * starts and will be available for all other Activity classes to use.
 */
class App : Application() {
    // override the onCreate method
    override fun onCreate() {
        super.onCreate()

        // call the method that creates the notification channel
        createNotificationChannel()
    }

    /**
     * This method creates the Notification Channel for Shopper
     */
    private fun createNotificationChannel() {
        // check if Android Oreo (API 26) or higher because Notification Channels weren't
        // available on lower versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // initialize Notification Channel - must give it an Id, name, and impartance
            // level
            val channelshopper = NotificationChannel(
                    CHANNEL_SHOPPER_ID,
                    "Channel Shopper",
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            // customize the Notification Channel - set its description
            channelshopper.description = "This is the Shopper Channel."

            // initialize a Notification Manager
            val manager = getSystemService(NotificationManager::class.java)

            // create Shopper Notification Channel
            manager.createNotificationChannel(channelshopper)
        }
    }

    companion object {
        // declare and initialize a Channel Id
        const val CHANNEL_SHOPPER_ID = "channelshopper"
    }
}