package com.yuan.devlibrary._11___Widget.indicator;

import android.view.View;
import android.view.animation.LinearInterpolator;
import com.nineoldandroids.animation.ValueAnimator;

public class BallZigZagDeflectIndicator extends BallZigZagIndicator
{
    public void createAnimation()
    {
        float startX=getWidth()/6;
        float startY=getWidth()/6;
        for (int i = 0; i < 2; i++)
        {
            final int index=i;
            translateXAnim=ValueAnimator.ofFloat(startX,getWidth()-startX,startX,getWidth()-startX,startX);
            if (i==1)
            {
                translateXAnim=ValueAnimator.ofFloat(getWidth()-startX,startX,getWidth()-startX,startX,getWidth()-startX);
            }
            translateYAnim=ValueAnimator.ofFloat(startY,startY,getHeight()-startY,getHeight()-startY,startY);
            if (i==1)
            {
                translateYAnim=ValueAnimator.ofFloat(getHeight()-startY,getHeight()-startY,startY,startY,getHeight()-startY);
            }

            translateXAnim.setDuration(2000);
            translateXAnim.setInterpolator(new LinearInterpolator());
            translateXAnim.setRepeatCount(-1);
            translateXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    translateX [index]= (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateXAnim.start();
            translateYAnim.setDuration(2000);
            translateYAnim.setInterpolator(new LinearInterpolator());
            translateYAnim.setRepeatCount(-1);
            translateYAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    translateY [index]= (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            translateYAnim.start();
        }
    }

    private ValueAnimator translateXAnim = null;
    private ValueAnimator translateYAnim = null;
    public void showView()
    {
        if(translateXAnim == null || translateYAnim == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        translateXAnim.start();
        translateYAnim.start();
    }

    public void hideViewInvisible()
    {
        if(translateXAnim == null || translateYAnim == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        translateXAnim.cancel();
        translateYAnim.cancel();
    }

    public void hideViewGone()
    {
        if(translateXAnim == null || translateYAnim == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        translateXAnim.cancel();
        translateYAnim.cancel();
    }
}