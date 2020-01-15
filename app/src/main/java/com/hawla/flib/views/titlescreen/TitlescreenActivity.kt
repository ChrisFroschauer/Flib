package com.hawla.flib.views.titlescreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.text.Html
import android.widget.TextView
import com.hawla.flib.R
import com.hawla.flib.views.game.GameActivity
import com.hawla.flib.views.game.LoadGameActivity
import com.hawla.flib.views.highscore.HighscoreActivity
import com.hawla.flib.views.settings.SettingsActivity


class TitlescreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_screen)

        // Logo text color:
        val title: TextView  = findViewById(R.id.title)
        val first: String = "Fli"
        val second: String = "<font color='#D34D4C'>b</font>"
        title.text = Html.fromHtml(first + second, Html.FROM_HTML_MODE_COMPACT)
        initViews()
    }

    fun initViews(){
        // Endless Button Functions
        val buttonEndless : Button = findViewById(R.id.start_endless)
        buttonEndless.setOnClickListener{
            val intent = Intent(this, LoadGameActivity::class.java)
            intent.putExtra(GameActivity.INTENT_TIMERACE, false)
            startActivity(intent)
        }

        // timerace Button Functions
        val buttonTimeRace : Button = findViewById(R.id.start_timerace)
        buttonTimeRace.setOnClickListener{
            val intent = Intent(this, LoadGameActivity::class.java)
            intent.putExtra(GameActivity.INTENT_TIMERACE, true)
            startActivity(intent)
        }

        // highscore button
        val buttonHighscore : Button = findViewById(R.id.show_highscore)
        buttonHighscore.setOnClickListener{
            val intent = Intent(this, HighscoreActivity::class.java)
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
