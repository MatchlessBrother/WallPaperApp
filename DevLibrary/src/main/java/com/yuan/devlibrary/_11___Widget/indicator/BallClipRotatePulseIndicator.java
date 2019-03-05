package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;

public class BallClipRotatePulseIndicator extends BaseIndicatorController
{
    float scaleFloat1,scaleFloat2,degrees;
    private ValueAnimator scaleAnim = null;
    private ValueAnimator scaleAnim2 = null;
    private ValueAnimator rotateAnim = null;

    public void draw(Canvas canvas, Paint paint)
    {
        float circleSpacing=12;
        float x=getWidth()/2;
        float y=getHeight()/2;

        //draw fill circle
        canvas.save();
        canvas.translate(x, y);
        canvas.scale(scaleFloat1, scaleFloat1);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, x / 2.5f, paint);

        canvas.restore();

        canvas.translate(x, y);
        canvas.scale(scaleFloat2, scaleFloat2);
        canvas.rotate(degrees);

        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        //draw two arc
        float[] startAngles=new float[]{225,45};
        for (int i = 0; i < 2; i++)
        {
            RectF rectF=new RectF(-x+circleSpacing,-y+circleSpacing,x-circleSpacing,y-circleSpacing);
            canvas.drawArc(rectF, startAngles[i], 90, false, paint);
        }
    }

    public void createAnimation()
    {
        scaleAnim=ValueAnimator.ofFloat(1,0.3f,1);
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleFloat1 = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim.start();
        scaleAnim2=ValueAnimator.ofFloat(1,0.6f,1);
        scaleAnim2.setDuration(1000);
        scaleAnim2.setRepeatCount(-1);
        scaleAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleFloat2 = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim2.start();
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
        if(rotateAnim == null || scaleAnim == null || scaleAnim2 == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        scaleAnim.start();
        scaleAnim2.start();
        rotateAnim.start();
    }

    public void hideViewInvisible()
    {
        if(rotateAnim == null || scaleAnim == null || scaleAnim2 == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        scaleAnim.cancel();
        scaleAnim2.cancel();
        rotateAnim.cancel();
    }

    public void hideViewGone()
    {
        if(rotateAnim == null || scaleAnim == null || scaleAnim2 == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        scaleAnim.cancel();
        scaleAnim2.cancel();
        rotateAnim.cancel();
    }
}