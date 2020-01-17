package com.hawla.flib.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static com.hawla.flib.views.settings.PatternPickPreference.PICKPATTERN_TAG;

public class TrainingViewModel extends AndroidViewModel {

    // from settings Settings:
    private int gameSize;
    private List<Boolean> pattern;

    // Live data
    private MutableLiveData<List<List<Boolean>>> gameBoard;
    private MutableLiveData<Boolean> historyEmpty;
    private MutableLiveData<Integer> countTurns;
    private int countTurnsInternal;
    // History
    Deque<int[]> history;

    public TrainingViewModel(@NonNull Application application) {
        super(application);
        history = new ArrayDeque<>();
        countTurnsInternal = 0;
        historyEmpty = new MutableLiveData<>();
        countTurns = new MutableLiveData<>();
        loadFromSettings();
    }

    public LiveData<List<List<Boolean>>> getGameBoard() {
        if (gameBoard == null) {
            gameBoard = new MutableLiveData<>();
            createGameBoard();
        }
        return gameBoard;
    }
    public LiveData<Boolean> getHistoryEmpty(){
        if (historyEmpty == null){
            historyEmpty = new MutableLiveData<>();
        }
        return historyEmpty;
    }
    public LiveData<Integer> getCountTurns(){
        if (countTurns == null){
            countTurns = new MutableLiveData<>();
        }
        return countTurns;
    }

    public void clickOnField(int x, int y){
        countTurnsInternal++;
        history.addLast(new int[]{x, y});
        if (history.size() > 100){
            history.pollFirst();
        }
        applyPatternOn(x, y);
        historyEmpty.setValue(false);
        countTurns.postValue(countTurnsInternal);
    }

    public void revertLastField(){
        int[] revertField = history.pollLast();
        if (revertField == null){
            historyEmpty.setValue(true);
        }else{
            countTurnsInternal--;
            applyPatternOn(revertField[0], revertField[1]);
            countTurns.postValue(countTurnsInternal);
        }
    }
    private void loadFromSettings() {
        // Load board size:
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String gameSize = prefs.getString("game_size", "4");
        Log.i("TrainingViewModel", gameSize + "x" + gameSize);
        this.gameSize = Integer.parseInt(gameSize);

        // Load pattern:
        SharedPreferences patternPrefs = getApplication().getSharedPreferences("PickPattern", Context.MODE_PRIVATE);
        List<Boolean> patternList = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            boolean curr = patternPrefs.getBoolean(PICKPATTERN_TAG +i,true);
            Log.i("TrainingViewModel", i+": " + patternPrefs.getBoolean(PICKPATTERN_TAG +i,true) );
            patternList.add(curr);
        }
        this.pattern = patternList;
    }

    private void createGameBoard() {
        List<List<Boolean>> board = new ArrayList<>();
        for (int i = 0; i < gameSize; i++){
            List<Boolean> row = new ArrayList<>();
            for (int j = 0; j < gameSize; j++) {
                row.add(true);
            }
            board.add(row);
        }
        gameBoard.setValue(board);
    }

    // this method changes the gameField without affecting turns, etc...
    private void applyPatternOn(int x, int y) {
        boolean result = false;
        Log.i("applyPatternOn", " x: " + x + " y: " + y);
        List<List<Boolean>> currBoard = gameBoard.getValue();
        boolean doFlip;
        int x_now;
        int y_now;
        for (int i = 0; i < pattern.size(); i++) {
            doFlip = pattern.get(i);
            if (doFlip) {
                switch (i) {
                    case 0:
                        //Log.i("applyPatternOn 0", "x: " + (x-1) + " y: " + (y-1));
                        x_now = x - 1;
                        y_now = y - 1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize) {
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            Log.i("apply pattern upper left", "x_now: " + x_now + " y_now: " + y_now +
                                    " gets set to " + !curr);
                            result = true;
                        }
                        break;
                    case 1:
                        //Log.i("applyPatternOn 1", "x: " + (x-1) + " y: " + (y));
                        x_now = x - 1;
                        y_now = y;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize) {
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 2:
                        //Log.i("applyPatternOn 2", "x: " + (x-1) + " y: " + (y+1));
                        x_now = x - 1;
                        y_now = y + 1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize) {
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 3:
                        //Log.i("applyPatternOn 3", "x: " + (x) + " y: " + (y-1));
                        x_now = x;
                        y_now = y - 1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize) {
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
                                && y_now >= 0 && y_now < gameSize) {
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 5:
                        //Log.i("applyPatternOn 5", "x: " + (x) + " y: " + (y+1));
                        x_now = x;
                        y_now = y + 1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize) {
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 6:
                        //Log.i("applyPatternOn 6" , "x: " + (x+1) + " y: " + (y-1));
                        x_now = x + 1;
                        y_now = y - 1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize) {
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 7:
                        //Log.i("applyPatternOn 7", "x: " + (x+1) + " y: " + (y));
                        x_now = x + 1;
                        y_now = y;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize) {
                            boolean curr = currBoard.get(x_now).get(y_now);
                            currBoard.get(x_now).set(y_now, !curr);
                            result = true;
                        }
                        break;
                    case 8:
                        //Log.i("applyPatternOn 8", "x: " + (x+1) + " y: " + (y+1));
                        x_now = x + 1;
                        y_now = y + 1;
                        if (x_now >= 0 && x_now < gameSize
                                && y_now >= 0 && y_now < gameSize) {
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
