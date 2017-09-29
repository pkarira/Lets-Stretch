package com.sdsmdg.pulkit.letsstretch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by pulkit on 30/9/17.
 */

public class GameView extends View {
    AppCompatActivity activity;
    float height, width;
    float ballPositionX, ballPositionY, ballRadius;

    public GameView(Context context, final AppCompatActivity activity) {
        super(context);
        this.activity = activity;
        height = activity.getWindowManager().getDefaultDisplay().getHeight();
        width = activity.getWindowManager().getDefaultDisplay().getWidth();
        ballPositionY = 2 * height / 3;
        ballPositionX = width / 4;
        ballRadius = 30;
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
        canvas.drawCircle(ballPositionX, ballPositionY - ballRadius, ballRadius, pink);
        if (stretchLimit(ballPositionX, ballPositionY)) {
            canvas.drawLine(0, 2 * height / 3, ballPositionX, ballPositionY, yellow);
            canvas.drawLine(ballPositionX, ballPositionY, width / 2, 2 * height / 3, yellow);
        } else {
            canvas.drawLine(0, 2 * height / 3, width/2, 2 * height / 3, yellow);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ballPositionX - event.getX()) <= 100 && Math.abs(ballPositionY - event.getY()) <= 100 && stretchLimit(ballPositionX, ballPositionY)) {
                    ballPositionX = event.getX();
                    ballPositionY = event.getY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                ballPositionX = event.getX();
                ballPositionY = event.getY();
                break;
        }
        return true;
    }

    boolean stretchLimit(float ballPositionX, float ballPositionY) {
        if (ballPositionY >= 2 * height / 3) {
            if (Math.pow(ballPositionX - width / 4, 2) + Math.pow(ballPositionY - 2 * height / 3, 2) - Math.pow(width / 4, 2) <= 0) {
                return true;
            } else
                return false;
        } else
            return false;
    }
}
