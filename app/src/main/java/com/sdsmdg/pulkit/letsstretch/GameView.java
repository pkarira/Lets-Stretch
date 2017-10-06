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
import android.widget.RelativeLayout;

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
    boolean released;
    float[] starPositionsX;
    float[] starPositionsY;
    Drawable star;
    Drawable basketballNet;
    Drawable gameOverTitle;
    Context context;
    RelativeLayout relativeLayout;
    int starDeflection,starDeflectionLimit,starDeflectionInterval;
    int netDeflection,netDeflectionLimit,netDeflectionInterval;
    int i;

    public GameView(Context context, final AppCompatActivity activity,RelativeLayout relativeLayout) {
        super(context);
        this.context=context;
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
        starDeflection=0;
        starDeflectionLimit=30;
        starDeflectionInterval=1;
        netDeflection=0;
        netDeflectionLimit=10;
        netDeflectionInterval=1;
        star = getResources().getDrawable(R.drawable.star);
        basketballNet = getResources().getDrawable(R.drawable.basketball_net);
        gameOverTitle=getResources().getDrawable(R.drawable.gameover);
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
        this.relativeLayout=relativeLayout;
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
            if(ballPositionY==0 && vy>0)
                acceleration*=1000;
            if(ballPositionY==0 && vy<0)
                acceleration/=1000;
            canvas.drawLine(0, 2 * height / 3, width / 2, 2 * height / 3, yellow);
            canvas.drawCircle(ballPositionX, ballPositionY, ballRadius, pink);
            if (obstacleY - ballPositionY <= 10 && obstacleY - ballPositionY >= 0 && ballPositionX >= obstacleX - obstacleLen / 2-5 && ballPositionX <= obstacleX + obstacleLen / 2+5 && won == false) {
                won = true;
                ballPositionX = obstacleX;
                vx = 0;
            }
            if(ballPositionY>obstacleY && vy<0)
            {
                gameOverTitle.setBounds(0, 0, (int)width, (int)height/5);
                gameOverTitle.draw(canvas);
            }
            if (won == true && ballPositionY > (int) (obstacleY + obstacleLen)) {
                for (i = 0; i < 6; i++) {
                    star.setBounds((int) (-50 + starPositionsX[i])-starDeflection, (int) (starPositionsY[i] - 50)-starDeflection, (int) (50 + starPositionsX[i])+starDeflection, (int) (starPositionsY[i] + 50)+starDeflection);
                    star.draw(canvas);
                    if((starDeflectionInterval>0 && starDeflection<starDeflectionLimit) ||(starDeflectionInterval<0 && starDeflection>-1*starDeflectionLimit))
                        starDeflection+=starDeflectionInterval;
                    else
                    if(starDeflection==starDeflectionLimit)
                        starDeflectionInterval*=-1;
                    else
                     if(starDeflection==-1*starDeflectionLimit)
                         starDeflectionInterval*=-1;
                }
            }
        }
        if (won == true && ballPositionY<=(int) (obstacleY + obstacleLen)) {
            basketballNet.setBounds((int) (obstacleX - obstacleLen / 2)+netDeflection, (int) (obstacleY), (int) (obstacleX + obstacleLen / 2)-netDeflection, (int) (obstacleY + obstacleLen)-netDeflection);
            basketballNet.draw(canvas);
            if((netDeflectionInterval>0 && netDeflection<0) || (netDeflectionInterval<0 && netDeflection>-1*netDeflectionLimit))
                netDeflection+=netDeflectionInterval;
            else
            if(netDeflection==0)
                netDeflectionInterval*=-1;
            else
            if(netDeflection==-1*netDeflectionLimit)
                netDeflectionInterval*=-1;

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
                    vx = (width / 4 - ballPositionX) / 40;
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
