package company.petrifaction.boss.adapter;

import java.util.List;
import java.util.Random;
import android.view.ViewGroup;
import android.util.TypedValue;
import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import company.petrifaction.boss.R;
import android.widget.LinearLayout;
import android.support.annotation.Nullable;
import company.petrifaction.boss.bean.StyleData;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class StyleAdapter extends BaseQuickAdapter<StyleData,BaseViewHolder>
{
    private Context mContext;

    public StyleAdapter(Context context, @Nullable List<StyleData> datas)
    {
        super(R.layout.item_style,datas);
        mContext = context;
    }

    protected void convert(BaseViewHolder helper,StyleData styleData)
    {
        RequestOptions options = new RequestOptions();
        options.error(R.drawable.icon_defaultimgs);
        options.placeholder(R.drawable.icon_defaultimgs);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.transforms(new CenterCrop(),new RoundedCornersTransformation
        ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,6,mContext.
        getResources().getDisplayMetrics()),0,RoundedCornersTransformation.CornerType.TOP));
        if(styleData.getDownloadImgHeight() == 0) styleData.setDownloadImgHeight((int)((mContext.getResources().getDisplayMetrics().widthPixels / 2) * (new Random().nextInt(7) / 20f + 1)));
        helper.getView(R.id.item_style_img).setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,styleData.getDownloadImgHeight()));/**************************/
        Glide.with(mContext).load(styleData.getDownloadUrl()).apply(options).into((ImageView)helper.getView(R.id.item_style_img));/*********************************************************/
        helper.setText(R.id.item_style_imgnumber, (null != styleData.getCount() && !"".equals(styleData.getCount().trim()) ? styleData.getCount().trim() : "0"));/**************************/
        helper.setText(R.id.item_style_description,(null != styleData.getDescription() && !"".equals(styleData.getDescription().trim()) ? styleData.getDescription().trim() : mContext.getResources().getString(R.string.nodescription)));
    }
}