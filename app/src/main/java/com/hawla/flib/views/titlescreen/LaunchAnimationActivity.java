package com.hawla.flib.views.titlescreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.hawla.flib.R;
import com.hawla.flib.utils.AnimationDrawableWithCallback;

public class LaunchAnimationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_animation);

        ImageView launchImage = findViewById(R.id.launch_image_view);
        animateLaunchIcon(launchImage);

    }

    private void animateLaunchIcon(ImageView launchImage){
        Drawable[] layers = new Drawable[6];
        /*layers[0] = getResources().getDrawable(R.drawable.ic_flib_anim_0, null);
        layers[1] = getResources().getDrawable(R.drawable.ic_flib_anim_1, null);
        layers[2] = getResources().getDrawable(R.drawable.ic_flib_anim_2, null);
        layers[3] = getResources().getDrawable(R.drawable.ic_flib_anim_3, null);
        layers[4] = getResources().getDrawable(R.drawable.ic_flib_anim_4, null);
        layers[5] = getResources().getDrawable(R.drawable.ic_flib_anim_5, null);
        layers[6] = getResources().getDrawable(R.drawable.ic_flib_anim_6, null);*/

        layers[0] = getResources().getDrawable(R.drawable.ic_flib_anim_0, null);
        layers[1] = getResources().getDrawable(R.drawable.ic_flib_anim_3, null);
        layers[2] = getResources().getDrawable(R.drawable.ic_flib_anim_4, null);
        layers[3] = getResources().getDrawable(R.drawable.ic_flib_anim_8, null);
        layers[4] = getResources().getDrawable(R.drawable.ic_flib_anim_5, null);
        layers[5] = getResources().getDrawable(R.drawable.ic_flib_anim_6, null);
        //layers[7] = getResources().getDrawable(R.drawable.ic_flib_anim_7, null);

        AnimationDrawableWithCallback animationDrawable = new AnimationDrawableWithCallback();
        for (int i = 0; i < layers.length; i++){
            animationDrawable.addFrame(layers[i], 150);
        }
        launchImage.setImageDrawable(animationDrawable);
        animationDrawable.setOneShot(true);
        animationDrawable.setAnimationFinishListener(()->{
            goToTitlescreenActivity();
        });

        new Handler().postDelayed(()->{
                animationDrawable.start();
        }, 200);

        //new Handler().postDelayed(this::goToTitlescreenActivity, 800);
    }

    private void goToTitlescreenActivity(){
        Intent intent = new Intent(this, TitlescreenActivity.class);
        startActivity(intent);
    }

}


