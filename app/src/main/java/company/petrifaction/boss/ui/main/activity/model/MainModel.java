package company.petrifaction.boss.ui.main.activity.model;

import android.content.Context;
import io.reactivex.schedulers.Schedulers;
import company.petrifaction.boss.network.NetClient;
import company.petrifaction.boss.ui.base.BaseMvp_PVModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import company.petrifaction.boss.ui.base.BaseMvp_NetListCallBack;
import company.petrifaction.boss.ui.base.BaseMvp_LocalObjCallBack;
import company.petrifaction.boss.ui.base.BaseMvp_LocalListCallBack;

public class MainModel extends BaseMvp_PVModel
{
    public static final int GetStyleDatas = 0x0001;

    public void executeOfNet(Context context,int netRequestCode,BaseMvp_LocalObjCallBack localCallBack)
    {
        localCallBack.onStart();
        localCallBack.onFinish();
    }

    public void executeOfLocal(Context context,int localRequestCode,BaseMvp_LocalObjCallBack localCallBack)
    {
        localCallBack.onStart();
        localCallBack.onFinish();
    }

    public void executeOfNet(Context context,int netRequestCode,BaseMvp_LocalListCallBack localCallBack)
    {
        localCallBack.onStart();
        switch(netRequestCode)
        {
            case GetStyleDatas:NetClient.getInstance(context).getNetUrl().getStyleDatas(getForms()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseMvp_NetListCallBack(context,localCallBack));break;
        }
    }

    public void executeOfLocal(Context context,int localRequestCode,BaseMvp_LocalListCallBack localCallBack)
    {
        localCallBack.onStart();
        localCallBack.onFinish();
    }
}