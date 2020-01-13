package com.hawla.flib.views.settings

import android.R
import android.content.Context
import android.content.DialogInterface
import android.content.res.TypedArray
import androidx.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.NumberPicker


class NumberPickerPreference(context: Context, attrs: AttributeSet) :
    DialogPreference(context, attrs) {

    private lateinit var picker: NumberPicker
    private var initialValue: Int? = null

   /* override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        this.picker = view.findViewById(R.id.pref_num_picker) as NumberPicker
        // TODO this should be an XML parameter:
        picker.setRange(1, 7)
        if (this.initialValue != null) picker.setCurrent(initialValue)
    }*/

    override fun onSetInitialValue(
        restorePersistedValue: Boolean,
        defaultValue: Any?
    ) {
        val def = if (defaultValue is Number)
            defaultValue as Int
        else if (defaultValue != null) Integer.parseInt(defaultValue.toString()) else 1
        if (restorePersistedValue) {
            this.initialValue = getPersistedInt(def)
        } else
            this.initialValue = defaultValue as Int
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInt(index, 1)
    }
}