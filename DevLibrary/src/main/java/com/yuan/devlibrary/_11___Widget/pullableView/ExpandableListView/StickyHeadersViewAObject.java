package com.yuan.devlibrary._11___Widget.pullableView.ExpandableListView;
/*****使用此黏沾性标题控件,其父类数据或则子类数据必须集成此类*****/
/******mTagObj变量:用于在快速滚动内容时,增加其顺畅性,保证不卡*****/
/******mIsRealData变量:当指定的父类(Key)所对应的子类集合(Value)没*/
/**有任何数据时,则无法显示此父类标题.所以我们给原本为空的子类集合*/
/**添加一个假数据,并且用mIsRealData变量标记,当其为true时,代表是真*/
/**实数据,那么我们走正常的显示流程.反之则是假数据,这时我们便通过始/
/**终不展开这个父类的子类视图的办法去达到只显示其父类视图的效果,这/
/*******************也是mIsRealData变量的作用。*******************/
/**cloneObject()接口函数:为了适配器能够自动添加上面所说的假数据,**/
/**帮助适配器去显示只有父类标题的情况,因此其子类数据对象必须含有深/
/**度拷贝自身的方法,也只有这样适配器才能添加上真正独立的假数据,从*/
/**而才能实现只显示父类标题的情况,这也是cloneObject接口函数的作用*/
/**特别注意:::当所有父类(Key)所包含的子类集合(Value)都为空时,则需*/
/***********要我们手动添加仅仅一个子类数据即可,切记,切记**********/
public abstract class StickyHeadersViewAObject
{
    Object mTagObj = null;
    Boolean mIsRealData = true;

    public abstract Object cloneObject();

    public Object getTagObj()
    {
        return mTagObj;
    }

    public Boolean getIsRealData()
    {
        return mIsRealData;
    }

    public void setTagObj(Object tagObj)
    {
        mTagObj = tagObj;
    }

    public void setIsRealData(Boolean isRealData)
    {
        mIsRealData = isRealData;
    }
}