package com.sysaxiom.alarmmanager.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sysaxiom.alarmmanager.R
import com.sysaxiom.alarmmanager.set.SetAlarmActivity
import com.sysaxiom.alarmmanager.setrepeating.SetRepeatingAlarmActivity

class MainActivity : AppCompatActivity() {

    lateinit var buttonSetAlarm : Button
    lateinit var buttonSetRepeatingAlarm : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSetAlarm = findViewById(R.id.button_set_alarm)
        buttonSetAlarm.setOnClickListener {
            Intent(this, SetAlarmActivity::class.java).also {
                this.startActivity(it)
            }
        }

        buttonSetRepeatingAlarm = findViewById(R.id.button_set_repeating_alarm)
        buttonSetRepeatingAlarm.setOnClickListener {
            Intent(this, SetRepeatingAlarmActivity::class.java).also {
                this.startActivity(it)
            }
        }
    }
}
