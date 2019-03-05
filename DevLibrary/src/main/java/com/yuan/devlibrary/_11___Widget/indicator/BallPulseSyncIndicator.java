package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;

public class BallPulseSyncIndicator extends BaseIndicatorController
{
    float[] translateYFloats=new float[3];

    public void draw(Canvas canvas, Paint paint)
    {
        float circleSpacing=4;
        float radius=(getWidth()-circleSpacing*2)/6;
        float x = getWidth()/ 2-(radius*2+circleSpacing);
        for (int i = 0; i < 3; i++)
        {
            canvas.save();
            float translateX=x+(radius*2)*i+circleSpacing*i;
            canvas.translate(translateX, translateYFloats[i]);
            canvas.drawCircle(0, 0, radius, paint);
            canvas.restore();
        }
    }

    public void createAnimation()
    {
        float circleSpacing=4;
        float radius=(getWidth()-circleSpacing*2)/6;
        int[] delays=new int[]{70,140,210};
        for (int i = 0; i < 3; i++)
        {
            final int index=i;
            scaleAnim=ValueAnimator.ofFloat(getHeight()/2,getHeight()/2-radius*2,getHeight()/2);
            scaleAnim.setDuration(600);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    translateYFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.start();
        }
    }

    private ValueAnimator scaleAnim = null;
    public void showView()
    {
        if(scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        scaleAnim.start();
    }

    public void hideViewInvisible()
    {
        if(scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        scaleAnim.cancel();
    }

    public void hideViewGone()
    {
        if(scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        scaleAnim.cancel();
    }
}