package company.petrifaction.boss.adapter;

import java.util.List;
import java.util.Random;
import android.view.ViewGroup;
import android.content.Context;
import android.util.TypedValue;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import android.widget.LinearLayout;
import company.petrifaction.boss.R;
import android.support.annotation.Nullable;
import company.petrifaction.boss.bean.PhotoData;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PhotoAdapter extends BaseQuickAdapter<PhotoData,BaseViewHolder>
{
    private Context mContext;

    public PhotoAdapter(Context context,@Nullable List<PhotoData> datas)
    {
        super(R.layout.item_photo,datas);
        mContext = context;/************/
    }

    protected void convert(BaseViewHolder helper,PhotoData photoData)
    {
        photoData.setPosition(helper.getAdapterPosition());
        helper.addOnClickListener(R.id.item_photo_favority);
        helper.addOnClickListener(R.id.item_photo_favority_all);
        helper.getView(R.id.item_photo_favority).setTag(photoData);
        helper.getView(R.id.item_photo_favority_all).setTag(photoData);
        helper.setChecked(R.id.item_photo_favority,photoData.getFavorite());
        /******************************************************************/
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.icon_defaultimg);
        options.placeholder(R.drawable.icon_defaultimg);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.transforms(new CenterCrop(),new RoundedCornersTransformation
        ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,6,mContext.
        getResources().getDisplayMetrics()),0,RoundedCornersTransformation.CornerType.TOP));
        if(photoData.getDownloadImgHeight() == 0) photoData.setDownloadImgHeight((int)((mContext.getResources().getDisplayMetrics().widthPixels / 2) * (new Random().nextInt(7) / 20f + 1)));
        helper.getView(R.id.item_photo_img).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,photoData.getDownloadImgHeight()));/**************************/
        Glide.with(mContext).load(photoData.getDownloadThumbUrl()).apply(options).into((ImageView)helper.getView(R.id.item_photo_img));/****************************************************/
        helper.setText(R.id.item_photo_description,(null != photoData.getWidth() && !"".equals(photoData.getWidth().trim()) ?/**************************************************************/
        photoData.getWidth().trim() : "0") + "×" + (null != photoData.getHeight() && !"".equals(photoData.getHeight().trim()) ? photoData.getHeight().trim() : "0"));/*********************/
    }
}