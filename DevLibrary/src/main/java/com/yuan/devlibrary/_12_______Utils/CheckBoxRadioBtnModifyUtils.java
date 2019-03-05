package com.yuan.devlibrary._12_______Utils;

import android.content.Context;
import android.widget.CompoundButton;
import android.graphics.drawable.Drawable;

/**修改CheckBox,Radiobtn中自定义Btn大小的工具**/
public class CheckBoxRadioBtnModifyUtils
{
    /******************Position表示自定义Btn位于View的那个方向,1左，2上，3右，4下******************/
    public static final void setHavedDrawbleView(Context context, CompoundButton compoundButton, Integer DrawableRes, Integer widthSize, Integer heightSize, Integer position)
    {
        Drawable drawable = context.getResources().getDrawable(DrawableRes);
        drawable.setBounds(0,0,widthSize,heightSize);
        switch (position)
        {
            case 1:compoundButton.setCompoundDrawables(drawable,null,null,null);break;
            case 2:compoundButton.setCompoundDrawables(null,drawable,null,null);break;
            case 3:compoundButton.setCompoundDrawables(null,null,drawable,null);break;
            case 4:compoundButton.setCompoundDrawables(null,null,null,drawable);break;
        }
    }
}