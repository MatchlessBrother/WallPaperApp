package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

public class TriangleSkewSpinIndicator extends BaseIndicatorController
{
    public void draw(Canvas canvas, Paint paint)
    {
        Path path=new Path();
        path.moveTo(getWidth()/5,getHeight()*4/5);
        path.lineTo(getWidth()*4/5, getHeight()*4/5);
        path.lineTo(getWidth()/2,getHeight()/5);
        path.close();
        canvas.drawPath(path, paint);
    }

    public void createAnimation()
    {
        /*ObjectAnimator rotationXAnimation=ObjectAnimator.ofFloat(getTarget(), "rotationX",0,180,180,0,0);
        rotationXAnimation.setDuration(3000);

        ObjectAnimator rotationYAnimation=ObjectAnimator.ofFloat(getTarget(), "rotationY",0,0,180,180,0);
        rotationYAnimation.setDuration(3000);

        final AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(rotationXAnimation,rotationYAnimation);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                System.out.println("onAnimationRepeat");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                System.out.println("onAnimationEnd(");
                animatorSet.start();
            }
        });
        animatorSet.start();*/


        /*PropertyValuesHolder rotation1=PropertyValuesHolder.ofFloat("rotationX",0,180,0,0,0);
        PropertyValuesHolder rotation2=PropertyValuesHolder.ofFloat("rotationX",0,0,180,0,0);
        PropertyValuesHolder rotation3=PropertyValuesHolder.ofFloat("rotationY",0,0,180,0,0);
        PropertyValuesHolder rotation4=PropertyValuesHolder.ofFloat("rotationY",0,0,0,180,0);*/

        PropertyValuesHolder rotation5=PropertyValuesHolder.ofFloat("rotationX",0,180,180,0,0);
        PropertyValuesHolder rotation6=PropertyValuesHolder.ofFloat("rotationY", 0, 0, 180, 180, 0);
        animator=ObjectAnimator.ofPropertyValuesHolder(getTarget(), rotation6,rotation5);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setDuration(2500);
        animator.start();
    }

    private ObjectAnimator animator = null;
    public void showView()
    {
        if(animator == null)
            createAnimation();
        getTarget().setVisibility(View.VISIBLE);
        animator.start();
    }

    public void hideViewInvisible()
    {
        if(animator == null)
            createAnimation();
        getTarget().setVisibility(View.INVISIBLE);
        animator.cancel();
    }

    public void hideViewGone()
    {
        if(animator == null)
            createAnimation();
        getTarget().setVisibility(View.GONE);
        animator.cancel();
    }
}