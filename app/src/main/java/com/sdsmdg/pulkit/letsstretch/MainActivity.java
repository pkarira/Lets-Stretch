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
    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        relativeLayout=(RelativeLayout)findViewById(R.id.rlayout);
        gameView=new GameView(this,this);
        relativeLayout.addView(gameView);
        startOver=(Button)findViewById(R.id.restart);
        startOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.removeView(gameView);
                gameView=new GameView(getApplicationContext(),activity);
                relativeLayout.addView(gameView);
            }
        });
    }
}
