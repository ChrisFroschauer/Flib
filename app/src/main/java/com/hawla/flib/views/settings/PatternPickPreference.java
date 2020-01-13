package com.hawla.flib.views.settings;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.hawla.flib.R;

import java.util.ArrayList;
import java.util.List;

public class PatternPickPreference extends Preference {

    private final String PICKPATTERN_TAG = "pick_pattern_";

    private SharedPreferences preferencesPatternPick;

    private List<Button> patternButtons;

    private List<Boolean> pattern;
    /*private Boolean[] DEFAULT_PATTERN = {true, true, true,
                                        true, true, true,
                                        true, true, true};*/

    public PatternPickPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PatternPickPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        preferencesPatternPick = context.getSharedPreferences("PickPattern", Context.MODE_PRIVATE);
        //setWidgetLayoutResource(R.layout.pattern_picker_preference);
        //setLayoutResource(R.layout.pattern_picker_preference);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.itemView.setClickable(false); // disable parent click

        patternButtons = new ArrayList<>();
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button1));
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button2));
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button3));
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button4));
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button5));
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button6));
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button7));
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button8));
        patternButtons.add((Button)holder.findViewById(R.id.ppick_button9));

        for (int index = 0; index < patternButtons.size(); index++){
            Button button = patternButtons.get(index);
            button.setClickable(true);
            final int curr_index = index;
            button.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    boolean curr = !pattern.get(curr_index);
                    pattern.set(curr_index, curr );
                    persistPickPattern(curr_index, curr);
                    togglePickPatternButton(v, curr);
                }

                private void persistPickPattern(int i, boolean value){
                    //SharedPreferences sharedPreferences = getSharedPreferences();
                    SharedPreferences.Editor editor = preferencesPatternPick.edit();
                    editor.putBoolean(PICKPATTERN_TAG + i, value);
                    editor.commit();
                }

            });
        }

        loadSavedPattern();
    }

    private void loadSavedPattern() {
        Log.i("TestCustomPref", "loadSavedPattern");
        //SharedPreferences sharedPreferences = getSharedPreferences();
        List<Boolean> patternList = new ArrayList<>();
        for (int i = 0; i < 9; i++){
            boolean curr = preferencesPatternPick.getBoolean(PICKPATTERN_TAG +i,true);
            Log.i("TestCustomPref", i+": " + preferencesPatternPick.getBoolean(PICKPATTERN_TAG +i,true) );
            patternList.add(curr);
            togglePickPatternButton(patternButtons.get(i), curr);
        }
        pattern = patternList;
    }

    public void togglePickPatternButton(View v, boolean isSet){
        if (isSet){
            ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getContext(), R.color.enabledPattern));
        }else{
            ViewCompat.setBackgroundTintList(v, ContextCompat.getColorStateList(getContext(), R.color.disabledPattern));

        }
    }

}