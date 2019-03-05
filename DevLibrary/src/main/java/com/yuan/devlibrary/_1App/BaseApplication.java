package com.yuan.devlibrary._1App;

import java.util.Stack;
import java.util.Iterator;
import android.app.Activity;
import android.os.StrictMode;
import android.app.Application;

public class BaseApplication extends Application
{
    private Stack<Activity> mActivityStack;
    public static BaseApplication mApplication;
    public static void setApplication(BaseApplication application)
    {
        mApplication = application;
    }

    public void onCreate()
    {
        super.onCreate();
        setApplication(this);
        interceptANRException();
        mActivityStack = new Stack<Activity>();
    }

    /*****捕获致使程序崩溃的异常，并在友好提醒之后关闭应用，增加应用亲和度*****/
    public void interceptANRException()
    {
        BaseUncaughtExceptionHandler handler = BaseUncaughtExceptionHandler.getInstance();
        handler.registerUncaughtExceptionHandler(this);
    }

    /************************重置StrictMode(严格模式)的VmPolicy******************
     *使在7.0或更高版本的系统应用仍旧可以用file：样式的Uri在应用之间分享指定文件*/
    public void resetNewVmPolicy()
    {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
    }

    /************************恢复StrictMode(严格模式)的VmPolicy******************
     ***使在7.0或更高版本的系统应用不能使用file：样式的Uri在应用之间分享指定文件*/
    public void restoreOriginalVmPolicy()
    {}

    /*********记录应用开启的所有activity，从而实现程序的完全关闭(完全退出)*******/
    public synchronized void addActivity(Activity activity)
    {
        if (mActivityStack == null)
            return;
        if (!hasActivity(activity.getClass().getSimpleName()))
            mActivityStack.add(activity);
    }

    /******************************获取最近使用的Activity************************/
    public Activity getCurrentActivity()
    {
        if (mActivityStack == null)
            return null;

        if (mActivityStack.size() != 0)
        {
            Activity activity = mActivityStack.get(mActivityStack.size() - 1);
            return activity;
        }
        return null;
    }

    /********************记录Activity的栈，判断是否包含此Activity****************/
    public boolean hasActivity(String activityName)
    {
        for (Activity activity : mActivityStack)
        {
            if (activity != null)
            {
                if (activity.getClass().getSimpleName().equals(activityName))
                    return true;
            }
        }
        return false;
    }

    /*******************************关闭所有Activity*****************************/
    public void finishAllActivity()
    {
        if (mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity)
            {
                iterator.remove();
                activity.finish();
            }
        }
        System.gc();
    }

    /*******************************移除所有Activity*****************************/
    public void removeAllActivity()
    {
        if (mActivityStack == null)
            return;
        mActivityStack.clear();
        System.gc();
    }

    /**************************根据ActivityName关闭此Activity********************/
    public void finishActivity(String activityName)
    {
        if (mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity && activity.getClass().getSimpleName().equals(activityName))
            {
                iterator.remove();
                activity.finish();
            }
        }
    }

    /**************************根据ActivityName移除此Activity********************/
    public void removeActivity(String activityName)
    {
        if (mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity && activity.getClass().getSimpleName().equals(activityName))
            {
                iterator.remove();
            }
        }
    }

    /************根据ActivityName关闭除该Activity外的其他所有Activity************/
    public void finishAllActivityExcept(String activityName)
    {
        if (mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity && (!activity.getClass().getSimpleName().equals(activityName)))
            {
                iterator.remove();
                activity.finish();
            }
        }
    }

    /************根据ActivityName移除除该Activity外的其他所有Activity************/
    public void removeAllActivityExcept(String activityName)
    {
        if (mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity && (!activity.getClass().getSimpleName().equals(activityName)))
            {
                iterator.remove();
            }
        }
    }
}