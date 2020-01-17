package com.hawla.flib.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;


import org.junit.Before;
import org.junit.Test;

import static com.hawla.flib.views.gameover.GameOverActivity.DEFAULT_SCORES;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HighscoreViewModelTest {

    @Mock
    private Application application;
    @Mock
    private SharedPreferences prefs;

    @Before
    public void init(){
        application = mock(Application.class);
        prefs = mock(SharedPreferences.class);
        when(application.getSharedPreferences(any(), anyInt())).thenReturn(prefs);
        when(prefs.getString(any(), eq(DEFAULT_SCORES))).thenReturn(DEFAULT_SCORES);
        when(prefs.getString(any(), eq(""))).thenReturn("");
        when(prefs.getInt(any(), eq(0))).thenReturn(0);
    }

    @Test
    public void test_Constructor(){
        boolean isTimerace = true;
        HighscoreViewModel highscoreViewModel = new HighscoreViewModel(application);
        LiveData<String[]> observed = highscoreViewModel.getText();
        LiveData<Boolean> observedIsTimerace = highscoreViewModel.getIsTimerace();
        /* highscoreViewModel.setIndex(isTimerace);
        observedIsTimerace.observe((LifecycleOwner) this, isTR ->{
            assertTrue(isTR);
        } );*/
        // TODO testing liveData how?
    }
}
