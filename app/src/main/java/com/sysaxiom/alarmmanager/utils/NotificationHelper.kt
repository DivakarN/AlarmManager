package com.sysaxiom.alarmmanager.utils

import android.app.NotificationManager
import android.app.NotificationChannel
import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import androidx.core.app.NotificationCompat
import com.sysaxiom.alarmmanager.R


class NotificationHelper(base: Context) : ContextWrapper(base) {
    private var mManager: NotificationManager? = null
    val manager: NotificationManager?
        get() {
            if (mManager == null) {
                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return mManager
        }
    val channelNotification = NotificationCompat.Builder(applicationContext,
        channelID
    )
            .setContentTitle("Alarm!")
            .setContentText("Your AlarmManager is working.")
            .setSmallIcon(R.drawable.ic_android)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val channel =
            NotificationChannel(
                channelID,
                channelName, NotificationManager.IMPORTANCE_HIGH)
        manager!!.createNotificationChannel(channel)
    }

    companion object {
        val channelID = "channelID"
        val channelName = "Channel Name"
    }
}