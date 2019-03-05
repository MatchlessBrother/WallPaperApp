package company.petrifaction.boss.ui.main.activity.view;

import java.util.List;
import android.view.View;
import java.util.ArrayList;
import android.view.KeyEvent;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import company.petrifaction.boss.R;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.support.v7.widget.RecyclerView;
import company.petrifaction.boss.base.BaseAct;
import company.petrifaction.boss.bean.PhotoData;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SimpleItemAnimator;
import company.petrifaction.boss.util.FavoriteTools;
import com.chad.library.adapter.base.BaseQuickAdapter;
import company.petrifaction.boss.adapter.SearchAdapter;
import android.support.v7.widget.StaggeredGridLayoutManager;
import company.petrifaction.boss.ui.main.activity.view_v.SearchAct_V;
import company.petrifaction.boss.ui.main.activity.presenter.SearchPresenter;
import static company.petrifaction.boss.ui.main.activity.view.PreviewPhotoAct.IMG_PATH;

public class SearchAct extends BaseAct implements SearchAct_V, View.OnClickListener
{
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;
    private SearchPresenter mSearchPresenter;
    private SwipeRefreshLayout mSwiperefreshlayout;
    public static final int MeedUpdateCurrentDatas = 0x0001;

    protected int setLayoutResID()
    {
        return R.layout.activity_search;
    }

    protected void initWidgets(View rootView)
    {
        super.initWidgets(rootView);
        setTitleBackVisible(View.VISIBLE);
        setTitleMoreIconVisible(View.VISIBLE);
        setTitleBackDrawable(R.drawable.icon_love);
        setTitleMoreDrawableIcon(R.drawable.icon_searching);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mSwiperefreshlayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swiperefreshlayout);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);/***************/
        mEditText = (EditText)rootView.findViewById(R.id.titlebar_edittext);/******************/
        ((SimpleItemAnimator)mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        View view = LayoutInflater.from(this).inflate(R.layout.recyclerview_nodata,null);
        TextView textView = (TextView)view.findViewById(R.id.recyclerview_nodata);
        mSearchAdapter = new SearchAdapter(this,new ArrayList<PhotoData>());
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mSearchAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(mSearchAdapter);
        mSearchAdapter.setEnableLoadMore(false);
        mSwiperefreshlayout.setEnabled(true);
        textView.setText("No Datas");/***/
        mSearchAdapter.setEmptyView(view);
    }

    protected void initDatas()
    {
        mSearchPresenter=new SearchPresenter();
        bindBaseMvpPresenter(mSearchPresenter);
        mSearchPresenter.getPhotoDatasByCondition(mEditText.getText().toString().trim());
    }

    protected void initLogic()
    {
        mSwiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                mEditText.setText("");
                mSearchPresenter.getPhotoDatasByCondition("");
            }
        });

        mSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            public void onItemClick(BaseQuickAdapter adapter,View view,int position)
            {
                Intent intent = new Intent(SearchAct.this,PreviewPhotoAct.class);/****/
                intent.putExtra(IMG_PATH,((PhotoData)adapter.getData().get(position)));
                startActivityForResult(intent,MeedUpdateCurrentDatas);
            }
        });

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(TextView textView,int actionId,KeyEvent keyEvent)
            {
                if(actionId == EditorInfo.IME_ACTION_SEARCH)
                {
                    mSearchPresenter.getPhotoDatasByCondition(mEditText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        mSearchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            public void onItemChildClick(BaseQuickAdapter adapter,View view,int position)
            {
                switch(view.getId())
                {
                    case R.id.item_search_favority:
                    case R.id.item_search_favority_all:
                    {
                        PhotoData photoData = (PhotoData)view.getTag();
                        photoData.setFavorite(!photoData.getFavorite());
                        mSearchAdapter.notifyItemChanged(photoData.getPosition());
                        if(photoData.getFavorite())
                            FavoriteTools.addPhotoData(SearchAct.this,photoData);
                        else
                            FavoriteTools.removePhotoData(SearchAct.this,photoData);
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
        mSearchPresenter.getPhotoDatasByCondition(mEditText.getText().toString().trim());
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
        mSearchAdapter.setNewData(photoDatas);
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
                    for(int index = 0;index < mSearchAdapter.getData().size();index++)
                    {
                        mSearchAdapter.getData().get(index).setFavorite(false);
                    }
                }
                else
                {
                    for(int position = 0;position < mSearchAdapter.getData().size();position++)
                    {
                        for(int index = 0;index < favoritePhotoDatas.size();index++)
                        {
                            if(null != mSearchAdapter.getData().get(position) && null != mSearchAdapter.getData().get(position).getDownloadUrl() &&
                               null != favoritePhotoDatas.get(index) && null != favoritePhotoDatas.get(index).getDownloadUrl() && favoritePhotoDatas.
                               get(index).getDownloadUrl().trim().equals(mSearchAdapter.getData().get(position).getDownloadUrl().trim()) && !"".equals(
                               favoritePhotoDatas.get(index).getDownloadUrl().trim()) && !"".equals(mSearchAdapter.getData().get(position).getDownloadUrl().trim()))
                            {
                                mSearchAdapter.getData().get(position).setFavorite(true);
                                break;
                            }
                            if(index == favoritePhotoDatas.size() -1)
                            {
                                mSearchAdapter.getData().get(position).setFavorite(false);
                            }
                        }
                    }
                }
                mSearchAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}