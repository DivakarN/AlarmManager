package com.sysaxiom.alarmmanager.set

import android.app.Dialog
import android.text.format.DateFormat.is24HourFormat
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


class TimePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        return TimePickerDialog(
            getActivity(),
            getActivity() as TimePickerDialog.OnTimeSetListener,
            hour,
            minute,
            is24HourFormat(getActivity())
        )
    }
}