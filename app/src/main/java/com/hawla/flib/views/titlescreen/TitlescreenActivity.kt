package com.hawla.flib.views.titlescreen

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.hawla.flib.R
import com.hawla.flib.views.game.GameActivity
import com.hawla.flib.views.game.LoadGameActivity
import com.hawla.flib.views.training.TrainingActivity
import com.hawla.flib.views.highscore.HighscoreActivity
import com.hawla.flib.views.settings.SettingsActivity
import com.hawla.flib.views.training.LoadTrainingActivity


class TitlescreenActivity : AppCompatActivity() {

    lateinit var colorStateListFalse: ColorStateList
    lateinit var colorStateListTrue: ColorStateList
    lateinit var buttonList: Array<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_screen)

        // Logo text color:
        /*val title: TextView  = findViewById(R.id.title)
        val first: String = "Fli"
        val second: String = "<font color='#D34D4C'>b</font>"
        title.text = Html.fromHtml(first + second, Html.FROM_HTML_MODE_COMPACT)*/
        initViews()
    }

    fun initViews(){
        // Endless Button Functions
        val buttonEndless : ImageButton = findViewById(R.id.start_endless)
        buttonEndless.setOnClickListener{
            val intent = Intent(this, LoadGameActivity::class.java)
            intent.putExtra(GameActivity.INTENT_TIMERACE, false)
            startActivity(intent)
        }

        // timerace Button Functions
        val buttonTimeRace : ImageButton = findViewById(R.id.start_timerace)
        buttonTimeRace.setOnClickListener{
            val intent = Intent(this, LoadGameActivity::class.java)
            intent.putExtra(GameActivity.INTENT_TIMERACE, true)
            startActivity(intent)
        }

        // highscore button
        val buttonHighscore : ImageButton = findViewById(R.id.show_highscore)
        buttonHighscore.setOnClickListener{
            val intent = Intent(this, HighscoreActivity::class.java)
            startActivity(intent)
        }

        // Setting Button Functions
        val buttonOptions : ImageButton = findViewById(R.id.show_options)
        buttonOptions.setOnClickListener{
            val intentShowSettings = Intent(this, SettingsActivity::class.java)
            startActivity(intentShowSettings)
        }

        // Info button
        val buttonInfo : Button = findViewById(R.id.info_button)
        buttonInfo.setOnClickListener{
            val dialog = GameExplanationDialog()
            dialog.show(supportFragmentManager, GameActivity.DIALOG_TAG)
            dialog.isCancelable = true
        }

        // Setting fun buttons:#
        colorStateListFalse = ContextCompat.getColorStateList(application, R.color.fieldColorFalse)!!
        colorStateListTrue = ContextCompat.getColorStateList(application, R.color.colorAccent)!!

        buttonList = arrayOf(findViewById<Button>(R.id.playfull_button00),
            findViewById<Button>(R.id.playfull_button01),
            findViewById<Button>(R.id.playfull_button11),
            findViewById<Button>(R.id.playfull_button12),
            findViewById<Button>(R.id.playfull_button20))
        for (button: Button in buttonList){
            ViewCompat.setBackgroundTintList(button, colorStateListFalse)
            button.setOnClickListener{
                if ( button.backgroundTintList!!.equals(colorStateListFalse) ){
                    ViewCompat.setBackgroundTintList(button, colorStateListTrue)
                    checkForTrainingsCode()
                }else{
                    ViewCompat.setBackgroundTintList(button, colorStateListFalse)
                    checkForTrainingsCode()
                }
            }
        }

    }

    fun checkForTrainingsCode(){
        var showTraining = true
        buttonList.forEach {
            showTraining = showTraining && (it.backgroundTintList!!.equals(colorStateListTrue))
        }
        if (showTraining){
            val intent = Intent(this, LoadTrainingActivity::class.java)
            startActivity(intent)
        }
    }

}
