package com.hawla.flib.views.settings

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.NumberPicker
import android.widget.TimePicker
import java.lang.Exception
import java.lang.reflect.Field

class MinuteSecondsPickerDialog : TimePickerDialog {
    private lateinit var mTimePicker: TimePicker
    private val callback: OnTimeSetListener

    private var initialMinutes: Int

    constructor(context: Context, callback: OnTimeSetListener, minutes: Int, seconds: Int) : super(context, callback, minutes, seconds, true) {
        //true in super removes the am/pm spinner
        this.callback = callback
        this.initialMinutes = minutes
        this.setTitle("TODO text-string")
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        if (callback != null && mTimePicker != null){
            mTimePicker.clearFocus()
            callback.onTimeSet(mTimePicker, mTimePicker.hour, mTimePicker.minute)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        try {
            var classForid: Class<*> = Class.forName("com.android.internal.R\$id")
            var timePickerField: Field = classForid.getDeclaredField("timePicker")
            this.mTimePicker = findViewById(timePickerField.getInt(null))

            // Modify the hours spinner to cover maximum supported minutes
            val maxMinutes = 160 //TODO magicNumber
            /*for (field in classForid.declaredFields){
                Log.println(Log.ERROR, "chris-tag", field.name)
            }*/
            var field: Field = classForid.getDeclaredField("time")//.getField("hour")
            var mHourSpinner: NumberPicker = mTimePicker.findViewById(field.getInt(null))
            mHourSpinner.minValue = 0
            mHourSpinner.maxValue = maxMinutes
            var displayedValues: List<String> = ArrayList<String>()
            for (i in 0..maxMinutes){
                displayedValues += String.format("%02d", i)
            }
            mHourSpinner.displayedValues = displayedValues.toTypedArray()
            mHourSpinner.value = initialMinutes
        }catch(e: Exception){
            e.printStackTrace()
            Log.println(Log.ERROR, "chris-tag", e.toString())
        }
    }
}