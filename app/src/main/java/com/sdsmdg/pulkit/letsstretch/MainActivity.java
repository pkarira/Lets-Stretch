package com.sdsmdg.pulkit.letsstretch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    Button startOver;
    AppCompatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        relativeLayout=(RelativeLayout)findViewById(R.id.rlayout);
        relativeLayout.addView(new GameView(this,this));
        startOver=(Button)findViewById(R.id.restart);
        startOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.addView(new GameView(getApplicationContext(),activity));
            }
        });
    }
}
