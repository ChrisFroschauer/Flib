package com.hawla.flib.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.hawla.flib.viewmodel.GameViewModel.ARE_INCREASING;
import static com.hawla.flib.viewmodel.GameViewModel.ARE_INCREASING_DEFAULT;
import static com.hawla.flib.viewmodel.GameViewModel.HEARTS;
import static com.hawla.flib.viewmodel.GameViewModel.HEARTS_DEFAULT;
import static com.hawla.flib.viewmodel.GameViewModel.TURNS;
import static com.hawla.flib.viewmodel.GameViewModel.TURNS_DEFAULT;
import static com.hawla.flib.views.game.GameActivity.GAME_SIZE;
import static com.hawla.flib.views.game.GameActivity.GAME_SIZE_DEFAULT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameViewModelTest {

    @Mock
    private Application application;
    @Mock
    private SharedPreferences prefs;

    @Before
    public void init(){
        application = mock(Application.class);
        prefs = mock(SharedPreferences.class);
        when(application.getSharedPreferences(any(), anyInt())).thenReturn(prefs);
        when(prefs.getString(eq(GAME_SIZE), any())).thenReturn(GAME_SIZE_DEFAULT);
        when(prefs.getString(eq(TURNS), any())).thenReturn(TURNS_DEFAULT);
        when(prefs.getString(eq(HEARTS), any())).thenReturn(HEARTS_DEFAULT);
        //when(prefs.getBoolean(eq(ARE_INCREASING), any())).thenReturn(ARE_INCREASING_DEFAULT);
    }

    @Test
    public void test_clickOnField(){
        // TODO how to test LiveData
//        GameViewModel gameViewModel = new GameViewModel(application);
        //gameViewModel.clickOnField(1, 1);
    }

    @Test
    public void test_gameOver(){

    }
}
