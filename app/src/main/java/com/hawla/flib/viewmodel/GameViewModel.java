package com.hawla.flib.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.PreferenceManager;

import com.hawla.flib.R;
import com.hawla.flib.model.DialogValue;
import com.hawla.flib.views.game.CustomInfoDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hawla.flib.views.settings.PatternPickPreference.PICKPATTERN_TAG;

public class GameViewModel extends AndroidViewModel {

    private final int INCREASE_TURNS = 5;

    private MutableLiveData<List<List<Boolean>>> gameBoard;
    private List<List<Boolean>> currentStartBoard;
    //private MutableLiveData<Boolean> boardIsVisible;

    // Variable Members:
    private MutableLiveData<Integer> currentTurnsLeft;
    private MutableLiveData<Integer> currentHeartsLeft;
    private MutableLiveData<Integer> currentLevel;
    private MutableLiveData<Integer> currentScore;
    private MutableLiveData<DialogValue> showDialog;

    // Fixed Settings:
    private int gameSize;
    private int totalTurns;
    private boolean areIncreasing;
    private int totalHearts;
    private List<Boolean> pattern;
    private boolean isTimerace;


    public GameViewModel(@NonNull Application application) {
        super(application);
        loadFromSettings();
    }

    public LiveData<List<List<Boolean>>> getGameBoard() {
        if (gameBoard == null) {
            gameBoard = new MutableLiveData<>();
            createGameBoard();
        }
        return gameBoard;
    }

    public LiveData<Integer> getCurrentTurnsLeft() {
        return currentTurnsLeft;
    }
    public LiveData<Integer> getCurrentHeartsLeft() {
        return currentHeartsLeft;
    }
    public LiveData<DialogValue> getShowDialog() {
        return showDialog;
    }
    public LiveData<Integer> getCurrentLevel() {
        return currentLevel;
    }

    public void setTimerace(boolean timerace){
        this.isTimerace = timerace;
    }

    public void clickOnField(int x, int y){
        applyPatternOn(x, y);

        int turnsLeft = currentTurnsLeft.getValue();
        int heartsLeft = currentHeartsLeft.getValue();
        if (turnsLeft > 1){ //default case
            turnsLeft -= 1;
            currentTurnsLeft.setValue(turnsLeft);
            if(isSolved()){
                int tempNextLevel = currentLevel.getValue().intValue() + 1;
                if (tempNextLevel % INCREASE_TURNS == 0 && totalTurns < gameSize*gameSize){
                    showDialog.setValue(new DialogValue(R.string.level_up,
                            currentLevel.getValue() + 1, CustomInfoDialog.DialogType.LEVEL_UP, R.string.are_increasing));
                }else{
                    showDialog.setValue(new DialogValue(R.string.level_up,
                            currentLevel.getValue() + 1, CustomInfoDialog.DialogType.LEVEL_UP));
                }
                new Handler().postDelayed(()-> nextLevel(), 1000);
                return;
            }
        }else {
            // end round / lose heart / increase level
            currentTurnsLeft.setValue(0);
            if(isSolved()){
                int tempNextLevel = currentLevel.getValue().intValue() + 1;
                if (tempNextLevel % INCREASE_TURNS == 0 && totalTurns < gameSize*gameSize){
                    showDialog.setValue(new DialogValue(R.string.level_up,
                            currentLevel.getValue() + 1, CustomInfoDialog.DialogType.LEVEL_UP, R.string.are_increasing));
                }else{
                    showDialog.setValue(new DialogValue(R.string.level_up,
                            currentLevel.getValue() + 1, CustomInfoDialog.DialogType.LEVEL_UP));
                }
                new Handler().postDelayed(()-> nextLevel(), 1000);
                return;
            }
            // Losing a heart:
            if (heartsLeft > 1){
                heartsLeft -= 1;
                currentHeartsLeft.setValue(heartsLeft);
                // show dialog: Losing a heart
                showDialog.setValue(new DialogValue(R.string.lose_heart,
                        currentHeartsLeft.getValue(), CustomInfoDialog.DialogType.LOSE_HEART));
                // dialog gets removed in activity
                new Handler().postDelayed(()->{
                    restartLevel();
                }, 1000);
                return;
            }else {
                currentHeartsLeft.setValue(0);
                gameOver();
            }
        }
    }

    private boolean isSolved() {
        List<List<Boolean>> currGameBoard = gameBoard.getValue();
        boolean isSolved = true;
        for (List<Boolean> row : currGameBoard){
            for (boolean field : row){
                isSolved &= field;
            }
        }
        return isSolved;
    }

    private void restartLevel() {
        // Copy currentStartBoard for restarting:
        List<List<Boolean>> temp = new ArrayList<>();
        for (int i = 0; i < gameSize; i++){
            temp.add(new ArrayList<>());
            for (int j = 0; j < gameSize; j++){
                temp.get(i).add(currentStartBoard.get(i).get(j));
            }
        }
        gameBoard.setValue(temp);
        currentTurnsLeft.setValue(totalTurns);
    }

    private void nextLevel() {
        int nextLevel = currentLevel.getValue().intValue() + 1;
        currentLevel.setValue(nextLevel);
        if (nextLevel % INCREASE_TURNS == 0 && totalTurns < gameSize*gameSize){
            totalTurns += 1;
        }
        currentTurnsLeft.setValue(totalTurns);
        createGameBoard();
    }

    public void gameOver() {
        showDialog.setValue(new DialogValue(R.string.lose_game,
                0, CustomInfoDialog.DialogType.GAME_OVER));
        // TODO: Intent to gameover activity
    }

    private void loadFromSettings() {
        // Init variables:
        currentTurnsLeft = new MutableLiveData<>();
        currentHeartsLeft = new MutableLiveData<>();
        showDialog = new MutableLiveData<>();
        showDialog.setValue(new DialogValue(0, 0, CustomInfoDialog.DialogType.INVISIBLE));
        currentLevel = new MutableLiveData<>();
        currentLevel.setValue(1);
        currentScore = new MutableLiveData<>();
        currentScore.setValue(0);

        // Load board size:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String gameSize = prefs.getString("game_size", "4");
        Log.i("GameViewModel", gameSize + "x" + gameSize);
        this.gameSize = Integer.parseInt(gameSize);

        // Load total turns:
        String totalTurns = prefs.getString("turns", "3");
        Log.i("GameViewModel", totalTurns + " turns");
        this.totalTurns = Integer.parseInt(totalTurns);
        if (this.totalTurns > 9){ //TODO: Total Turns with >= gameSize*gameSize is dumb isn't it?
            this.totalTurns = 9;
        }
        currentTurnsLeft.setValue(this.totalTurns);

        // Load increasing turns
        boolean areIncreasing = prefs.getBoolean("are_increasing", false);
        Log.i("GameViewModel", " are Incresing: " + areIncreasing);
        this.areIncreasing = areIncreasing;

        // Load total hearts:
        String totalHearts = prefs.getString("hearts", "3");
        Log.i("GameViewModel", totalTurns + " turns");
        this.totalHearts = Integer.parseInt(totalHearts);
        currentHeartsLeft.setValue(this.totalHearts);

        // Load pattern:
        SharedPreferences patternPrefs = getApplication().getSharedPreferences("PickPattern", Context.MODE_PRIVATE);
        List<Boolean> patternList = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            boolean curr = patternPrefs.getBoolean(PICKPATTERN_TAG +i,true);
            Log.i("GameViewModel", i+": " + patternPrefs.getBoolean(PICKPATTERN_TAG +i,true) );
            patternList.add(curr);
        }
        this.pattern = patternList;
    }

    private void createGameBoard() {
        // board is visible
        /*boardIsVisible = new MutableLiveData<>();
        boardIsVisible.setValue(false);*/

        // Init Arrays:
        List<List<Boolean>> board = new ArrayList<>();
        for (int i = 0; i < gameSize; i++){
            List<Boolean> row = new ArrayList<>();
            for (int j = 0; j < gameSize; j++) {
                row.add(true);
            }
            board.add(row);
        }
        gameBoard.setValue(board);

        // Random init pattern:
        // turn times, with pattern, on random location, avoid double tapping
        int curr_x;
        int curr_y;
        // Possible Values:
        boolean[][] notAvailable = new boolean[gameSize][gameSize]; // is init with false
        for (int i = 0; i < totalTurns; i++){
            Random rando = new Random();
            curr_x = rando.nextInt(gameSize);
            curr_y = rando.nextInt(gameSize);
            // avoid double tapping the same field:
            while(notAvailable[curr_x][curr_y] == true){
                curr_x = rando.nextInt(gameSize);
                curr_y = rando.nextInt(gameSize);
            }
            applyPatternOn(curr_x, curr_y);
            notAvailable[curr_x][curr_y] = true;
        }

        // Copy currentStartBoard for restarting:
        List<List<Boolean>> temp =  gameBoard.getValue();
        currentStartBoard = new ArrayList<>();
        for (int i = 0; i < gameSize; i++){
            currentStartBoard.add(new ArrayList<>());
            for (int j = 0; j < gameSize; j++){
                currentStartBoard.get(i).add(temp.get(i).get(j));
            }
        }

        /*boardIsVisible.setValue(true);*/
    }

    // this method changes the gameField without affecting turns, etc...
    private void applyPatternOn(int x, int y){
        boolean result = false;
        Log.i("applyPatternOn", " x: " + x + " y: " +y);
        List<List<Boolean>> currBoard = gameBoard.getValue();
        boolean doFlip;
        int x_now;
        int y_now;
        for (int i = 0; i < pattern.size(); i++){
            doFlip = pattern.get(i);
            if (doFlip){
                switch(i){
                    case 0:
                        //Log.i("applyPatternOn 0", "x: " + (x-1) + " y: " + (y-1));
                        x_now = x-1;
                        y_now = y-1;
                        if (x_now >= 0 && x_now < gameSize
                        && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            Log.i("apply pattern upper left", "x_now: " + x_now + " y_now: " + y_now +
                                    " gets set to " + !curr);
                            result = true;
                        }
                        break;
                    case 1:
                        //Log.i("applyPatternOn 1", "x: " + (x-1) + " y: " + (y));
                        x_now = x-1;
                        y_now = y;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 2:
                        //Log.i("applyPatternOn 2", "x: " + (x-1) + " y: " + (y+1));
                        x_now = x-1;
                        y_now = y+1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 3:
                        //Log.i("applyPatternOn 3", "x: " + (x) + " y: " + (y-1));
                        x_now = x;
                        y_now = y-1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 4:
                        Log.i("applyPatternOn 4", "x: " + (x) + " y: " + (y));
                        x_now = x;
                        y_now = y;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 5:
                        //Log.i("applyPatternOn 5", "x: " + (x) + " y: " + (y+1));
                        x_now = x;
                        y_now = y+1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 6:
                        //Log.i("applyPatternOn 6" , "x: " + (x+1) + " y: " + (y-1));
                        x_now = x+1;
                        y_now = y-1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 7:
                        //Log.i("applyPatternOn 7", "x: " + (x+1) + " y: " + (y));
                        x_now = x+1;
                        y_now = y;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 8:
                        //Log.i("applyPatternOn 8", "x: " + (x+1) + " y: " + (y+1));
                        x_now = x+1;
                        y_now = y+1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize){
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                }

            }
        }
        gameBoard.setValue(currBoard);
        //return result;
    }


}
