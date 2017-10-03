package com.sdsmdg.pulkit.letsstretch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
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
    float obstacleX, obstacleY, obstacleLen;
    float acceleration, vx, vy;
    Timer timer;
    boolean won = false;
    int deviation=10;
    boolean released;
    float[] starPositionsX;
    float[] starPositionsY;
    Drawable star;
    Drawable basketballNet;
    int i;

    public GameView(Context context, final AppCompatActivity activity) {
        super(context);
        this.activity = activity;
        height = activity.getWindowManager().getDefaultDisplay().getHeight();
        width = activity.getWindowManager().getDefaultDisplay().getWidth();
        starPositionsX = new float[6];
        starPositionsY = new float[6];
        starPositionsX[0] = width / 3;
        starPositionsX[1] = 2 * width / 3;
        starPositionsX[2] = width / 3;
        starPositionsX[3] = 2 * width / 3;
        starPositionsX[4] = width / 3;
        starPositionsX[5] = 2 * width / 3;
        starPositionsY[0] = height / 4;
        starPositionsY[1] = height / 4;
        starPositionsY[2] = height / 2;
        starPositionsY[3] = height / 2;
        starPositionsY[4] = 3 * height / 4;
        starPositionsY[5] = 3 * height / 4;
        star = getResources().getDrawable(R.drawable.star);
        basketballNet = getResources().getDrawable(R.drawable.basketball_net);
        ballPositionY = 2 * height / 3;
        ballPositionX = width / 4;
        ballRadius = 30;
        timer = new Timer();
        vx = 0;
        vy = 0;
        acceleration = width / 10000;
        obstacleX = 3 * width / 4;
        obstacleY = height / 3;
        obstacleLen = width / 4;
        released = false;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint yellow = new Paint();
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.STROKE);
        yellow.setStrokeWidth(10);
        Paint pink = new Paint();
        pink.setColor(Color.parseColor("#F34212"));
        pink.setStyle(Paint.Style.FILL);
        canvas.drawLine(obstacleX - obstacleLen / 2, obstacleY, obstacleX + obstacleLen / 2, obstacleY, yellow);
        canvas.drawLine(5, 2 * height / 3-30, 5, 2 * height / 3+30, yellow);
        canvas.drawLine(width / 2, 2 * height / 3-30, width / 2, 2 * height / 3+30, yellow);
        yellow.setStrokeWidth(5);
        if (released == false) {
            canvas.drawLine(0, 2 * height / 3, ballPositionX, ballPositionY, yellow);
            canvas.drawLine(ballPositionX, ballPositionY, width / 2, 2 * height / 3, yellow);
            canvas.drawCircle(ballPositionX, ballPositionY - ballRadius, ballRadius, pink);
        } else {
            ballPositionX += vx;
            ballPositionY -= vy;
            vy -= acceleration;
            if (vy == 0)
                acceleration *= -1;
            canvas.drawLine(0, 2 * height / 3, width / 2, 2 * height / 3, yellow);
            canvas.drawCircle(ballPositionX, ballPositionY, ballRadius, pink);
            if (obstacleY - ballPositionY <= 10 && obstacleY - ballPositionY >= 0 && ballPositionX >= obstacleX - obstacleLen / 2-5 && ballPositionX <= obstacleX + obstacleLen / 2+5 && won == false) {
                won = true;
                ballPositionX = obstacleX;
                vx = 0;
            }
            if (won == true && ballPositionY > (int) (obstacleY + obstacleLen)) {
                for (i = 0; i < 6; i++) {
                    star.setBounds((int) (-50 + starPositionsX[i]), (int) (starPositionsY[i] - 50), (int) (50 + starPositionsX[i]), (int) (starPositionsY[i] + 50));
                    star.draw(canvas);
                }
            }
        }
        if (won == true && ballPositionY<=(int) (obstacleY + obstacleLen)) {
            basketballNet.setBounds((int) (obstacleX - obstacleLen / 2)+deviation/2, (int) (obstacleY), (int) (obstacleX + obstacleLen / 2)-deviation/2, (int) (obstacleY + obstacleLen)-deviation);
            basketballNet.draw(canvas);
            if (deviation==10)
                deviation=0;
            else
                deviation=10;
        } else {
            basketballNet.setBounds((int) (obstacleX - obstacleLen / 2), (int) (obstacleY), (int) (obstacleX + obstacleLen / 2), (int) (obstacleY + obstacleLen));
            basketballNet.draw(canvas);
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
                } else if (Math.abs(ballPositionX - event.getX()) <= 40 && Math.abs(ballPositionY - event.getY()) <= 40) {
                    ballPositionX = event.getX();
                    ballPositionY = 2 * height / 3;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (stretchLimit(ballPositionX, ballPositionY)) {
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
        if (ballPositionY >= 2 * height / 3 - 20) {
            if (Math.pow(ballPositionX - width / 4, 2) + Math.pow(ballPositionY - 2 * height / 3, 2) - Math.pow(width / 4, 2) <= 0) {
                return true;
            } else
                return false;
        } else
            return false;
    }

}
