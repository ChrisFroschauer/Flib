package com.hawla.flib.views.settings.helper

import android.text.InputFilter
import android.text.Spanned

class InputFilterMinMax : InputFilter {

    private val min: Int
    private val max: Int

    constructor(min: Int, max :Int){
        this.min = min
        this.max = max
    }

    constructor(min: String, max: String){
        this.min = Integer.parseInt(min)
        this.max = Integer.parseInt(max)
    }

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence {
        try{
            var input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(min, max, input)) return null!!
        }catch(nfe: NumberFormatException){
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        if (b>a){
            return c in a..b
        } else {
            return c in b..a
        }
    }

}