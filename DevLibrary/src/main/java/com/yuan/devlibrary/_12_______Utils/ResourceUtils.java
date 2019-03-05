package com.yuan.devlibrary._12_______Utils;

import android.view.View;
import android.content.Context;
import android.view.LayoutInflater;
import android.graphics.drawable.Drawable;

/**快速获取系统资源的工具*/
public class ResourceUtils
{
    /**************根据Layout资源resID获取View***************/
    public static View generateView(Context context,int resID)
    {
        return LayoutInflater.from(context).inflate(resID, null);
    }

    /**************根据Color资源resID获取Color**************/
    public static int getColorRes(Context context,int resID)
    {
        return context.getResources().getColor(resID);
    }

    /**************根据String资源resID获取String**************/
    public static String getTextRes(Context context,int resID)
    {
        return context.getResources().getString(resID);
    }

    /***************根据Drawable资源resID获取Drawable***************/
    public static Drawable getDrawableRes(Context context, int resID)
    {
        return context.getResources().getDrawable(resID);
    }
}