package company.petrifaction.boss.ui.main.activity.presenter;

import java.util.Arrays;
import java.util.Locale;
import company.petrifaction.boss.bean.StyleData;
import company.petrifaction.boss.util.EncryptionTools;
import company.petrifaction.boss.bean.BaseReturnListData;
import company.petrifaction.boss.ui.base.BaseMvp_Presenter;
import company.petrifaction.boss.ui.base.BaseMvp_EntranceOfModel;
import company.petrifaction.boss.ui.main.activity.model.MainModel;
import static company.petrifaction.boss.network.NetFlags.SIGN_KEY;
import company.petrifaction.boss.ui.main.activity.view_v.MainAct_V;
import company.petrifaction.boss.ui.base.BaseMvp_LocalListCallBack;

public class MainPresenter extends BaseMvp_Presenter<MainAct_V>
{
    public void getStyleDatas()
    {
        if(isAttachContextAndViewLayer())
        {
            BaseMvp_EntranceOfModel.requestDatas(MainModel.class).
            putForm("language",Locale.getDefault().getLanguage()).
            putForm("time",String.valueOf(System.currentTimeMillis())).
            putForm("key",EncryptionTools.md5(Locale.getDefault().getLanguage() + System.currentTimeMillis() + SIGN_KEY)).
            convertForms().executeOfNet(getContext(),MainModel.GetStyleDatas,new BaseMvp_LocalListCallBack<BaseReturnListData<StyleData>>(this)
            {
                public void onSuccess(BaseReturnListData<StyleData> styleDatas)
                {
                    if(isAttachContextAndViewLayer())
                    {
                        getViewLayer().finishRefresh();
                        getViewLayer().refreshDatas(Arrays.asList(styleDatas.getData()));
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