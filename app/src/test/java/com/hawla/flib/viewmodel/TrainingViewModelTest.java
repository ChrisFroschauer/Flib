package com.hawla.flib.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import org.junit.Before;
import org.mockito.Mock;

import static com.hawla.flib.views.gameover.GameOverActivity.DEFAULT_SCORES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrainingViewModelTest {

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

    // TODO Testing LiveData how?
}
