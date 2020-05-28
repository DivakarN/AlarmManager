package com.sysaxiom.alarmmanager.set

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import com.sysaxiom.alarmmanager.R
import java.text.DateFormat
import java.util.*

class SetAlarmActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    lateinit var mTextView: TextView
    lateinit var buttonTimePicker : Button
    lateinit var buttonCancelAlarm : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)

        mTextView = findViewById(R.id.textView)

        buttonTimePicker = findViewById(R.id.button_start)
        buttonTimePicker.setOnClickListener {
            val timePicker = TimePickerFragment()
            timePicker.show(supportFragmentManager, "time picker")
        }

        buttonCancelAlarm = findViewById(R.id.button_cancel)
        buttonCancelAlarm.setOnClickListener {
            cancelAlarm()
        }
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)
        c.set(Calendar.SECOND, 0)
        updateTimeText(c)
        startAlarm(c)
    }

    private fun updateTimeText(c: Calendar) {
        var timeText = "Alarm set for: "
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime())
        mTextView.setText(timeText)
    }

    private fun startAlarm(c: Calendar) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SetAlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent)
    }

    private fun cancelAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SetAlertReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)
        alarmManager.cancel(pendingIntent)
        mTextView.setText("Alarm canceled")
    }
}
