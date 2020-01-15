package com.hawla.flib.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hawla.flib.model.HighscoreEntry;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.hawla.flib.views.gameover.GameOverActivity.DEFAULT_SCORES;
import static com.hawla.flib.views.gameover.GameOverActivity.HIGHSCORE;
import static com.hawla.flib.views.gameover.GameOverActivity.SEPARATOR_ENTRIES;
import static com.hawla.flib.views.gameover.GameOverActivity.SEPARATOR_NAME_SCORE;

public class HighscoreViewModel extends AndroidViewModel {

    // prefs:
    private SharedPreferences prefs;
    private MutableLiveData<Boolean> isTimerace = new MutableLiveData<>();

    private LiveData<String[]> mText = Transformations.map(isTimerace, timerace -> {
        String prefKey = timerace ? "TOP10_TIMERACE" : "TOP10_ENDLESS";
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

        // Fill in to view digestable strings
        String names = "";
        String scores = "";
        for (int i = 0; i < topList.size(); i++){
            HighscoreEntry entry = topList.get(i);
            names += (i+1) + ". " + entry.getName() + "\n";
            scores += entry.getScore() + "\n";
        }
        String[] arr = new String[2];
        arr[0] = names;
        arr[1] = scores;
        return arr;
    });

    public HighscoreViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences(HIGHSCORE, MODE_PRIVATE);
    }

    public void setIndex(boolean data) {
        isTimerace.setValue(data);
    }

    public LiveData<String[]> getText() {
        return mText;
    }
}