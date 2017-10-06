package com.sdsmdg.pulkit.letsstretch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * Created by pulkit on 5/10/17.
 */

public class GameOver extends View {
    private Context context;
    private float width,height,score,result;
    private AppCompatActivity activity;
    GameOver(Context context, AppCompatActivity activity,float width, float height, float score, float result)
    {
        super(context);
        this.context=context;
        this.width=width;
        this.height=height;
        this.score=score;
        this.result=result;
        this.activity=activity;
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("on","on");
        Paint red = new Paint();
        red.setColor(Color.RED);
        red.setStyle(Paint.Style.FILL);
        Paint textColor = new Paint();
        textColor.setColor(Color.WHITE);
        textColor.setStyle(Paint.Style.FILL);
        RectF gameOver=new RectF((float)0,(float)0.1*height,width,(float)0.25*height);
        canvas.drawRect(gameOver,red);
        RectF score=new RectF((float)0,(float)0.4*height,width,(float)0.55*height);
        canvas.drawRect(score,red);
        RectF result=new RectF((float)0,(float)0.7*height,width,(float)0.85*height);
        canvas.drawRect(result,red);
    }
}
