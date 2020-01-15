package com.hawla.flib.views.highscore;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hawla.flib.R;
import com.hawla.flib.viewmodel.HighscoreViewModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class HighscoreTabFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private HighscoreViewModel highscoreViewModel;

    private TextView shorthandsTextView;
    private TextView valuesTextView;

    public static HighscoreTabFragment newInstance(int index) {
        HighscoreTabFragment fragment = new HighscoreTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        highscoreViewModel = ViewModelProviders.of(this).get(HighscoreViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        boolean isTimerace = (index != 1);

        new Handler().post(()-> highscoreViewModel.setIndex(isTimerace));
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_highscore_tab, container, false);

        shorthandsTextView = root.findViewById(R.id.shorthands_text_view);
        valuesTextView = root.findViewById(R.id.values_text_view);
        highscoreViewModel.getText().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(@Nullable String[] s) {
                if (shorthandsTextView != null){
                    shorthandsTextView.setText(s[0]);
                    valuesTextView.setText(s[1]);
                }else{
                    Log.w("setText FAILED", s[0]);
                }

            }
        });
        return root;
    }
}