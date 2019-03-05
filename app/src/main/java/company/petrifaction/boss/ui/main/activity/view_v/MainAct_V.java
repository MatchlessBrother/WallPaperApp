package company.petrifaction.boss.ui.main.activity.view_v;

import java.util.List;
import company.petrifaction.boss.bean.StyleData;
import company.petrifaction.boss.ui.base.BaseMvp_View;

public interface MainAct_V extends BaseMvp_View
{
    void finishRefresh();
    void refreshDatas(List<StyleData> styleDatas);
}