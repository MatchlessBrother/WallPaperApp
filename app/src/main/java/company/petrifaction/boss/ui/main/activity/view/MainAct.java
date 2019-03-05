package company.petrifaction.boss.ui.main.activity.view;

import java.util.List;
import android.view.View;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.TextView;
import company.petrifaction.boss.R;
import android.view.LayoutInflater;
import android.support.v7.widget.RecyclerView;
import company.petrifaction.boss.base.BaseAct;
import company.petrifaction.boss.bean.StyleData;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v4.widget.SwipeRefreshLayout;
import company.petrifaction.boss.adapter.StyleAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import android.support.v7.widget.StaggeredGridLayoutManager;
import company.petrifaction.boss.ui.main.activity.view_v.MainAct_V;
import company.petrifaction.boss.ui.main.activity.presenter.MainPresenter;
import static company.petrifaction.boss.ui.main.activity.view.PhotoAct.CID;
import static company.petrifaction.boss.ui.main.activity.view.PhotoAct.TITLE;

public class MainAct extends BaseAct implements MainAct_V,View.OnClickListener
{
    private RecyclerView mRecyclerView;
    private StyleAdapter mStyleAdapter;
    private MainPresenter mMainPresenter;
    private SwipeRefreshLayout mSwiperefreshlayout;

    protected int setLayoutResID()
    {
        return R.layout.activity_main;
    }

    protected void initWidgets(View rootView)
    {
        super.initWidgets(rootView);
        setTitleBackVisible(View.VISIBLE);
        setTitleMoreIconVisible(View.VISIBLE);
        setTitleContent(getString(R.string.style));
        setTitleBackDrawable(R.drawable.icon_love);
        setTitleMoreDrawableIcon(R.drawable.icon_search);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mSwiperefreshlayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefreshlayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);/***************/
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        View view = LayoutInflater.from(this).inflate(R.layout.recyclerview_nodata,null);
        TextView textView = (TextView)view.findViewById(R.id.recyclerview_nodata);
        mStyleAdapter = new StyleAdapter(this,new ArrayList<StyleData>());
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mStyleAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mStyleAdapter);
        mStyleAdapter.setEnableLoadMore(false);
        mSwiperefreshlayout.setEnabled(true);
        textView.setText("No Datas");/***/
        mStyleAdapter.setEmptyView(view);
    }

    protected void initDatas()
    {
        mMainPresenter = new MainPresenter();
        bindBaseMvpPresenter(mMainPresenter);
        mMainPresenter.getStyleDatas();
    }

    protected void initLogic()
    {
        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                mMainPresenter.getStyleDatas();
            }
        });

        mStyleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                Intent intent = new Intent(MainAct.this,PhotoAct.class);
                intent.putExtra(CID,((StyleData)adapter.getData().get(position)).getId().trim());
                intent.putExtra(TITLE,null != ((StyleData)adapter.getData().get(position)).getDescription() &&
                !"".equals(((StyleData)adapter.getData().get(position)).getDescription().trim()) ? ((StyleData)
                adapter.getData().get(position)).getDescription().trim() : getResources().getString(R.string.wallpaper));
                startActivity(intent);
            }
        });
    }

    public void finishRefresh()
    {
        mSwiperefreshlayout.setRefreshing(false);

    }

    protected void onTitleBackClick()
    {
        Intent intent = new Intent(this,FavoriteAct.class);
        startActivity(intent);
    }

    protected void onTitleMoreIconClick()
    {
        Intent intent = new Intent(this,SearchAct.class);
        startActivity(intent);
    }

    public void refreshDatas(List<StyleData> styleDatas)
    {
        mStyleAdapter.setNewData(styleDatas);

    }
}