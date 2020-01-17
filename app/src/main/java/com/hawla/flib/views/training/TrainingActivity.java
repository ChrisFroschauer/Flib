package com.hawla.flib.views.training;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.hawla.flib.R;
import com.hawla.flib.viewmodel.TrainingViewModel;
import com.hawla.flib.views.game.GameActivity;
import com.hawla.flib.views.titlescreen.GameExplanationDialog;

import java.util.ArrayList;
import java.util.List;

import static com.hawla.flib.views.game.GameActivity.GAME_SIZE;
import static com.hawla.flib.views.game.GameActivity.GAME_SIZE_DEFAULT;

public class TrainingActivity extends AppCompatActivity {

    private TrainingViewModel model;
    private int gameSize;
    private int currentColor = R.color.fieldColorTrue;

    // VIEWS:
    private List<List<Button>> gameBoardButtons;
    private Button buttonInfo;
    private Button buttonUndo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());

        // INIT GAME_SIZE:
        gameSize = Integer.parseInt(prefs.getString(GAME_SIZE, GAME_SIZE_DEFAULT));
        switch(gameSize){
            case 3:
                setContentView(R.layout.activity_training3x3);
                initGameBoardButtons3x3();
                break;
            case 4:
                setContentView(R.layout.activity_training4x4);
                initGameBoardButtons4x4();
                break;
            case 5:
                setContentView(R.layout.activity_training5x5);
                initGameBoardButtons5x5();
                break;
            case 6:
                setContentView(R.layout.activity_training6x6);
                initGameBoardButtons6x6();
                break;
            case 7:
                setContentView(R.layout.activity_training7x7);
                initGameBoardButtons7x7();
                break;
        }

        //  :
        initViews();

        // VIEWMODEL:
        model = ViewModelProviders.of(this).get(TrainingViewModel.class);
        model.getGameBoard().observe(this, gameBoard -> {
            for (int i = 0; i < gameSize; i++) {
                for(int j = 0; j < gameSize; j++){
                    updateButtonColor(gameBoardButtons.get(i).get(j), gameBoard.get(i).get(j));
                }
            }
        });
        model.getHistoryEmpty().observe(this, historyEmpty -> {
            if (historyEmpty){
                buttonUndo.setClickable(false);
            }else{
                buttonUndo.setClickable(true);
            }
        });
        model.getCountTurns().observe(this, countTurns -> {
            Log.i("COUNTTURNS", "" + countTurns);
            if ((countTurns/100)%2 != 0) {
                currentColor = R.color.colorAccent;
            }else{
                currentColor = R.color.fieldColorTrue;
            }
        });
    }

    private void updateButtonColor(Button button, boolean isFliped){
        if (isFliped){
            ViewCompat.setBackgroundTintList(button, ContextCompat.getColorStateList(getApplication(), currentColor));
        }else{
            ViewCompat.setBackgroundTintList(button, ContextCompat.getColorStateList(getApplication(), R.color.fieldColorFalse));
        }
    }

    private void initViews(){
        // Info button
        buttonInfo = findViewById(R.id.info_button_training);
        buttonInfo.setOnClickListener((view)->{
            GameExplanationDialog dialog = new GameExplanationDialog();
            dialog.show(getSupportFragmentManager(), GameActivity.DIALOG_TAG);
            dialog.setCancelable(true);
        });

        // Undo button
        buttonUndo = findViewById(R.id.button_undo);
        buttonUndo.setOnClickListener((view)->{
            model.revertLastField();
        });
    }

    private void setOnClickListenerToButton(Button button, int x, int y){
        button.setOnClickListener(v -> model.clickOnField(x, y));
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

    private void initGameBoardButtons3x3(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add(findViewById(R.id.gamefield_button00));
        buttonList.add(findViewById(R.id.gamefield_button01));
        buttonList.add(findViewById(R.id.gamefield_button02));
        buttonList.add(findViewById(R.id.gamefield_button10));
        buttonList.add(findViewById(R.id.gamefield_button11));
        buttonList.add(findViewById(R.id.gamefield_button12));
        buttonList.add(findViewById(R.id.gamefield_button20));
        buttonList.add(findViewById(R.id.gamefield_button21));
        buttonList.add(findViewById(R.id.gamefield_button22));
        fillButtonList(buttonList, 3);
    }

    private void initGameBoardButtons4x4(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add(findViewById(R.id.gamefield_button00));
        buttonList.add(findViewById(R.id.gamefield_button01));
        buttonList.add(findViewById(R.id.gamefield_button02));
        buttonList.add(findViewById(R.id.gamefield_button03));
        buttonList.add(findViewById(R.id.gamefield_button10));
        buttonList.add(findViewById(R.id.gamefield_button11));
        buttonList.add(findViewById(R.id.gamefield_button12));
        buttonList.add(findViewById(R.id.gamefield_button13));
        buttonList.add(findViewById(R.id.gamefield_button20));
        buttonList.add(findViewById(R.id.gamefield_button21));
        buttonList.add(findViewById(R.id.gamefield_button22));
        buttonList.add(findViewById(R.id.gamefield_button23));
        buttonList.add(findViewById(R.id.gamefield_button30));
        buttonList.add(findViewById(R.id.gamefield_button31));
        buttonList.add(findViewById(R.id.gamefield_button32));
        buttonList.add(findViewById(R.id.gamefield_button33));
        fillButtonList(buttonList, 4);
    }

    private void initGameBoardButtons5x5(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add(findViewById(R.id.gamefield_button00));
        buttonList.add(findViewById(R.id.gamefield_button01));
        buttonList.add(findViewById(R.id.gamefield_button02));
        buttonList.add(findViewById(R.id.gamefield_button03));
        buttonList.add(findViewById(R.id.gamefield_button04));
        buttonList.add(findViewById(R.id.gamefield_button10));
        buttonList.add(findViewById(R.id.gamefield_button11));
        buttonList.add(findViewById(R.id.gamefield_button12));
        buttonList.add(findViewById(R.id.gamefield_button13));
        buttonList.add(findViewById(R.id.gamefield_button14));
        buttonList.add(findViewById(R.id.gamefield_button20));
        buttonList.add(findViewById(R.id.gamefield_button21));
        buttonList.add(findViewById(R.id.gamefield_button22));
        buttonList.add(findViewById(R.id.gamefield_button23));
        buttonList.add(findViewById(R.id.gamefield_button24));
        buttonList.add(findViewById(R.id.gamefield_button30));
        buttonList.add(findViewById(R.id.gamefield_button31));
        buttonList.add(findViewById(R.id.gamefield_button32));
        buttonList.add(findViewById(R.id.gamefield_button33));
        buttonList.add(findViewById(R.id.gamefield_button34));
        buttonList.add(findViewById(R.id.gamefield_button40));
        buttonList.add(findViewById(R.id.gamefield_button41));
        buttonList.add(findViewById(R.id.gamefield_button42));
        buttonList.add(findViewById(R.id.gamefield_button43));
        buttonList.add(findViewById(R.id.gamefield_button44));
        fillButtonList(buttonList, 5);
    }

    private void initGameBoardButtons6x6(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add(findViewById(R.id.gamefield_button00));
        buttonList.add(findViewById(R.id.gamefield_button01));
        buttonList.add(findViewById(R.id.gamefield_button02));
        buttonList.add(findViewById(R.id.gamefield_button03));
        buttonList.add(findViewById(R.id.gamefield_button04));
        buttonList.add(findViewById(R.id.gamefield_button05));
        buttonList.add(findViewById(R.id.gamefield_button10));
        buttonList.add(findViewById(R.id.gamefield_button11));
        buttonList.add(findViewById(R.id.gamefield_button12));
        buttonList.add(findViewById(R.id.gamefield_button13));
        buttonList.add(findViewById(R.id.gamefield_button14));
        buttonList.add(findViewById(R.id.gamefield_button15));
        buttonList.add(findViewById(R.id.gamefield_button20));
        buttonList.add(findViewById(R.id.gamefield_button21));
        buttonList.add(findViewById(R.id.gamefield_button22));
        buttonList.add(findViewById(R.id.gamefield_button23));
        buttonList.add(findViewById(R.id.gamefield_button24));
        buttonList.add(findViewById(R.id.gamefield_button25));
        buttonList.add(findViewById(R.id.gamefield_button30));
        buttonList.add(findViewById(R.id.gamefield_button31));
        buttonList.add(findViewById(R.id.gamefield_button32));
        buttonList.add(findViewById(R.id.gamefield_button33));
        buttonList.add(findViewById(R.id.gamefield_button34));
        buttonList.add(findViewById(R.id.gamefield_button35));
        buttonList.add(findViewById(R.id.gamefield_button40));
        buttonList.add(findViewById(R.id.gamefield_button41));
        buttonList.add(findViewById(R.id.gamefield_button42));
        buttonList.add(findViewById(R.id.gamefield_button43));
        buttonList.add(findViewById(R.id.gamefield_button44));
        buttonList.add(findViewById(R.id.gamefield_button45));
        buttonList.add(findViewById(R.id.gamefield_button50));
        buttonList.add(findViewById(R.id.gamefield_button51));
        buttonList.add(findViewById(R.id.gamefield_button52));
        buttonList.add(findViewById(R.id.gamefield_button53));
        buttonList.add(findViewById(R.id.gamefield_button54));
        buttonList.add(findViewById(R.id.gamefield_button55));
        fillButtonList(buttonList, 6);
    }

    private void initGameBoardButtons7x7(){
        List<Button> buttonList = new ArrayList<>();
        buttonList.add(findViewById(R.id.gamefield_button00));
        buttonList.add(findViewById(R.id.gamefield_button01));
        buttonList.add(findViewById(R.id.gamefield_button02));
        buttonList.add(findViewById(R.id.gamefield_button03));
        buttonList.add(findViewById(R.id.gamefield_button04));
        buttonList.add(findViewById(R.id.gamefield_button05));
        buttonList.add(findViewById(R.id.gamefield_button06));
        buttonList.add(findViewById(R.id.gamefield_button10));
        buttonList.add(findViewById(R.id.gamefield_button11));
        buttonList.add(findViewById(R.id.gamefield_button12));
        buttonList.add(findViewById(R.id.gamefield_button13));
        buttonList.add(findViewById(R.id.gamefield_button14));
        buttonList.add(findViewById(R.id.gamefield_button15));
        buttonList.add(findViewById(R.id.gamefield_button16));
        buttonList.add(findViewById(R.id.gamefield_button20));
        buttonList.add(findViewById(R.id.gamefield_button21));
        buttonList.add(findViewById(R.id.gamefield_button22));
        buttonList.add(findViewById(R.id.gamefield_button23));
        buttonList.add(findViewById(R.id.gamefield_button24));
        buttonList.add(findViewById(R.id.gamefield_button25));
        buttonList.add(findViewById(R.id.gamefield_button26));
        buttonList.add(findViewById(R.id.gamefield_button30));
        buttonList.add(findViewById(R.id.gamefield_button31));
        buttonList.add(findViewById(R.id.gamefield_button32));
        buttonList.add(findViewById(R.id.gamefield_button33));
        buttonList.add(findViewById(R.id.gamefield_button34));
        buttonList.add(findViewById(R.id.gamefield_button35));
        buttonList.add(findViewById(R.id.gamefield_button36));
        buttonList.add(findViewById(R.id.gamefield_button40));
        buttonList.add(findViewById(R.id.gamefield_button41));
        buttonList.add(findViewById(R.id.gamefield_button42));
        buttonList.add(findViewById(R.id.gamefield_button43));
        buttonList.add(findViewById(R.id.gamefield_button44));
        buttonList.add(findViewById(R.id.gamefield_button45));
        buttonList.add(findViewById(R.id.gamefield_button46));
        buttonList.add(findViewById(R.id.gamefield_button50));
        buttonList.add(findViewById(R.id.gamefield_button51));
        buttonList.add(findViewById(R.id.gamefield_button52));
        buttonList.add(findViewById(R.id.gamefield_button53));
        buttonList.add(findViewById(R.id.gamefield_button54));
        buttonList.add(findViewById(R.id.gamefield_button55));
        buttonList.add(findViewById(R.id.gamefield_button56));
        buttonList.add(findViewById(R.id.gamefield_button60));
        buttonList.add(findViewById(R.id.gamefield_button61));
        buttonList.add(findViewById(R.id.gamefield_button62));
        buttonList.add(findViewById(R.id.gamefield_button63));
        buttonList.add(findViewById(R.id.gamefield_button64));
        buttonList.add(findViewById(R.id.gamefield_button65));
        buttonList.add(findViewById(R.id.gamefield_button66));
        fillButtonList(buttonList, 7);
    }

}
