package com.hawla.flib.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class SettingsViewModel extends AndroidViewModel {
    private MutableLiveData<List<Boolean>> pattern;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Boolean>> getPattern() {
        if (pattern == null) {
            pattern = new MutableLiveData<List<Boolean>>();
            loadSavedPattern();
        }
        return pattern;
    }

    private void loadSavedPattern() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("pattern", Context.MODE_PRIVATE);
        List<Boolean> patternList = new ArrayList<Boolean>();
        for (int i = 1; i <= 9; i++){
            patternList.add(sharedPreferences.getBoolean(Integer.toString(i),true));
        }
        pattern.setValue(patternList);
    }

    private void saveCurrentPattern() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences("pattern", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 1; i <= 9; i++){
            editor.putBoolean(Integer.toString(i), pattern.getValue().get(i-1));
        }
        editor.commit();
    }

    @Override
    protected void onCleared(){
        saveCurrentPattern();
    }

}
