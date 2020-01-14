package com.hawla.flib.views.settings

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hawla.flib.R


class SettingsActivity : AppCompatActivity(){

    companion object {
        val KEY_PREF_GAME_TIME = "game_time"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings_activity)

        // init Preferences
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.settings,
                SettingsFragment()
            )
            .commit()

        // Up button to parent
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    class SettingsFragment : PreferenceFragmentCompat() {
        lateinit var mContext:Context
        var patternButtons: Array<Button> = arrayOf()

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            //EditText of turns/hearts to only take numbers
            /*val editTextPreferenceTurns = findPreference<EditTextPreference>("turns")
            val editTextPreferenceHearts = findPreference<EditTextPreference>("hearts")
            editTextPreferenceTurns!!.setOnBindEditTextListener{
                editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER
                //editText.filters = arrayOf<InputFilter>(InputFilterMinMax(1, 30))
            }
            editTextPreferenceHearts!!.setOnBindEditTextListener{
                editText -> editText.inputType = InputType.TYPE_CLASS_NUMBER
            }*/

        }

        fun updateFullPattern(pattern: List<Boolean>){

            for((index, field) in pattern.withIndex()){
                if (field){
                    // true means the tile will flip
                    //TODO now in fragment
                    patternButtons[index].setBackgroundColor(Color.RED)
                }else{
                    patternButtons[index].setBackgroundColor(Color.WHITE)
                }
            }
        }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mContext = context;
        }


        // implemented for the TimePickerDialog
        // TODO uncomment: if you want to use timepicker:
        /*override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            when(preference!!.key) {
                KEY_PREF_GAME_TIME ->
                    showTimePickerDialog(preference)
            }
            return super.onPreferenceTreeClick(preference)
        }*/

        private fun showTimePickerDialog(preference: Preference) {
            var newFragment: DialogFragment = TimePickerFragment()
            newFragment.show(fragmentManager!!, "timePicker")
        }
    }

}