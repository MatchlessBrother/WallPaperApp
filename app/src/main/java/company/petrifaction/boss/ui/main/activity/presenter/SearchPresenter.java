package company.petrifaction.boss.ui.main.activity.presenter;

import java.util.Arrays;
import java.util.Locale;
import company.petrifaction.boss.bean.PhotoData;
import company.petrifaction.boss.util.EncryptionTools;
import company.petrifaction.boss.bean.BaseReturnListData;
import company.petrifaction.boss.ui.base.BaseMvp_Presenter;
import company.petrifaction.boss.ui.base.BaseMvp_EntranceOfModel;
import static company.petrifaction.boss.network.NetFlags.SIGN_KEY;
import company.petrifaction.boss.ui.base.BaseMvp_LocalListCallBack;
import company.petrifaction.boss.ui.main.activity.model.SearchModel;
import company.petrifaction.boss.ui.main.activity.view_v.SearchAct_V;

public class SearchPresenter extends BaseMvp_Presenter<SearchAct_V>
{
    public void getPhotoDatasByCondition(String condition)
    {
        if(isAttachContextAndViewLayer())
        {
            BaseMvp_EntranceOfModel.
            requestDatas(SearchModel.class).
            putForm("name",condition.trim()).
            putForm("language",Locale.getDefault().getLanguage()).
            putForm("time",String.valueOf(System.currentTimeMillis())).
            putForm("key",EncryptionTools.md5(Locale.getDefault().getLanguage() + condition.trim() + System.currentTimeMillis() + SIGN_KEY)).
            convertForms().executeOfNet(getContext(),SearchModel.GetPhotoDatasByCondition,new BaseMvp_LocalListCallBack<BaseReturnListData<PhotoData>>(this)
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