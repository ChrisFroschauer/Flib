package com.hawla.flib.views.titlescreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hawla.flib.R;

public class GameExplanationDialog extends DialogFragment {
    private TextView textView;
    private TextView subTextView;

    public GameExplanationDialog(){

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View view = inflater.inflate(
                R.layout.fragment_game_explanation_dialog, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedIstanceState){
        super.onViewCreated(view, savedIstanceState);
        Button closeButton = view.findViewById(R.id.close_dialog_button);
        closeButton.setOnClickListener((v)-> getDialog().dismiss());
    }

    @Override
    public void onResume(){
        super.onResume();
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
