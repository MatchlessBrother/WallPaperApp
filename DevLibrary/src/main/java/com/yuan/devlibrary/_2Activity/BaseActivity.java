package com.yuan.devlibrary._2Activity;

import android.os.Bundle;
import android.view.View;
import android.app.Activity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.app.AppCompatActivity;
import com.yuan.devlibrary._1App.BaseApplication;
import com.yuan.devlibrary._12_______Utils.ResourceUtils;

public abstract class BaseActivity extends AppCompatActivity
{
    protected View mRootView = null;
    protected Activity mActivity = this;

    /*****巧妙的把每个Acivity添加进BaseApplication的Stack栈中,以便完全关闭应用或则实现其他功能*****/
    protected void onCreate(Bundle savedInstanceState)
    {
        /**为了完美使用自定义的UI适配方案,因此必须隐藏系统自带的标题栏***/
        /***********也只有这样才能正确计算出Activity的显示高度***********/
        initStatusBarAddTitleBar();
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mRootView = ResourceUtils.generateView(this,setLayoutResID());
        BaseApplication.mApplication.addActivity(mActivity);
        setContentView(mRootView);
        initWidgets(mRootView);
        initDatas();
        initLogic();
    }

    /**************初始化界面状态栏和标题栏***********/
    protected abstract void initStatusBarAddTitleBar();

    /****************设置布局文件的函数***************/
    protected abstract int setLayoutResID();

    /********************初始化界面Ui*****************/
    protected abstract void initWidgets(View rootView);

    /******************初始化界面数据*****************/
    protected abstract void initDatas();

    /******************初始化界面逻辑*****************/
    protected abstract void initLogic();

    /*****巧妙的把每个Acivity从BaseApplication的Stack栈中移除,以便完全关闭应用或则实现其他功能*****/
    protected void onDestroy()
    {
        if(BaseApplication.mApplication.hasActivity(this.getClass().getSimpleName()))
            BaseApplication.mApplication.removeActivity(this.getClass().getSimpleName());
        mActivity =  null;
        mRootView =  null;
        super.onDestroy();
    }
}