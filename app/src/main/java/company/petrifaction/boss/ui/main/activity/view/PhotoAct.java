package company.petrifaction.boss.ui.main.activity.view;

import java.util.List;
import android.view.View;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.TextView;
import android.view.LayoutInflater;
import company.petrifaction.boss.R;
import android.support.v7.widget.RecyclerView;
import company.petrifaction.boss.base.BaseAct;
import company.petrifaction.boss.bean.PhotoData;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v4.widget.SwipeRefreshLayout;
import company.petrifaction.boss.util.FavoriteTools;
import com.chad.library.adapter.base.BaseQuickAdapter;
import company.petrifaction.boss.adapter.PhotoAdapter;
import android.support.v7.widget.StaggeredGridLayoutManager;
import company.petrifaction.boss.ui.main.activity.view_v.PhotoAct_V;
import company.petrifaction.boss.ui.main.activity.presenter.PhotoPresenter;
import static company.petrifaction.boss.ui.main.activity.view.PreviewPhotoAct.IMG_PATH;

public class PhotoAct extends BaseAct implements PhotoAct_V,View.OnClickListener
{
    private RecyclerView mRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private PhotoPresenter mPhotoPresenter;
    private SwipeRefreshLayout mSwiperefreshlayout;

    public static String CID = "CategoryId";
    public static final String TITLE = "PhotoActTitle";
    public static final int MeedUpdateCurrentDatas = 0x0001;

    protected int setLayoutResID()
    {
        return R.layout.activity_photo;
    }

    protected void initWidgets(View rootView)
    {
        super.initWidgets(rootView);
        setTitleBackVisible(View.VISIBLE);
        setTitleMoreIconVisible(View.VISIBLE);
        setTitleBackDrawable(R.drawable.icon_love);
        setTitleMoreDrawableIcon(R.drawable.icon_search);
        setTitleContent(getIntent().getStringExtra(TITLE));
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mSwiperefreshlayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefreshlayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);/***************/
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        View view = LayoutInflater.from(this).inflate(R.layout.recyclerview_nodata,null);
        TextView textView = (TextView)view.findViewById(R.id.recyclerview_nodata);
        mPhotoAdapter = new PhotoAdapter(this,new ArrayList<PhotoData>());
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mPhotoAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setEnableLoadMore(false);
        mSwiperefreshlayout.setEnabled(true);
        textView.setText("No Datas");/***/
        mPhotoAdapter.setEmptyView(view);
    }

    protected void initDatas()
    {
        mPhotoPresenter = new PhotoPresenter();
        bindBaseMvpPresenter(mPhotoPresenter);
        mPhotoPresenter.getPhotoDatas(getIntent().getStringExtra(CID));
    }

    protected void initLogic()
    {
        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                mPhotoPresenter.getPhotoDatas(getIntent().getStringExtra(CID));
            }
        });

        mPhotoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            public void onItemClick(BaseQuickAdapter adapter,View view,int position)
            {
                Intent intent = new Intent(PhotoAct.this,PreviewPhotoAct.class);/*****/
                intent.putExtra(IMG_PATH,((PhotoData)adapter.getData().get(position)));
                startActivityForResult(intent,MeedUpdateCurrentDatas);
            }
        });

        mPhotoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            public void onItemChildClick(BaseQuickAdapter adapter,View view,int position)
            {
                switch(view.getId())
                {
                    case R.id.item_photo_favority:
                    case R.id.item_photo_favority_all:
                    {
                        PhotoData photoData = (PhotoData)view.getTag();
                        photoData.setFavorite(!photoData.getFavorite());
                        mPhotoAdapter.notifyItemChanged(photoData.getPosition());
                        if(photoData.getFavorite())
                            FavoriteTools.addPhotoData(PhotoAct.this,photoData);
                        else
                            FavoriteTools.removePhotoData(PhotoAct.this,photoData);
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

    protected void onTitleBackClick()
    {
        Intent intent = new Intent(this,FavoriteAct.class);
        startActivityForResult(intent,MeedUpdateCurrentDatas);
    }

    protected void onTitleMoreIconClick()
    {
        Intent intent = new Intent(this,SearchAct.class);
        startActivityForResult(intent,MeedUpdateCurrentDatas);
    }

    public void refreshDatas(List<PhotoData> photoDatas)
    {
        List<PhotoData> favoritePhotoDatas = FavoriteTools.getPhotoDatas(this);
        for(int index = 0;index < favoritePhotoDatas.size();index++)
        {
            for(int position = 0;position < photoDatas.size();position++)
            {
                if(null != photoDatas.get(position) && null != photoDatas.get(position).getDownloadUrl() &&
                   null != favoritePhotoDatas.get(index) && null != favoritePhotoDatas.get(index).getDownloadUrl() &&
                   favoritePhotoDatas.get(index).getDownloadUrl().trim().equals(photoDatas.get(position).getDownloadUrl().trim()) &&
                   !"".equals(favoritePhotoDatas.get(index).getDownloadUrl().trim()) && !"".equals(photoDatas.get(position).getDownloadUrl().trim()))
                {
                    photoDatas.get(position).setFavorite(true);
                    break;
                }
            }
        }
        mPhotoAdapter.setNewData(photoDatas);
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
                    for(int index = 0;index < mPhotoAdapter.getData().size();index++)
                    {
                        mPhotoAdapter.getData().get(index).setFavorite(false);
                    }
                }
                else
                {
                    for(int position = 0;position < mPhotoAdapter.getData().size();position++)
                    {
                        for(int index = 0;index < favoritePhotoDatas.size();index++)
                        {
                            if(null != mPhotoAdapter.getData().get(position) && null != mPhotoAdapter.getData().get(position).getDownloadUrl() &&
                               null != favoritePhotoDatas.get(index) && null != favoritePhotoDatas.get(index).getDownloadUrl() && favoritePhotoDatas.
                               get(index).getDownloadUrl().trim().equals(mPhotoAdapter.getData().get(position).getDownloadUrl().trim()) && !"".equals(
                               favoritePhotoDatas.get(index).getDownloadUrl().trim()) && !"".equals(mPhotoAdapter.getData().get(position).getDownloadUrl().trim()))
                            {
                                mPhotoAdapter.getData().get(position).setFavorite(true);
                                break;
                            }
                            if(index == favoritePhotoDatas.size() -1)
                            {
                                mPhotoAdapter.getData().get(position).setFavorite(false);
                            }
                        }
                    }
                }
                mPhotoAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}