package company.petrifaction.boss.ui.main.activity.view;

import java.util.List;
import android.view.View;
import java.util.ArrayList;
import android.graphics.Color;
import android.content.Intent;
import android.widget.TextView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import company.petrifaction.boss.R;
import android.support.v7.widget.RecyclerView;
import company.petrifaction.boss.base.BaseAct;
import android.graphics.drawable.ColorDrawable;
import company.petrifaction.boss.bean.PhotoData;
import company.petrifaction.boss.util.FavoriteTools;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v4.widget.SwipeRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import company.petrifaction.boss.adapter.FavoriteAdapter;
import com.yuan.devlibrary._12_______Utils.PromptBoxUtils;
import android.support.v7.widget.StaggeredGridLayoutManager;
import company.petrifaction.boss.ui.main.activity.view_v.FavoriteAct_V;
import company.petrifaction.boss.ui.main.activity.presenter.FavoritePresenter;
import static company.petrifaction.boss.ui.main.activity.view.PreviewPhotoAct.IMG_PATH;

public class FavoriteAct extends BaseAct implements FavoriteAct_V, View.OnClickListener
{
    private RecyclerView mRecyclerView;
    private FavoriteAdapter mFavoriteAdapter;
    private FavoritePresenter mFavoritePresenter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    public static final int MeedUpdateCurrentDatas = 0x0001;

    protected int setLayoutResID()
    {
        return R.layout.activity_favorite;
    }

    protected void initWidgets(View rootView)
    {
        super.initWidgets(rootView);
        setTitleContent("Favorite");
        setTitleBackVisible(View.VISIBLE);
        setTitleMoreIconVisible(View.VISIBLE);
        setTitleMoreDrawableIcon(R.drawable.icon_search);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mSwiperefreshlayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefreshlayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);/***************/
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        View view = LayoutInflater.from(this).inflate(R.layout.recyclerview_nodata,null);
        TextView textView = (TextView)view.findViewById(R.id.recyclerview_nodata);
        mFavoriteAdapter = new FavoriteAdapter(this,new ArrayList<PhotoData>());
        mFavoriteAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(mFavoriteAdapter);
        mFavoriteAdapter.setEnableLoadMore(false);
        mSwiperefreshlayout.setEnabled(true);
        textView.setText("No Datas");/*****/
        mFavoriteAdapter.setEmptyView(view);
    }

    protected void initDatas()
    {
        mFavoritePresenter = new FavoritePresenter();
        bindBaseMvpPresenter(mFavoritePresenter);
        mFavoritePresenter.getPhotoDatas();
    }

    protected void initLogic()
    {
        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                mFavoritePresenter.getPhotoDatas();
            }
        });

        mFavoriteAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            public void onItemClick(BaseQuickAdapter adapter,View view,int position)
            {
                Intent intent = new Intent(FavoriteAct.this,PreviewPhotoAct.class);/**/
                intent.putExtra(IMG_PATH,((PhotoData)adapter.getData().get(position)));
                startActivityForResult(intent,MeedUpdateCurrentDatas);
            }
        });

        mFavoriteAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            public void onItemChildClick(BaseQuickAdapter adapter,View view,int position)
            {
                switch(view.getId())
                {
                    case R.id.item_favorite:
                    case R.id.item_favorite_all:
                    {
                        final PhotoData photoData = (PhotoData)view.getTag();
                        if(!photoData.getFavorite())photoData.setFavorite(true);
                        mFavoriteAdapter.notifyItemChanged(photoData.getPosition());
                        PromptBoxUtils.showPromptDialog(FavoriteAct.this,getString(R.string.cancelcollectiontips),Color.argb(255,51,51,51),
                        36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.GONE,getString(R.string.cancelcollectiontipscontent),
                        Color.argb(255,102,102,102),30,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),"No",Color.argb(255,51,51,51),
                        36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.VISIBLE,"Yes",Color.argb(255,51,51,51),36,TypedValue.
                        COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.VISIBLE,true,new View.OnClickListener()
                        {
                            public void onClick(View view)
                            {
                                photoData.setFavorite(false);
                                FavoriteTools.removePhotoData(FavoriteAct.this,photoData);
                                mFavoriteAdapter.getData().remove(photoData.getPosition());
                                mFavoriteAdapter.notifyItemRemoved(photoData.getPosition());
                                mFavoriteAdapter.notifyItemRangeChanged(photoData.getPosition(),mFavoriteAdapter.getData().size());
                            }
                         },null,null);
                        break;
                    }
                }
            }
        });
    }

    public void finishRefresh()
    {
        mSwiperefreshlayout.setRefreshing(false);

    }

    protected void onTitleMoreIconClick()
    {
        Intent intent = new Intent(this,SearchAct.class);
        startActivityForResult(intent,MeedUpdateCurrentDatas);
    }

    public void refreshDatas(List<PhotoData> photoDatas)
    {
        mFavoriteAdapter.setNewData(new ArrayList<>(photoDatas));
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent intent)
    {
        super.onActivityResult(requestCode,resultCode,intent);
        switch(requestCode)
        {
            case MeedUpdateCurrentDatas:
            {
                List<PhotoData> favoritePhotoDatas = FavoriteTools.getPhotoDatas(this);
                if(favoritePhotoDatas.size() == 0)
                {
                    mFavoriteAdapter.getData().clear();
                }
                else
                {
                    for(int position = 0;position < mFavoriteAdapter.getData().size();position++)
                    {
                        for(int index = 0;index < favoritePhotoDatas.size();index++)
                        {
                            if(null != mFavoriteAdapter.getData().get(position) && null != mFavoriteAdapter.getData().get(position).getDownloadUrl() &&
                               null != favoritePhotoDatas.get(index) && null != favoritePhotoDatas.get(index).getDownloadUrl() && favoritePhotoDatas.
                               get(index).getDownloadUrl().trim().equals(mFavoriteAdapter.getData().get(position).getDownloadUrl().trim()) && !"".equals(
                               favoritePhotoDatas.get(index).getDownloadUrl().trim()) && !"".equals(mFavoriteAdapter.getData().get(position).getDownloadUrl().trim()))
                            {
                                mFavoriteAdapter.getData().get(position).setFavorite(true);
                                break;
                            }
                            if(index == favoritePhotoDatas.size() -1)
                            {
                                mFavoriteAdapter.getData().get(position).setFavorite(false);
                                mFavoriteAdapter.getData().remove(position);
                            }
                        }
                    }
                }
                mFavoriteAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}