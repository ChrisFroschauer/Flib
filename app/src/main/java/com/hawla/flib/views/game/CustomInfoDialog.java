package com.hawla.flib.views.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.hawla.flib.R;

public class CustomInfoDialog extends DialogFragment {
    private String mainText;
    private String subText = "";
    private TextView textView;
    private TextView subTextView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private DialogType type;

    public enum DialogType{
        LEVEL_UP, GAME_OVER, LOSE_HEART, INVISIBLE
    }

    public CustomInfoDialog(){

    }

    public void setDialogMainText(String text){
        mainText = text;
    }
    public void setDialogSubText(String text){
        subText = text;
    }

    public void setType(DialogType type){
        this.type = type;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View view = inflater.inflate(
                R.layout.fragment_custom_info_dialog, container);

        textView = view.findViewById(R.id.dialog_main_text);
        textView.setText(mainText);
        subTextView = view.findViewById(R.id.dialog_sub_text);
        subTextView.setText(subText);
        progressBar = view.findViewById(R.id.dialog_progress_bar);
        imageView = view.findViewById(R.id.image_dialog);

        handleImageForType();
        //---set the title for the dialog
        getDialog().setTitle(mainText);

        return view;
    }

    public ProgressBar getProgressBar(){
        return progressBar;
    }

    public ImageView getImageView(){
        return imageView;
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

    private void handleImageForType(){
        switch(this.type){
            case LEVEL_UP:
                imageView.setVisibility(View.GONE);
                break;
            case GAME_OVER:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_skull_crossbones_solid, null));
                break;
            case LOSE_HEART:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_heart_broken_solid, null));
                break;
            default:
                break;
        }
    }
}
