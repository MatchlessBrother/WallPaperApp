package com.yuan.devlibrary._11___Widget.pullableView.ExpandableGridView;

/************使用此黏沾性标题控件,其子类元素数据必须集成此类************/
/*********mTagObj变量:用于记录每一个子类元素所属的父类元素的下标********/
/**这样就可以快速的获取每一个子类元素所对应的父类元素,从而优化显示速度**/
public class StickyHVGridViewAObject
{
    Object mTagObj = null;
    public Object getTagObj()
    {
        return mTagObj;
    }
    public void setTagObj(Object mTagObj)
    {
        this.mTagObj = mTagObj;
    }
}