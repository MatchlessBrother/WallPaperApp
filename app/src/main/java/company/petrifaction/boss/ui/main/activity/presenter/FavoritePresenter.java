package company.petrifaction.boss.ui.main.activity.presenter;

import java.util.Arrays;
import company.petrifaction.boss.bean.PhotoData;
import company.petrifaction.boss.bean.BaseReturnListData;
import company.petrifaction.boss.ui.base.BaseMvp_Presenter;
import company.petrifaction.boss.ui.base.BaseMvp_EntranceOfModel;
import company.petrifaction.boss.ui.base.BaseMvp_LocalListCallBack;
import company.petrifaction.boss.ui.main.activity.model.FavoriteModel;
import company.petrifaction.boss.ui.main.activity.view_v.FavoriteAct_V;

public class FavoritePresenter extends BaseMvp_Presenter<FavoriteAct_V>
{
    public void getPhotoDatas()
    {
        if(isAttachContextAndViewLayer())
        {
            BaseMvp_EntranceOfModel.
            requestDatas(FavoriteModel.class).
            executeOfLocal(getContext(),FavoriteModel.GetFavoritePhotoDatas,new BaseMvp_LocalListCallBack<BaseReturnListData<PhotoData>>(this)
            {
                public void onSuccess(BaseReturnListData<PhotoData> photoDatas)
                {
                    if(isAttachContextAndViewLayer())
                    {
                        getViewLayer().finishRefresh();
                        getViewLayer().refreshDatas(Arrays.asList(photoDatas.getData()));
                    }
                }

                public void onFailure(String msg)
                {
                    super.onFailure(msg);
                    if(isAttachContextAndViewLayer())
                    {
                        getViewLayer().finishRefresh();
                    }
                }

                public void onError(String msg)
                {
                    super.onError(msg);
                    if(isAttachContextAndViewLayer())
                    {
                        getViewLayer().finishRefresh();
                    }
                }
            });
        }
    }
}