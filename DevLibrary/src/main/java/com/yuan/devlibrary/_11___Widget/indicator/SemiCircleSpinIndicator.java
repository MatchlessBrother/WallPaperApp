package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;

public class SemiCircleSpinIndicator extends BaseIndicatorController
{
    public void draw(Canvas canvas, Paint paint)
    {
        RectF rectF=new RectF(0,0,getWidth(),getHeight());
        canvas.drawArc(rectF,-60,120,false,paint);
    }

    public void createAnimation()
    {
        rotateAnim=ObjectAnimator.ofFloat(getTarget(),"rotation",0,180,360);
        rotateAnim.setDuration(600);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.start();
    }

    private ObjectAnimator rotateAnim = null;
    public void showView()
    {
        if(rotateAnim == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        rotateAnim.start();
    }

    public void hideViewInvisible()
    {
        if(rotateAnim == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        rotateAnim.cancel();
    }

    public void hideViewGone()
    {
        if(rotateAnim == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        rotateAnim.cancel();
    }
}