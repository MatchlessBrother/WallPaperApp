package com.yuan.devlibrary._11___Widget.indicator;

import android.view.View;
import android.graphics.Paint;
import android.graphics.Canvas;
import com.nineoldandroids.animation.ValueAnimator;

public class BallBeatIndicator extends BaseIndicatorController
{
    public static final float SCALE = 1.0f;
    public static final int ALPHA = 255;

    private ValueAnimator alphaAnim = null;
    private ValueAnimator scaleAnim = null;
    private float[] scaleFloats = new float[]{SCALE, SCALE, SCALE};
    private int[] alphas = new int[]{ALPHA, ALPHA, ALPHA,};

    public void draw(Canvas canvas, Paint paint)
    {
        float circleSpacing=4;
        float radius=(getWidth()-circleSpacing*2)/6;
        float x = getWidth()/ 2-(radius*2+circleSpacing);
        float y=getHeight() / 2;
        for (int i = 0; i < 3; i++)
        {
            canvas.save();
            float translateX=x+(radius*2)*i+circleSpacing*i;
            canvas.translate(translateX, y);
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            paint.setAlpha(alphas[i]);
            canvas.drawCircle(0, 0, radius, paint);
            canvas.restore();
        }
    }

    public void createAnimation()
    {
        int[] delays=new int[]{350,0,350};
        for (int i = 0; i < 3; i++)
        {
            final int index=i;
            scaleAnim=ValueAnimator.ofFloat(1,0.75f,1);
            scaleAnim.setDuration(700);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    if (getTarget().getVisibility() == View.VISIBLE)
                        postInvalidate();
                }
            });
            scaleAnim.start();
            alphaAnim=ValueAnimator.ofInt(255,51,255);
            alphaAnim.setDuration(700);
            alphaAnim.setRepeatCount(-1);
            alphaAnim.setStartDelay(delays[i]);
            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    alphas[index] = (int) animation.getAnimatedValue();
                    if (getTarget().getVisibility() == View.VISIBLE)
                        postInvalidate();
                }
            });
            alphaAnim.start();
        }
    }

    public void showView()
    {
        if(alphaAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        alphaAnim.start();
        scaleAnim.start();
    }

    public void hideViewInvisible()
    {
        if(alphaAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        alphaAnim.cancel();
        scaleAnim.cancel();
    }

    public void hideViewGone()
    {
        if(alphaAnim == null || scaleAnim == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        alphaAnim.cancel();
        scaleAnim.cancel();
    }
}