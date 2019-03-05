package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.nineoldandroids.animation.ValueAnimator;

public class PacmanIndicator extends BaseIndicatorController
{
    private int alpha;
    private float translateX;
    private float degrees1,degrees2;

    public void draw(Canvas canvas, Paint paint)
    {
        drawPacman(canvas,paint);
        drawCircle(canvas,paint);
    }

    private void drawPacman(Canvas canvas,Paint paint)
    {
        float x=getWidth()/2;
        float y=getHeight()/2;

        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(degrees1);
        paint.setAlpha(255);
        RectF rectF1=new RectF(-x/1.7f,-y/1.7f,x/1.7f,y/1.7f);
        canvas.drawArc(rectF1, 0, 270, true, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(degrees2);
        paint.setAlpha(255);
        RectF rectF2=new RectF(-x/1.7f,-y/1.7f,x/1.7f,y/1.7f);
        canvas.drawArc(rectF2,90,270,true,paint);
        canvas.restore();
    }

    private void drawCircle(Canvas canvas, Paint paint)
    {
        float radius=getWidth()/11;
        paint.setAlpha(alpha);
        canvas.drawCircle(translateX, getHeight() / 2, radius, paint);
    }

    public void createAnimation()
    {
        float startT=getWidth()/11;
        translationAnim=ValueAnimator.ofFloat(getWidth()-startT,getWidth()/2);
        translationAnim.setDuration(650);
        translationAnim.setInterpolator(new LinearInterpolator());
        translationAnim.setRepeatCount(-1);
        translationAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                translateX = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        translationAnim.start();

        alphaAnim=ValueAnimator.ofInt(255,122);
        alphaAnim.setDuration(650);
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

        rotateAnim1=ValueAnimator.ofFloat(0, 45, 0);
        rotateAnim1.setDuration(650);
        rotateAnim1.setRepeatCount(-1);
        rotateAnim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                degrees1 = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rotateAnim1.start();

        rotateAnim2=ValueAnimator.ofFloat(0,-45,0);
        rotateAnim2.setDuration(650);
        rotateAnim2.setRepeatCount(-1);
        rotateAnim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                degrees2 = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rotateAnim2.start();
    }

    private ValueAnimator translationAnim = null;
    private ValueAnimator alphaAnim = null;
    private ValueAnimator rotateAnim1 = null;
    private ValueAnimator rotateAnim2 = null;
    public void showView()
    {
        if(translationAnim == null && alphaAnim == null && rotateAnim1 == null && rotateAnim2 == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        translationAnim.start();
        alphaAnim.start();
        rotateAnim1.start();
        rotateAnim2.start();
    }

    public void hideViewInvisible()
    {
        if(translationAnim == null && alphaAnim == null && rotateAnim1 == null && rotateAnim2 == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        translationAnim.cancel();
        alphaAnim.cancel();
        rotateAnim1.cancel();
        rotateAnim2.cancel();
    }

    public void hideViewGone()
    {
        if(translationAnim == null && alphaAnim == null && rotateAnim1 == null && rotateAnim2 == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        translationAnim.cancel();
        alphaAnim.cancel();
        rotateAnim1.cancel();
        rotateAnim2.cancel();
    }
}