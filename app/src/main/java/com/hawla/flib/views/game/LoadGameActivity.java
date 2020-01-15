package com.hawla.flib.views.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.hawla.flib.R;

import static com.hawla.flib.views.game.GameActivity.INTENT_TIMERACE;

public class LoadGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        TextView textView = findViewById(R.id.progress_text_view);

        // Load Game
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(INTENT_TIMERACE, getIntent().getBooleanExtra(INTENT_TIMERACE, false));
        new Handler().postDelayed(()->{
            startActivity(intent);
        }, 300);
        new Handler().postDelayed(()->{
            textView.setText(getString(R.string.loading_1));
        }, 10);
        new Handler().postDelayed(()->{
            textView.setText(getString(R.string.loading_2));
        }, 1500);
        new Handler().postDelayed(()->{
            textView.setText(getString(R.string.loading_3));
        }, 3000);
    }
}
