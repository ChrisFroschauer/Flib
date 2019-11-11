package com.hawla.flib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent




class TitleScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.title_screen)

        //
        val buttonOptions : Button = findViewById(R.id.show_options)
        buttonOptions.setOnClickListener{
            val intentShowSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentShowSettings)
        }

    }

}
