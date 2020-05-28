package com.sysaxiom.alarmmanager.utils

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import java.lang.Exception

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try{
            Toast.makeText(context,"Notification Triggered",Toast.LENGTH_LONG).show()
            val notificationHelper = NotificationHelper(context)
            val nb = notificationHelper.channelNotification
            notificationHelper.manager?.notify(1, nb.build())
        } catch (e : Exception){
            e.printStackTrace()
        }
    }
}