package company.petrifaction.boss.ui.base;

import android.content.Context;

public class BaseMvp_Presenter<V extends BaseMvp_View>
{
    /***************************MVP中抽象的V层*************************/
    /********************Context主要用于满足网络请求*******************/
    private V mViewLayer;
    private Context mContext;

    /*************************获取MVP中抽象的V层***********************/
    public V getViewLayer()
    {
        return mViewLayer;
    }
    public Context getContext()
    {
        return mContext;
    }

    /****************使MVP中的P层与MVP中抽象的V层断开关联**************/
    public void detachContext()
    {
        mContext = null;
    }
    public void detachViewLayer()
    {
        mViewLayer = null;
    }
    public void detachContextAndViewLayout()
    {
        detachContext();
        detachViewLayer();
    }

    /***************判定MVP中的P层与MVP中抽象的V层是否关联*************/
    public boolean isAttachContext()
    {
        return mContext != null;
    }
    public boolean isAttachViewLayer()
    {
        return mViewLayer != null;
    }
    public boolean isAttachContextAndViewLayer()
    {
        return isAttachContext() && isAttachViewLayer();
    }

    /****************使MVP中的P层与MVP中抽象的V层建立关联**************/
    public void attachContext(Context context)
    {
        mContext = context;
    }
    public void attachViewLayer(V viewLayer)
    {
        mViewLayer = viewLayer;
    }
    public void attachContextAndViewLayer(Context context,V viewLayer)
    {
        attachContext(context);
        attachViewLayer(viewLayer);
    }
}