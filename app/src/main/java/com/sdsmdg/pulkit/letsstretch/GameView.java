package com.sdsmdg.pulkit.letsstretch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pulkit on 30/9/17.
 */

public class GameView extends View {
    AppCompatActivity activity;
    float height, width;
    float ballPositionX, ballPositionY, ballRadius;
    float acceleration, vx, vy;
    Timer timer;
    boolean released;

    public GameView(Context context, final AppCompatActivity activity) {
        super(context);
        this.activity = activity;
        height = activity.getWindowManager().getDefaultDisplay().getHeight();
        width = activity.getWindowManager().getDefaultDisplay().getWidth();
        ballPositionY = 2 * height / 3;
        ballPositionX = width / 4;
        ballRadius = 30;
        timer = new Timer();
        vx = 0;
        vy = 0;
        acceleration = width/10000;
        released = false;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.STROKE);
        yellow.setStrokeWidth(5);
        Paint pink = new Paint();
        pink.setColor(Color.parseColor("#F34212"));
        pink.setStyle(Paint.Style.FILL);
        if (released == false) {
            canvas.drawLine(0, 2 * height / 3, ballPositionX, ballPositionY, yellow);
            canvas.drawLine(ballPositionX, ballPositionY, width / 2, 2 * height / 3, yellow);
            canvas.drawCircle(ballPositionX, ballPositionY - ballRadius, ballRadius, pink);
        } else {
            ballPositionX+=vx;
            ballPositionY-=vy;
            vy-=acceleration;
            if(vy==0)
             acceleration*=-1;
            canvas.drawLine(0, 2 * height / 3, width/2, 2 * height / 3, yellow);
            canvas.drawCircle(ballPositionX, ballPositionY, ballRadius, pink);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ballPositionX - event.getX()) <= 40 && Math.abs(ballPositionY - event.getY()) <= 40 && stretchLimit(ballPositionX, ballPositionY)) {
                    ballPositionX = event.getX();
                    ballPositionY = event.getY();
                    invalidate();
                }else
                if(Math.abs(ballPositionX - event.getX()) <= 40 && Math.abs(ballPositionY - event.getY()) <= 40)
                {
                    ballPositionX = event.getX();
                    ballPositionY = 2 * height / 3;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(stretchLimit(ballPositionX,ballPositionY)) {
                    released = true;
                    ballPositionX = event.getX();
                    ballPositionY = event.getY();
                    vy = (ballPositionY - 2 * height / 3) / 10;
                    vx = (width / 4 - ballPositionX) / 25;
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    activity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            invalidate();
                                        }
                                    });
                                }
                            }, 0, 1);
                }
                break;
        }
        return true;
    }

    boolean stretchLimit(float ballPositionX, float ballPositionY) {
        if (ballPositionY >= 2 * height / 3-20) {
            if (Math.pow(ballPositionX - width / 4, 2) + Math.pow(ballPositionY - 2 * height / 3, 2) - Math.pow(width / 4, 2) <= 0) {
                return true;
            } else
                return false;
        } else
            return false;
    }

}
