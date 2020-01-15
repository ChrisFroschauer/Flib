package com.hawla.flib.views.gameover;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hawla.flib.R;
import com.hawla.flib.model.HighscoreEntry;
import com.hawla.flib.views.highscore.HighscoreActivity;
import com.hawla.flib.views.titlescreen.TitlescreenActivity;

import java.util.ArrayList;
import java.util.List;

import static com.hawla.flib.views.game.GameActivity.INTENT_SCORE;
import static com.hawla.flib.views.game.GameActivity.INTENT_SCORE_DEFAULT;
import static com.hawla.flib.views.game.GameActivity.INTENT_TIMERACE;
import static com.hawla.flib.views.game.GameActivity.INTENT_TIMERACE_DEFAULT;

public class GameOverActivity extends AppCompatActivity {

    public static final String HIGHSCORE = "HIGHSCORE";
    public static final String DEFAULT_SCORES =
            "COM-10,COM-20,COM-30," +
            "COM-40,COM-50,COM-60," +
            "COM-70,COM-80,COM-90," +
            "COM-100";
    public static final String TOP_10_TIMERACE = "TOP10_TIMERACE";
    public static final String TOP_10_ENDLESS = "TOP10_ENDLESS";
    public static final String SEPARATOR_ENTRIES = ",";
    public static final String SEPARATOR_NAME_SCORE = "-";
    private EditText shorthandName;
    private Button enterHighscoreButton;
    private TextView leftRow;
    private TextView rightRow;
    private TextView scoreValueTextView;
    SharedPreferences prefs;

    private boolean isTimerace;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // prefs:
        prefs = getSharedPreferences(HIGHSCORE, MODE_PRIVATE);

        // intent extras:
        isTimerace = getIntent().getBooleanExtra(INTENT_TIMERACE, INTENT_TIMERACE_DEFAULT); //TODO add to intent
        score = getIntent().getIntExtra(INTENT_SCORE, INTENT_SCORE_DEFAULT);

        initViews();
    }

    private void initViews(){
        scoreValueTextView = findViewById(R.id.score_value_view);
        scoreValueTextView.setText(String.valueOf(score));
        shorthandName = findViewById(R.id.enter_shorthand);
        enterHighscoreButton = findViewById(R.id.enter_highscore_button);

        // Set focus and keyboard to the editText:
        shorthandName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        // Button
        enterHighscoreButton.setOnClickListener(v -> {
            if (shorthandName.length() <= 0 || shorthandName.length()> 3){
                Log.i("highscoreinput", "input to short or to long");
            }else{
                Log.i("highscoreinput", "enter highscore");
                v.setEnabled(false);

                new Handler().post(() -> enterHighscore(shorthandName.getText().toString(), score, isTimerace));

                // Intent to go to Highscore Activity:
                imm.hideSoftInputFromWindow(shorthandName.getWindowToken(), 0);
                Intent intent = new Intent(this, HighscoreActivity.class);
                intent.putExtra(INTENT_TIMERACE, isTimerace);
                startActivity(intent);
            }
        });

        // showHighscore
        leftRow = findViewById(R.id.shorthands_text_view);
        rightRow = findViewById(R.id.values_text_view);
        // fill highscore
        new Handler().post(()->{
            fillHighscore(isTimerace);
        });

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, TitlescreenActivity.class);
        startActivity(intent);
    }

    private void fillHighscore(boolean isTimerace){
        String prefKey = isTimerace ? "TOP10_TIMERACE" : "TOP10_ENDLESS";
        String top10 = prefs.getString(prefKey, DEFAULT_SCORES);
        // Get List out of the string:
        List<HighscoreEntry> topList = new ArrayList<>();
        String[] top10Split = top10.split(SEPARATOR_ENTRIES); //TODO forbid , in edit text
        for(String entry : top10Split){
            if (!entry.isEmpty()) {
                String[] split = entry.split(SEPARATOR_NAME_SCORE); //TODO forbid - in edit text
                topList.add(new HighscoreEntry(split[0], Integer.parseInt(split[1])));
                Log.i("fillHighscore", "Insert into topList: " + split[0] + " " + split[1]);
            }
        }
        topList.sort((x,y) -> y.getScore() - x.getScore());

        // Fill in to the views:
        String names = "";
        String scores = "";
        for (int i = 0; i < topList.size(); i++){
            HighscoreEntry entry = topList.get(i);
            names += (i+1) + ". " + entry.getName() + "\n";
            scores += entry.getScore() + "\n";
        }
        leftRow.setText(names);
        rightRow.setText(scores);

    }

    private void enterHighscore(String name, int score, boolean isTimerace){
        String prefKey = isTimerace ? TOP_10_TIMERACE : TOP_10_ENDLESS;
        String top10 = prefs.getString(prefKey, DEFAULT_SCORES);

        // Get List out of the string:
        List<HighscoreEntry> topList = new ArrayList<>();
        String[] top10Split = top10.split(SEPARATOR_ENTRIES); //TODO forbid , in edit text
        for(String entry : top10Split){
            if (!entry.isEmpty()) {
                String[] split = entry.split(SEPARATOR_NAME_SCORE); //TODO forbid - in edit text
                topList.add(new HighscoreEntry(split[0], Integer.parseInt(split[1])));
            }
        }
        topList.add(new HighscoreEntry(name, score));
        topList.sort((x,y) -> y.getScore() - x.getScore());
        if (topList.size() == 11){
            topList.remove(10);
        }

        // Get List into the string:
        String value = "";
        for(HighscoreEntry entry : topList){
            if (value.isEmpty()){
                value += entry.getName() + SEPARATOR_NAME_SCORE + entry.getScore();
            }else{
                value += SEPARATOR_ENTRIES + entry.getName() + SEPARATOR_NAME_SCORE + entry.getScore();
            }
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(prefKey, value);
        editor.apply();
    }
}
