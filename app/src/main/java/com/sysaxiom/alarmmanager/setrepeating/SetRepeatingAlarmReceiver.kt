package com.sysaxiom.alarmmanager.setrepeating

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sysaxiom.alarmmanager.set.NotificationHelper

class SetRepeatingAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try{
            val notificationHelper = NotificationHelper(context)
            val nb = notificationHelper.channelNotification
            notificationHelper.manager?.notify(1, nb.build())
        } catch (e : java.lang.Exception){
            e.printStackTrace()
        }
    }

}