package com.group.neusoft.moviesurfer.coco;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.group.neusoft.moviesurfer.R;
import com.group.neusoft.moviesurfer.disordia.util.MainPageActivity;

public class StartActivity extends AppCompatActivity {
    View view;
    TextView titleView;
    ImageView plt1;
    ImageView plt2;
    AnimatorSet rotateSet2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animation);
        view= findViewById(R.id.hello_icon);
        titleView= (TextView) findViewById(R.id.start_title);
        plt1= (ImageView) findViewById(R.id.pallate_small);
        plt2= (ImageView) findViewById(R.id.pallete_big);
        rotateSet2= (AnimatorSet) AnimatorInflater.loadAnimator(StartActivity.this,R.animator.rotate2);
        rotateSet2.setTarget(plt2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AnimatorSet rotateSet= (AnimatorSet) AnimatorInflater.loadAnimator(StartActivity.this,R.animator.rotate);
        rotateSet.setTarget(plt1);
        rotateSet.start();
        rotateSet2.start();
        AnimatorSet set2 = (AnimatorSet) AnimatorInflater.loadAnimator(StartActivity.this,
                R.animator.titleanimator);
        set2.setTarget(titleView);
        set2.start();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(StartActivity.this, MainPageActivity.class);
                startActivity(i);
                StartActivity.this.finish();
            }
        },3000);
    }
}
