package com.sysaxiom.alarmmanager.setrepeating

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sysaxiom.alarmmanager.R
import com.sysaxiom.alarmmanager.utils.AlarmReceiver

class SetRepeatingAlarmActivity : AppCompatActivity() {

    lateinit var buttonStartAlarm : Button
    lateinit var buttonCancelAlarm : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_repeating_alarm)

        buttonStartAlarm = findViewById(R.id.button_start)
        buttonStartAlarm.setOnClickListener {
            Toast.makeText(this,"Alarm Started", Toast.LENGTH_LONG).show()
            startAlarm()
        }

        buttonCancelAlarm = findViewById(R.id.button_cancel)
        buttonCancelAlarm.setOnClickListener {
            Toast.makeText(this,"Alarm Stopped",Toast.LENGTH_LONG).show()
            cancelAlarm()
        }
    }

    private fun startAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000 ,pendingIntent)
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }
}
