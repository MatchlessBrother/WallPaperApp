package company.petrifaction.boss.ui.main.activity.model;

import android.content.Context;
import company.petrifaction.boss.util.FavoriteTools;
import company.petrifaction.boss.bean.BaseReturnListData;
import company.petrifaction.boss.ui.base.BaseMvp_PVModel;
import company.petrifaction.boss.ui.base.BaseMvp_LocalObjCallBack;
import company.petrifaction.boss.ui.base.BaseMvp_LocalListCallBack;

public class FavoriteModel extends BaseMvp_PVModel
{
    public static final int GetFavoritePhotoDatas = 0x0001;

    public void executeOfNet(Context context, int netRequestCode, BaseMvp_LocalObjCallBack localCallBack)
    {
        localCallBack.onStart();
        localCallBack.onFinish();
    }

    public void executeOfLocal(Context context,int localRequestCode,BaseMvp_LocalObjCallBack localCallBack)
    {
        localCallBack.onStart();
        localCallBack.onFinish();
    }

    public void executeOfNet(Context context, int netRequestCode, BaseMvp_LocalListCallBack localCallBack)
    {
        localCallBack.onStart();
        localCallBack.onFinish();
    }

    public void executeOfLocal(Context context,int localRequestCode,BaseMvp_LocalListCallBack localCallBack)
    {
        localCallBack.onStart();
        switch(localRequestCode)
        {
            case GetFavoritePhotoDatas:
            {
                BaseReturnListData baseReturnListData = new BaseReturnListData();/********/
                baseReturnListData.setCode("1");/*****************************************/
                baseReturnListData.setMsg("success");/************************************/
                baseReturnListData.setData(FavoriteTools.getPhotoDatas(context).toArray());
                localCallBack.onSuccess(baseReturnListData);/*****************************/
                break;
            }
        }
        localCallBack.onFinish();
    }
}