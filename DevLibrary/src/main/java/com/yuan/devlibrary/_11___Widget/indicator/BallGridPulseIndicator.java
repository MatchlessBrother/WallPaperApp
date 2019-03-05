package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.nineoldandroids.animation.ValueAnimator;

public class BallGridPulseIndicator extends BaseIndicatorController
{
    public static final int ALPHA=255;
    public static final float SCALE=1.0f;
    int[] alphas=new int[]{ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA};
    float[] scaleFloats=new float[]{SCALE, SCALE, SCALE, SCALE, SCALE, SCALE, SCALE, SCALE, SCALE};
    private static int a;

    public void draw(Canvas canvas, Paint paint)
    {
        float circleSpacing=4;
        float radius=(getWidth()-circleSpacing*4)/6;
        float x = getWidth()/ 2-(radius*2+circleSpacing);
        float y = getWidth()/ 2-(radius*2+circleSpacing);

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                canvas.save();
                float translateX=x+(radius*2)*j+circleSpacing*j;
                float translateY=y+(radius*2)*i+circleSpacing*i;
                canvas.translate(translateX, translateY);
                canvas.scale(scaleFloats[3 * i + j], scaleFloats[3 * i + j]);
                paint.setAlpha(alphas[3 * i + j]);
                canvas.drawCircle(0, 0, radius, paint);
                canvas.restore();
            }
        }
    }

    public void createAnimation()
    {
        int[] durations={720, 1020, 1280, 1420, 1450, 1180, 870, 1450, 1060};
        int[] delays= {-60, 250, -170, 480, 310, 30, 460, 780, 450};

        for (int i = 0; i < 9; i++)
        {
            final int index=i;
            scaleAnim=ValueAnimator.ofFloat(1,0.5f,1);
            scaleAnim.setDuration(durations[i]);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            scaleAnim.start();
            alphaAnim=ValueAnimator.ofInt(255, 210, 122, 255);
            alphaAnim.setDuration(durations[i]);
            alphaAnim.setRepeatCount(-1);
            alphaAnim.setStartDelay(delays[i]);
            alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                public void onAnimationUpdate(ValueAnimator animation)
                {
                    alphas[index] = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            alphaAnim.start();
        }
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