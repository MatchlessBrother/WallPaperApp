package com.yuan.devlibrary._11___Widget.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public abstract class BaseIndicatorController
{
    private View mTarget;

    public void setTarget(View target)
    {
        this.mTarget=target;
    }

    public View getTarget(){
        return mTarget;
    }

    public int getWidth()
    {
        return mTarget.getWidth();
    }

    public int getHeight()
    {
        return mTarget.getHeight();
    }

    public void postInvalidate()
    {
        mTarget.postInvalidate();
    }

    /******draw indicator what ever*******/
    /***********you want to draw**********/
    public abstract void draw(Canvas canvas,Paint paint);

    /***create animation or animations***/
    /****and add to your indicator.******/
    public abstract void createAnimation();

    /**show your view and start anim**/
    public abstract void showView();

    /**invisible your view and stop anim**/
    public abstract void hideViewInvisible();

    /****gone your view and stop anim*****/
    public abstract void hideViewGone();
}