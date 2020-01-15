package com.hawla.flib.views.highscore;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.hawla.flib.R;
import com.hawla.flib.views.titlescreen.TitlescreenActivity;

import static com.hawla.flib.views.game.GameActivity.INTENT_TIMERACE;

public class HighscoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        boolean comesFromTimeraceGameOver = getIntent().getBooleanExtra(INTENT_TIMERACE, false);
        TabLayout.Tab tab;
        if (comesFromTimeraceGameOver){
            tab = tabs.getTabAt(1);
        }else{
            tab = tabs.getTabAt(0);
        }
        if (tab != null) tab.select();

        /*FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, TitlescreenActivity.class);
        startActivity(intent);
    }
}