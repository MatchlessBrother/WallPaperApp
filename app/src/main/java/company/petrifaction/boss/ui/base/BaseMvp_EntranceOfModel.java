package company.petrifaction.boss.ui.base;

import android.util.Log;

/**请求网络数据/本地数据的规范入口类*/
public  class  BaseMvp_EntranceOfModel
{
    public static final String TAG = BaseMvp_EntranceOfModel.class.getName();
    public static BaseMvp_PVModel requestDatas(Class clazz)
    {
        BaseMvp_PVModel model = null;
        try
        {
            if(clazz.newInstance() instanceof BaseMvp_PVModel)
                model = (BaseMvp_PVModel)clazz.newInstance();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        if(null == model)
            Log.e(TAG,clazz.getName() + " Isn't Instanceof " + BaseMvp_PVModel.class.getName());
        return model;
    }
}