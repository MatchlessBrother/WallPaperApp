package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.nineoldandroids.animation.ValueAnimator;

public class BallScaleIndicator extends BaseIndicatorController
{
    float scale = 1;
    int alpha = 255;

    public void draw(Canvas canvas, Paint paint)
    {
        float circleSpacing=4;
        paint.setAlpha(alpha);
        canvas.scale(scale,scale,getWidth()/2,getHeight()/2);
        paint.setAlpha(alpha);
        canvas.drawCircle(getWidth()/2,getHeight()/2,getWidth()/2-circleSpacing,paint);
    }

    public void createAnimation()
    {
        scaleAnim=ValueAnimator.ofFloat(0,1);
        scaleAnim.setInterpolator(new LinearInterpolator());
        scaleAnim.setDuration(1000);
        scaleAnim.setRepeatCount(-1);
        scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                scale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        scaleAnim.start();

        alphaAnim=ValueAnimator.ofInt(255, 0);
        alphaAnim.setInterpolator(new LinearInterpolator());
        alphaAnim.setDuration(1000);
        alphaAnim.setRepeatCount(-1);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                alpha = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        alphaAnim.start();
    }

    private ValueAnimator scaleAnim = null;
    private ValueAnimator alphaAnim = null;
    public void showView()
    {
        if(alphaAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        scaleAnim.start();
        alphaAnim.start();
    }

    public void hideViewInvisible()
    {
        if(alphaAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        scaleAnim.cancel();
        alphaAnim.cancel();
    }

    public void hideViewGone()
    {
        if(alphaAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        scaleAnim.cancel();
        alphaAnim.cancel();
    }
}