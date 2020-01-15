package com.hawla.flib.views.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hawla.flib.R;
import com.hawla.flib.viewmodel.GameViewModel;
import com.hawla.flib.views.gameover.GameOverActivity;

import java.util.ArrayList;
import java.util.List;

import static com.hawla.flib.utils.Util.fillIntegerTwoDigits;

public class GameActivity extends AppCompatActivity {

    public static final String INTENT_TIMERACE = "TIMERACE";
    public static final boolean INTENT_TIMERACE_DEFAULT = false;
    public static final String INTENT_SCORE = "SCORE";
    public static final int INTENT_SCORE_DEFAULT = 0;
    public static final String GAME_SIZE = "game_size";
    public static final String GAME_SIZE_DEFAULT = "3";
    public static final String DIALOG_TAG = "dialog_tag";
    public static final String GAME_TIME = "game_time";
    public static final String GAME_TIME_DEFAULT = "2:00";
    public static final int MILLIS_PER_SECOND = 1000;
    public static final String SEPARATOR_EMPTY = " ";
    public static final String SEPARATOR_NORMAL = ":";

    private int gameSize;
    private boolean isTimerace;
    private List<List<Button>> gameBoardButtons;
    private TextView turnsLeftTextView;
    private TextView heartsLeftTextView;
    private TextView levelTextView;
    private TextView timeTextView;
    private List<ImageView> heartsImages;
    private int counter;

    private GameViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());

        // TIMERACE
        isTimerace = getIntent().getBooleanExtra(INTENT_TIMERACE, INTENT_TIMERACE_DEFAULT);
        if (isTimerace){
            String timer = prefs.getString(GAME_TIME, GAME_TIME_DEFAULT);
            String[] splits = timer.split(":");
            Log.i("TIMERACE", splits[0] + " : " + splits[1]);
            counter = Integer.parseInt(splits[0])* 60 + Integer.parseInt(splits[1]);
        }

        // INIT GAME_SIZE:
        gameSize = Integer.parseInt(prefs.getString(GAME_SIZE, GAME_SIZE_DEFAULT));
        switch(gameSize){
            case 3:
                setContentView(R.layout.activity_game3x3);
                initGameBoardButtons3x3();
                break;
            case 4:
                setContentView(R.layout.activity_game4x4);
                initGameBoardButtons4x4();
                break;
            case 5:
                setContentView(R.layout.activity_game5x5);
                initGameBoardButtons5x5();
                break;
            case 6:
                setContentView(R.layout.activity_game6x6);
                initGameBoardButtons6x6();
                break;
            case 7:
                setContentView(R.layout.activity_game7x7);
                initGameBoardButtons7x7();
                break;
        }

        // HOLD REFERENCES OF VIEWS:
        initViews();

        // VIEWMODEL:
        model = ViewModelProviders.of(this).get(GameViewModel.class);
        model.getGameBoard().observe(this, gameBoard -> {
            // react to gameboard change:
            Log.i("gotGameBoard", "at 0 0 : " + gameBoard.get(0).get(0));
            for (int i = 0; i < gameSize; i++) {
                for(int j = 0; j < gameSize; j++){
                    updateButtonColor(gameBoardButtons.get(i).get(j), gameBoard.get(i).get(j));
                }
            }
        });
        model.getCurrentTurnsLeft().observe(this, turns -> {
            // react to turn change:
            Log.i("turn changed", " turns: " + turns);
            turnsLeftTextView.setText(turns + " " + getString(R.string.turns_left));
        });
        model.getCurrentHeartsLeft().observe(this, hearts -> {
            // react to heart change:
            Log.i("hearts changed", " hearts: " + hearts);
            switch(hearts){
                case 0:
                    heartsLeftTextView.setText(getString(R.string.no_hearts));
                    heartsImages.get(0).setAlpha(0.0f);
                    heartsImages.get(1).setAlpha(0.0f);
                    heartsImages.get(2).setAlpha(0.0f);
                case 1:
                    heartsLeftTextView.setText("");
                    heartsImages.get(0).setAlpha(1.0f);
                    heartsImages.get(1).setAlpha(0.0f);
                    heartsImages.get(2).setAlpha(0.0f);
                    break;
                case 2:
                    heartsLeftTextView.setText("");
                    heartsImages.get(0).setAlpha(1.0f);
                    heartsImages.get(1).setAlpha(1.0f);
                    heartsImages.get(2).setAlpha(0.0f);
                    break;
                case 3:
                    heartsLeftTextView.setText("");
                    heartsImages.get(0).setAlpha(1.0f);
                    heartsImages.get(1).setAlpha(1.0f);
                    heartsImages.get(2).setAlpha(1.0f);
                    break;
                default:
                    heartsLeftTextView.setText(hearts+"");
                    heartsImages.get(0).setAlpha(1.0f);
                    heartsImages.get(1).setAlpha(0.0f);
                    heartsImages.get(2).setAlpha(0.0f);
                    break;
            }


        });
        model.getCurrentLevel().observe(this, level -> {
            // react to level change:
            Log.i("levels changed", " level: " + level);
            levelTextView.setText(getString(R.string.current_level).toUpperCase() + " " + level);
        });
        model.getShowDialog().observe(this, dialogValue -> {
            // react to possible showing dialog:
            if (dialogValue.isVisible()){
                Log.i("dialog should be shown", "dialog message: " + getString(dialogValue.getMessageId()));
                FragmentManager fm = getSupportFragmentManager();
                CustomInfoDialog dialog = new CustomInfoDialog();
                if (dialogValue.getType() == CustomInfoDialog.DialogType.LEVEL_UP){
                    dialog.setDialogMainText(getString(dialogValue.getMessageId()).toUpperCase() + " " + dialogValue.getValue());
                }else {
                    dialog.setDialogMainText(getString(dialogValue.getMessageId()) );
                }
                if (dialogValue.hasSubMessage()){
                    dialog.setDialogSubText(getString(dialogValue.getSubMessageId()));
                }

                dialog.setType(dialogValue.getType());
                dialog.show(fm, DIALOG_TAG);
                dialog.setCancelable(false);
                // Animate progress bar:
                new Handler().post(() -> {
                    ProgressBarAnimation anim = new ProgressBarAnimation(dialog.getProgressBar(), 0, 100);
                    anim.setDuration(2000);
                    dialog.getProgressBar().startAnimation(anim);
                });

                // GameOver Intent?
                if (dialogValue.getType() == CustomInfoDialog.DialogType.GAME_OVER) {
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(this, GameOverActivity.class);
                        intent.putExtra(INTENT_SCORE, 100);
                        intent.putExtra(INTENT_TIMERACE, isTimerace);
                        startActivity(intent);
                    }, 2000);
                }else {
                    // close dialog after x sec:
                    new Handler().postDelayed(() -> {
                        dialog.dismiss();
                    }, 2000);
                }
            }
        });

    }

    private void updateButtonColor(Button button, boolean isFliped){
        if (isFliped){
            ViewCompat.setBackgroundTintList(button, ContextCompat.getColorStateList(getApplication(), R.color.fieldColorTrue));
        }else{
            ViewCompat.setBackgroundTintList(button, ContextCompat.getColorStateList(getApplication(), R.color.fieldColorFalse));
        }
    }


    private void initViews(){
        turnsLeftTextView = findViewById(R.id.turns_text_view);
        heartsLeftTextView = findViewById(R.id.hearts_text_view);
        levelTextView = findViewById(R.id.level_text_view);
        heartsImages = new ArrayList<>();
        heartsImages.add(findViewById(R.id.hearts_picture_1));
        heartsImages.add(findViewById(R.id.hearts_picture_2));
        heartsImages.add(findViewById(R.id.hearts_picture_3));

        timeTextView = findViewById(R.id.time_text_view);
        if (isTimerace) {
            new CountDownTimer((counter+1) * MILLIS_PER_SECOND, MILLIS_PER_SECOND) {
                @Override
                public void onTick(long millisUntilFinished) {
                    String timerString = (int) (counter / 60) + SEPARATOR_NORMAL + fillIntegerTwoDigits(counter % 60);
                    if (counter <= 10){
                        String start = "<font color='#D34D4C'>";
                        String end = "</font>";
                        timeTextView.setText( Html.fromHtml(start + timerString + end, Html.FROM_HTML_MODE_COMPACT));
                    }else {
                        timeTextView.setText(timerString);
                    }
                    //timeTextView.setText(String.valueOf((int)(counter/60)) + SEPARATOR_EMPTY + String.valueOf(counter%60));
                    counter--;
                }

                @Override
                public void onFinish(){
                    //TODO GAME OVER
                    Log.i("TIMER", "finished");
                    model.gameOver();
                }
            }.start();
        }
    }


    private void fillButtonList(List<Button> buttonList, int size) {
        if (gameBoardButtons == null || gameBoardButtons.size() != size){
            List<Button> row;
            Button button;
            gameBoardButtons = new ArrayList<>();
            for (int x = 0; x < gameSize; x++){
                row = new ArrayList<>();
                for (int y = 0; y < gameSize; y++){
                    button = buttonList.get(x*gameSize + y);
                    row.add(button);
                    Log.i("fillButtonList", " x: " + x + " y: " + y);
                    setOnClickListenerToButton(button, x, y);
                }
                gameBoardButtons.add(row);
            }
        }
    }

    private void setOnClickListenerToButton(Button button, int x, int y){
        button.setOnClickListener(v -> model.clickOnField(x, y));
    }


    private void initGameBoardButtons3x3(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button)findViewById(R.id.gamefield_button00));
        buttonList.add((Button)findViewById(R.id.gamefield_button01));
        buttonList.add((Button)findViewById(R.id.gamefield_button02));
        buttonList.add((Button)findViewById(R.id.gamefield_button10));
        buttonList.add((Button)findViewById(R.id.gamefield_button11));
        buttonList.add((Button)findViewById(R.id.gamefield_button12));
        buttonList.add((Button)findViewById(R.id.gamefield_button20));
        buttonList.add((Button)findViewById(R.id.gamefield_button21));
        buttonList.add((Button)findViewById(R.id.gamefield_button22));
        fillButtonList(buttonList, 3);
    }

    private void initGameBoardButtons4x4(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button)findViewById(R.id.gamefield_button00));
        buttonList.add((Button)findViewById(R.id.gamefield_button01));
        buttonList.add((Button)findViewById(R.id.gamefield_button02));
        buttonList.add((Button)findViewById(R.id.gamefield_button03));
        buttonList.add((Button)findViewById(R.id.gamefield_button10));
        buttonList.add((Button)findViewById(R.id.gamefield_button11));
        buttonList.add((Button)findViewById(R.id.gamefield_button12));
        buttonList.add((Button)findViewById(R.id.gamefield_button13));
        buttonList.add((Button)findViewById(R.id.gamefield_button20));
        buttonList.add((Button)findViewById(R.id.gamefield_button21));
        buttonList.add((Button)findViewById(R.id.gamefield_button22));
        buttonList.add((Button)findViewById(R.id.gamefield_button23));
        buttonList.add((Button)findViewById(R.id.gamefield_button30));
        buttonList.add((Button)findViewById(R.id.gamefield_button31));
        buttonList.add((Button)findViewById(R.id.gamefield_button32));
        buttonList.add((Button)findViewById(R.id.gamefield_button33));
        fillButtonList(buttonList, 4);
    }

    private void initGameBoardButtons5x5(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button)findViewById(R.id.gamefield_button00));
        buttonList.add((Button)findViewById(R.id.gamefield_button01));
        buttonList.add((Button)findViewById(R.id.gamefield_button02));
        buttonList.add((Button)findViewById(R.id.gamefield_button03));
        buttonList.add((Button)findViewById(R.id.gamefield_button04));
        buttonList.add((Button)findViewById(R.id.gamefield_button10));
        buttonList.add((Button)findViewById(R.id.gamefield_button11));
        buttonList.add((Button)findViewById(R.id.gamefield_button12));
        buttonList.add((Button)findViewById(R.id.gamefield_button13));
        buttonList.add((Button)findViewById(R.id.gamefield_button14));
        buttonList.add((Button)findViewById(R.id.gamefield_button20));
        buttonList.add((Button)findViewById(R.id.gamefield_button21));
        buttonList.add((Button)findViewById(R.id.gamefield_button22));
        buttonList.add((Button)findViewById(R.id.gamefield_button23));
        buttonList.add((Button)findViewById(R.id.gamefield_button24));
        buttonList.add((Button)findViewById(R.id.gamefield_button30));
        buttonList.add((Button)findViewById(R.id.gamefield_button31));
        buttonList.add((Button)findViewById(R.id.gamefield_button32));
        buttonList.add((Button)findViewById(R.id.gamefield_button33));
        buttonList.add((Button)findViewById(R.id.gamefield_button34));
        buttonList.add((Button)findViewById(R.id.gamefield_button40));
        buttonList.add((Button)findViewById(R.id.gamefield_button41));
        buttonList.add((Button)findViewById(R.id.gamefield_button42));
        buttonList.add((Button)findViewById(R.id.gamefield_button43));
        buttonList.add((Button)findViewById(R.id.gamefield_button44));
        fillButtonList(buttonList, 5);
    }

    private void initGameBoardButtons6x6(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button)findViewById(R.id.gamefield_button00));
        buttonList.add((Button)findViewById(R.id.gamefield_button01));
        buttonList.add((Button)findViewById(R.id.gamefield_button02));
        buttonList.add((Button)findViewById(R.id.gamefield_button03));
        buttonList.add((Button)findViewById(R.id.gamefield_button04));
        buttonList.add((Button)findViewById(R.id.gamefield_button05));
        buttonList.add((Button)findViewById(R.id.gamefield_button10));
        buttonList.add((Button)findViewById(R.id.gamefield_button11));
        buttonList.add((Button)findViewById(R.id.gamefield_button12));
        buttonList.add((Button)findViewById(R.id.gamefield_button13));
        buttonList.add((Button)findViewById(R.id.gamefield_button14));
        buttonList.add((Button)findViewById(R.id.gamefield_button15));
        buttonList.add((Button)findViewById(R.id.gamefield_button20));
        buttonList.add((Button)findViewById(R.id.gamefield_button21));
        buttonList.add((Button)findViewById(R.id.gamefield_button22));
        buttonList.add((Button)findViewById(R.id.gamefield_button23));
        buttonList.add((Button)findViewById(R.id.gamefield_button24));
        buttonList.add((Button)findViewById(R.id.gamefield_button25));
        buttonList.add((Button)findViewById(R.id.gamefield_button30));
        buttonList.add((Button)findViewById(R.id.gamefield_button31));
        buttonList.add((Button)findViewById(R.id.gamefield_button32));
        buttonList.add((Button)findViewById(R.id.gamefield_button33));
        buttonList.add((Button)findViewById(R.id.gamefield_button34));
        buttonList.add((Button)findViewById(R.id.gamefield_button35));
        buttonList.add((Button)findViewById(R.id.gamefield_button40));
        buttonList.add((Button)findViewById(R.id.gamefield_button41));
        buttonList.add((Button)findViewById(R.id.gamefield_button42));
        buttonList.add((Button)findViewById(R.id.gamefield_button43));
        buttonList.add((Button)findViewById(R.id.gamefield_button44));
        buttonList.add((Button)findViewById(R.id.gamefield_button45));
        buttonList.add((Button)findViewById(R.id.gamefield_button50));
        buttonList.add((Button)findViewById(R.id.gamefield_button51));
        buttonList.add((Button)findViewById(R.id.gamefield_button52));
        buttonList.add((Button)findViewById(R.id.gamefield_button53));
        buttonList.add((Button)findViewById(R.id.gamefield_button54));
        buttonList.add((Button)findViewById(R.id.gamefield_button55));
        fillButtonList(buttonList, 6);
    }

    private void initGameBoardButtons7x7(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add((Button)findViewById(R.id.gamefield_button00));
        buttonList.add((Button)findViewById(R.id.gamefield_button01));
        buttonList.add((Button)findViewById(R.id.gamefield_button02));
        buttonList.add((Button)findViewById(R.id.gamefield_button03));
        buttonList.add((Button)findViewById(R.id.gamefield_button04));
        buttonList.add((Button)findViewById(R.id.gamefield_button05));
        buttonList.add((Button)findViewById(R.id.gamefield_button06));
        buttonList.add((Button)findViewById(R.id.gamefield_button10));
        buttonList.add((Button)findViewById(R.id.gamefield_button11));
        buttonList.add((Button)findViewById(R.id.gamefield_button12));
        buttonList.add((Button)findViewById(R.id.gamefield_button13));
        buttonList.add((Button)findViewById(R.id.gamefield_button14));
        buttonList.add((Button)findViewById(R.id.gamefield_button15));
        buttonList.add((Button)findViewById(R.id.gamefield_button16));
        buttonList.add((Button)findViewById(R.id.gamefield_button20));
        buttonList.add((Button)findViewById(R.id.gamefield_button21));
        buttonList.add((Button)findViewById(R.id.gamefield_button22));
        buttonList.add((Button)findViewById(R.id.gamefield_button23));
        buttonList.add((Button)findViewById(R.id.gamefield_button24));
        buttonList.add((Button)findViewById(R.id.gamefield_button25));
        buttonList.add((Button)findViewById(R.id.gamefield_button26));
        buttonList.add((Button)findViewById(R.id.gamefield_button30));
        buttonList.add((Button)findViewById(R.id.gamefield_button31));
        buttonList.add((Button)findViewById(R.id.gamefield_button32));
        buttonList.add((Button)findViewById(R.id.gamefield_button33));
        buttonList.add((Button)findViewById(R.id.gamefield_button34));
        buttonList.add((Button)findViewById(R.id.gamefield_button35));
        buttonList.add((Button)findViewById(R.id.gamefield_button36));
        buttonList.add((Button)findViewById(R.id.gamefield_button40));
        buttonList.add((Button)findViewById(R.id.gamefield_button41));
        buttonList.add((Button)findViewById(R.id.gamefield_button42));
        buttonList.add((Button)findViewById(R.id.gamefield_button43));
        buttonList.add((Button)findViewById(R.id.gamefield_button44));
        buttonList.add((Button)findViewById(R.id.gamefield_button45));
        buttonList.add((Button)findViewById(R.id.gamefield_button46));
        buttonList.add((Button)findViewById(R.id.gamefield_button50));
        buttonList.add((Button)findViewById(R.id.gamefield_button51));
        buttonList.add((Button)findViewById(R.id.gamefield_button52));
        buttonList.add((Button)findViewById(R.id.gamefield_button53));
        buttonList.add((Button)findViewById(R.id.gamefield_button54));
        buttonList.add((Button)findViewById(R.id.gamefield_button55));
        buttonList.add((Button)findViewById(R.id.gamefield_button56));
        buttonList.add((Button)findViewById(R.id.gamefield_button60));
        buttonList.add((Button)findViewById(R.id.gamefield_button61));
        buttonList.add((Button)findViewById(R.id.gamefield_button62));
        buttonList.add((Button)findViewById(R.id.gamefield_button63));
        buttonList.add((Button)findViewById(R.id.gamefield_button64));
        buttonList.add((Button)findViewById(R.id.gamefield_button65));
        buttonList.add((Button)findViewById(R.id.gamefield_button66));
        fillButtonList(buttonList, 7);
    }

}
