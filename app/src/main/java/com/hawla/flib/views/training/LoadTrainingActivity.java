package com.hawla.flib.views.training;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hawla.flib.R;
import com.hawla.flib.views.game.GameActivity;

import static com.hawla.flib.views.game.GameActivity.INTENT_TIMERACE;

public class LoadTrainingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);

        TextView textView = findViewById(R.id.progress_text_view);

        // Load Game
        Intent intent = new Intent(this, TrainingActivity.class);
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
