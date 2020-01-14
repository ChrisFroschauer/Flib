package com.hawla.flib.views.titlescreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import com.hawla.flib.R
import com.hawla.flib.views.game.GameActivity
import com.hawla.flib.views.settings.SettingsActivity


class TitlescreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.title_screen)

        // Endless Button Functions
        val buttonEndless : Button = findViewById(R.id.start_endless)
        buttonEndless.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameActivity.INTENT_TIMERACE, false)
            startActivity(intent)
        }

        // Endless Button Functions
        val buttonTimeRace : Button = findViewById(R.id.start_timerace)
        buttonTimeRace.setOnClickListener{
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameActivity.INTENT_TIMERACE, true)
            startActivity(intent)
        }


        // Setting Button Functions
        val buttonOptions : Button = findViewById(R.id.show_options)
        buttonOptions.setOnClickListener{
            val intentShowSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentShowSettings)
        }

    }

}
