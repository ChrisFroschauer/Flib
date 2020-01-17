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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import static com.hawla.flib.views.game.GameActivity.GAME_SIZE;
import static com.hawla.flib.views.game.GameActivity.GAME_SIZE_DEFAULT;
import static com.hawla.flib.views.settings.PatternPickPreference.PICKPATTERN_TAG;

public class GameViewModel extends AndroidViewModel {

    public static final String TURNS = "turns";
    public static final String TURNS_DEFAULT = "3";
    public static final String ARE_INCREASING = "are_increasing";
    public static final boolean ARE_INCREASING_DEFAULT = false;
    public static final String HEARTS = "hearts";
    public static final String HEARTS_DEFAULT = "3";
    private final int INCREASE_TURNS = 5;
    private final Random random = new Random();

    private MutableLiveData<List<List<Boolean>>> gameBoard;
    private List<List<Boolean>> currentStartBoard;

    // Variable Members:
    private MutableLiveData<Integer> currentTurnsLeft;
    private MutableLiveData<Integer> currentHeartsLeft;
    private MutableLiveData<Integer> currentLevel;
    private MutableLiveData<DialogValue> showDialog;
    private int currentScore;
    private int comboBonus = 0;
    private int patternCount;

    // from settings Settings:
    private int gameSize;
    private int totalTurns;
    private boolean areIncreasing;
    private int totalHearts;
    private List<Boolean> pattern;


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

    public void clickOnField(int x, int y){
        applyPatternOn(x, y);

        int turnsLeft = currentTurnsLeft.getValue();
        int heartsLeft = currentHeartsLeft.getValue();
        if (turnsLeft > 1){ //default case
            turnsLeft -= 1;
            currentTurnsLeft.setValue(turnsLeft);
            if(isSolved()){
                prepareNextLevel();
                return;
            }
        }else{
            // end round / lose heart / increase level
            currentTurnsLeft.setValue(0);
            if(isSolved()){
                prepareNextLevel();
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

    private void prepareNextLevel() {
        int tempNextLevel = currentLevel.getValue().intValue() + 1;
        if (areIncreasing && tempNextLevel % INCREASE_TURNS == 0 && totalTurns < gameSize * gameSize) {
            showDialog.setValue(new DialogValue(R.string.level_up,
                    currentLevel.getValue() + 1, CustomInfoDialog.DialogType.LEVEL_UP, R.string.are_increasing));
        } else {
            showDialog.setValue(new DialogValue(R.string.level_up,
                    currentLevel.getValue() + 1, CustomInfoDialog.DialogType.LEVEL_UP));
        }
        new Handler().postDelayed(() -> nextLevel(), 1000);
    }

    private void nextLevel() {
        // score:
        currentScore += calculateScoreForCurrLevel();
        Log.i("CURRENTSCORE", + currentScore +"");
        // combo:
        comboBonus++;
        // level up
        int nextLevel = currentLevel.getValue().intValue() + 1;
        currentLevel.setValue(nextLevel);
        if (areIncreasing && nextLevel % INCREASE_TURNS == 0 && totalTurns < gameSize * gameSize){
            totalTurns += 1;
        }
        // reset turnsLeft
        currentTurnsLeft.setValue(totalTurns);
        // make new Gameboard
        createGameBoard();
    }

    private int calculateScoreForCurrLevel() {
        // ####### gameSize ##############
        // it feels like it is this weird sequence from hardest to easiest:
        // 4x4 >= 3x3 > 5x5 > 6x6 > 7x7
        int scoreForCurrLevel = 1;
        if (gameSize == 3 || gameSize == 4){
            scoreForCurrLevel += 2;
        }else if(gameSize == 5 || gameSize == 6){
            scoreForCurrLevel += 1;
        }// no multiplicator for 7x7

        // ####### TotalTurns (+ areIncreasing) ###########
        // higher turns make it obviously way harder! so this has to be a high multiplicator
        // also areIncreasing is included since the totalTurns-Objectvariable gets increased
        // totalTurns == 1 means no points!
        if (totalTurns == 1){
            return 0;
        }else if (totalTurns == 2){
            return scoreForCurrLevel = 1;
        }
        scoreForCurrLevel += totalTurns*2;

        // ####### hearts ################
        // obiously more hearts gives a higher chance to proceed
        // but i don't think its that much an advantage
        // i propose: totalhearts: 5 -> 0, 4 total -> +1, ... 1 total -> +4
        scoreForCurrLevel += (5-totalHearts);

        // ####### heart comboBonus ###########
        // multiple rounds without loosing a heart -> get comboBonus
        scoreForCurrLevel += comboBonus/2;

        // ####### pattern ###############
        // obviously less set pattern-fields are kinda easier. E.g. 1 pattern field would be trivial.
        // But also a pattern with 6 can be easier for a human than a pattern with 5...
        // even though of a higher chance of collisions
        // 4 fields -> +0, 5 fields -> +1... 7 fields -> +2, ... 9 fields -> +2 (standard case)
        if (patternCount <= 1) {
            return 0;
        }else if (patternCount <= 4) {
            scoreForCurrLevel -= (4 - patternCount)*5; // sanction very low patternCounts harshly.
        }else if (patternCount <= 6){
            scoreForCurrLevel += 1;
        }else{
            scoreForCurrLevel += 2;
        }

        // End calc: Random, check for a sub 0 score
        scoreForCurrLevel += (2 - random.nextInt(4));
        if (scoreForCurrLevel <= 0) scoreForCurrLevel = 0;

        return scoreForCurrLevel;
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
        // reset comboBonus:
        comboBonus = 0;

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

    public void gameOver() {
        showDialog.setValue(new DialogValue(R.string.lose_game,
                currentScore, CustomInfoDialog.DialogType.GAME_OVER));
    }

    private void loadFromSettings() {
        // Init variables:
        currentTurnsLeft = new MutableLiveData<>();
        currentHeartsLeft = new MutableLiveData<>();
        showDialog = new MutableLiveData<>();
        showDialog.setValue(new DialogValue(0, 0, CustomInfoDialog.DialogType.INVISIBLE));
        currentLevel = new MutableLiveData<>();
        currentLevel.setValue(1);
        currentScore = 0;

        // Load board size:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String gameSize = prefs.getString(GAME_SIZE, GAME_SIZE_DEFAULT);
        Log.i("GameViewModel", gameSize + "x" + gameSize);
        this.gameSize = Integer.parseInt(gameSize);

        // Load total turns:
        String totalTurns = prefs.getString(TURNS, TURNS_DEFAULT);
        Log.i("GameViewModel", totalTurns + " turns");
        this.totalTurns = Integer.parseInt(totalTurns);
        if (this.totalTurns >= this.gameSize*this.gameSize){
            this.totalTurns = (this.gameSize*this.gameSize) - 1;
        }
        currentTurnsLeft.setValue(this.totalTurns);

        // Load increasing turns
        boolean areIncreasing = prefs.getBoolean(ARE_INCREASING, ARE_INCREASING_DEFAULT);
        Log.i("GameViewModel", " are Incresing: " + areIncreasing);
        this.areIncreasing = areIncreasing;

        // Load total hearts:
        String totalHearts = prefs.getString(HEARTS, HEARTS_DEFAULT);
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

        // Count pattern: (for score calculation)
        patternCount = 0;
        for(boolean flip: pattern){
            if (flip){
                patternCount++;
            }
        }
    }

    private void createGameBoard() {
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
        // Possible Values:
        Stack<int[]> openFields = new Stack<>();
        for (int i = 0; i < gameSize; i++){
            for (int j = 0; j < gameSize; j++){
                openFields.add(new int[]{i, j});
            }
        }
        // Shuffel:
        Collections.shuffle(openFields, random);
        int[] curr;
        for (int i = 0; i < totalTurns && i < gameSize*gameSize; i++){
            curr = openFields.pop();
            applyPatternOn(curr[0], curr[1]);
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
                        //Log.i("applyPatternOn 4", "x: " + (x) + " y: " + (y));
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
    }


}
