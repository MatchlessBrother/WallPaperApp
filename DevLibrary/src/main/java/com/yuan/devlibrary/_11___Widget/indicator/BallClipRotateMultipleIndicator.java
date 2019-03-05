package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;

public class BallClipRotateMultipleIndicator extends BaseIndicatorController
{
    float scaleFloat=1,degrees;
    private ValueAnimator scaleAnim = null;
    private ValueAnimator rotateAnim = null;
    public void draw(Canvas canvas, Paint paint)
    {
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        float circleSpacing=12;
        float x=getWidth()/2;
        float y=getHeight()/2;

        canvas.save();

        canvas.translate(x, y);
        canvas.scale(scaleFloat, scaleFloat);
        canvas.rotate(degrees);

        //draw two big arc
        float[] bStartAngles=new float[]{135,-45};
        for (int i = 0; i < 2; i++) {
            RectF rectF=new RectF(-x+circleSpacing,-y+circleSpacing,x-circleSpacing,y-circleSpacing);
            canvas.drawArc(rectF, bStartAngles[i], 90, false, paint);
        }

        canvas.restore();
        canvas.translate(x, y);
        canvas.scale(scaleFloat, scaleFloat);
        canvas.rotate(-degrees);
        //draw two small arc
        float[] sStartAngles=new float[]{225,45};
        for (int i = 0; i < 2; i++) {
            RectF rectF=new RectF(-x/1.8f+circleSpacing,-y/1.8f+circleSpacing,x/1.8f-circleSpacing,y/1.8f-circleSpacing);
            canvas.drawArc(rectF, sStartAngles[i], 90, false, paint);
        }
    }

    public void createAnimation()
    {
        scaleAnim=ValueAnimator.ofFloat(1,0.6f,1);
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleFloat = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim.start();

        rotateAnim=ValueAnimator.ofFloat(0, 180,360);
        rotateAnim.setDuration(1000);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                degrees = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rotateAnim.start();
    }

    public void showView()
    {
        if(rotateAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        scaleAnim.start();
        rotateAnim.start();
    }

    public void hideViewInvisible()
    {
        if(rotateAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        scaleAnim.cancel();
        rotateAnim.cancel();
    }

    public void hideViewGone()
    {
        if(rotateAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        scaleAnim.cancel();
        rotateAnim.cancel();
    }
}