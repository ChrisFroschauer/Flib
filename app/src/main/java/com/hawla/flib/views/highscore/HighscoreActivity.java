package com.hawla.flib.views.highscore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.hawla.flib.R;
import com.hawla.flib.views.titlescreen.TitlescreenActivity;

import static com.hawla.flib.views.game.GameActivity.INTENT_TIMERACE;

public class HighscoreActivity extends AppCompatActivity {

    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        progressBar = findViewById(R.id.progress_bar_highscore);

        new Handler().postDelayed(()->{

            SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

            // handle which tab the intent wants to be
            boolean wantsToSeeTimerace = getIntent().getBooleanExtra(INTENT_TIMERACE, false);
            TabLayout.Tab tab;
            if (wantsToSeeTimerace){
                tab = tabs.getTabAt(1);
            }else{
                tab = tabs.getTabAt(0);
            }
            if (tab != null) tab.select();

        }, 100);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, TitlescreenActivity.class);
        startActivity(intent);
    }
}