package com.hawla.flib.views.settings

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import com.hawla.flib.R
import com.hawla.flib.views.settings.MinuteSecondsPickerDialog
import com.hawla.flib.views.settings.SettingsActivity


class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private lateinit var mContext: Context
    private var mTimeTotalSeconds:Int = 0


    override fun onAttach(context: Context){
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var totalSeconds: Int = getTime()
        var minutes: Int = totalSeconds / 60
        var seconds: Int = totalSeconds % 60

        // Create a new instance of TimePickerDialog and return it
        return MinuteSecondsPickerDialog(
            mContext,
            this,
            minutes,
            seconds
        )
    }

    override fun onTimeSet(view: TimePicker?, minutes: Int, seconds: Int) {
        var totalSeconds: Int = (minutes * 60) + seconds
        setTime(totalSeconds)

    }

    fun getTime(): Int{
        var prefDefault: Int = mContext.resources.getInteger(R.integer.preferences_time_default)
        var sharedPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)
        mTimeTotalSeconds = sharedPref.getInt(SettingsActivity.KEY_PREF_GAME_TIME, prefDefault)
        return mTimeTotalSeconds
    }

    fun setTime(time: Int){
        mTimeTotalSeconds = time

        var editor: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit()
        editor.putInt(SettingsActivity.KEY_PREF_GAME_TIME, time)
        editor.commit()
    }


}
