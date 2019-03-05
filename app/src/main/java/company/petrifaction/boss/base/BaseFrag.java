package company.petrifaction.boss.base;

import java.util.List;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import android.view.Gravity;
import android.graphics.Color;
import android.widget.TextView;
import android.util.TypedValue;
import com.bumptech.glide.Glide;
import android.widget.ImageView;
import android.view.WindowManager;
import android.widget.ImageButton;
import com.gyf.barlibrary.BarHide;
import company.petrifaction.boss.R;
import me.jessyan.autosize.AutoSize;
import android.graphics.BitmapFactory;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import android.graphics.drawable.ColorDrawable;
import com.bumptech.glide.request.RequestOptions;
import com.yuan.devlibrary._3Fragment.BaseFragment;
import company.petrifaction.boss.ui.base.BaseMvp_View;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.yuan.devlibrary._12_______Utils.PromptBoxUtils;
import company.petrifaction.boss.ui.base.BaseMvp_Presenter;
import com.yuan.devlibrary._11___Widget.promptBox.BaseDialog;
import com.yuan.devlibrary._11___Widget.promptBox.BaseProgressDialog;

public abstract class BaseFrag extends BaseFragment implements BaseMvp_View,View.OnClickListener
{
    protected View mTitleBar;
    protected TextView mTitleContent;
    protected TextView mTitleMoreFont;
    protected ImageButton mTitleBackBtn;
    protected ImmersionBar mImmersionBar;
    protected ImageButton mTitleMoreIcon;
    private List<BaseMvp_Presenter> mPresenters = new ArrayList<>();
    private static final String LOG_TAG = BaseFrag.class.getSimpleName();

    protected void initWidgets(View rootView)
    {
        mImmersionBar = ImmersionBar.with(this);
        if(null != rootView.findViewById(R.id.titlebar) &&
           null != rootView.findViewById(R.id.titlebar_back) &&
           null != rootView.findViewById(R.id.titlebar_content) &&
           null != rootView.findViewById(R.id.titlebar_morefont) &&
           null != rootView.findViewById(R.id.titlebar_moreicon))
        {
            mTitleBar = (View)rootView.findViewById(R.id.titlebar);
            mTitleBackBtn = (ImageButton) rootView.findViewById(R.id.titlebar_back);
            mTitleContent = (TextView) rootView.findViewById(R.id.titlebar_content);
            mTitleMoreFont = (TextView) rootView.findViewById(R.id.titlebar_morefont);
            mTitleMoreIcon = (ImageButton) rootView.findViewById(R.id.titlebar_moreicon);
            mImmersionBar.titleBar(mTitleBar).navigationBarColor(R.color.colorPrimary).navigationBarAlpha(0f)
            .hideBar(BarHide.FLAG_SHOW_BAR).navigationBarEnable(true).navigationBarWithKitkatEnable(true)/***/
            .statusBarDarkFont(false).flymeOSStatusBarFontColor(R.color.white).fullScreen(false).keyboardEnable(true)
            .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE).setOnKeyboardListener(new OnKeyboardListener()
            {
                public void onKeyboardChange(boolean status,int keyboardHeight)
                {
                    if(status)
                        Log.w(LOG_TAG, "SoftKeyBoard：Turn On！");
                    else
                        Log.w(LOG_TAG, "SoftKeyBoard：Turn On！");
                }
            }).init();
            mTitleBackBtn.setOnClickListener(this);
            mTitleMoreFont.setOnClickListener(this);
            mTitleMoreIcon.setOnClickListener(this);
        }
        AutoSize.autoConvertDensity(mActivity,720,true);
    }

    public void onResume()
    {
        super.onResume();
        AutoSize.autoConvertDensity(mActivity,720,true);
    }

    public void onDestroy()
    {
        if(null != mPresenters && mPresenters.size() > 0)
            for(BaseMvp_Presenter presenter : mPresenters)
                presenter.detachContextAndViewLayout();
        if(null != mImmersionBar)
            mImmersionBar.destroy();
        super.onDestroy();
    }

    public BaseApp getBaseApp()
    {
        return (BaseApp)mActivity.getApplication();

    }

    public void onClick(View view)
    {
        if(isUseDefaultTitleLine())
        {
            switch (view.getId())
            {
                case R.id.titlebar_back:onTitleBackClick();break;
                case R.id.titlebar_morefont:onTitleMoreFontClick();break;
                case R.id.titlebar_moreicon:onTitleMoreIconClick();break;
            }
        }
    }

    protected void onTitleBackClick()
    {
        mActivity.finish();

    }

    protected void onTitleMoreFontClick()
    {

    }

    protected void onTitleMoreIconClick()
    {

    }

    protected boolean isUseDefaultTitleLine()
    {
        return null != mTitleBar && null != mTitleBackBtn && null != mTitleContent && null !=mTitleMoreFont  && null != mTitleMoreIcon ? true : false;
    }

    protected void setTitleBack(int resource)
    {
        if(isUseDefaultTitleLine())
            mTitleBackBtn.setImageBitmap(BitmapFactory.decodeResource(getResources(),resource));
        else
            Log.i(LOG_TAG,"因未使用自定义标题栏,所以设置返回按钮图标失败！");
    }

    protected void setTitleContent(String title)
    {
        if (isUseDefaultTitleLine())
            mTitleContent.setText(null != title ? title.trim() : "");
        else
            Log.i(LOG_TAG,"因未使用自定义标题栏,所以设置标题内容失败！");
    }

    protected void setTitleBackVisible(int value)
    {
        if(isUseDefaultTitleLine())
            mTitleBackBtn.setVisibility(value);
        else
            Log.i(LOG_TAG,"因未使用自定义标题栏,所以隐藏/显示返回按钮失败！");
    }

    protected void setTitleMoreFont(String text)
    {
        if(isUseDefaultTitleLine())
            mTitleMoreFont.setText(null != text ? text.trim() : "");
        else
            Log.i(LOG_TAG,"因未使用自定义标题栏,所以设置更多文字内容失败！");
    }

    protected void setTitleMoreFontVisible(int value)
    {
        if(isUseDefaultTitleLine())
            mTitleMoreFont.setVisibility(value);
        else
            Log.i(LOG_TAG,"因未使用自定义标题栏,所以隐藏/显示更多文字内容失败！");
    }

    protected void setTitleMoreIcon(int resource)
    {
        if(isUseDefaultTitleLine())
            mTitleMoreIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),resource));
        else
            Log.i(LOG_TAG,"因未使用自定义标题栏,所以设置更多按钮图标失败！");
    }

    protected void setTitleMoreIconVisible(int value)
    {
        if(isUseDefaultTitleLine())
            mTitleMoreIcon.setVisibility(value);
        else
            Log.i(LOG_TAG,"因未使用自定义标题栏,所以隐藏/显示更多按钮失败！");
    }

    public void useGlideLoadImg(ImageView imageView, int drawable)
    {
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.defaultimage);
        options.placeholder(R.mipmap.defaultimage);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(this).load(drawable).apply(options).into(imageView);
    }

    public void useGlideLoadImg(ImageView imageView, String imgPath)
    {
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.defaultimage);
        options.placeholder(R.mipmap.defaultimage);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(this).load(imgPath).apply(options).into(imageView);
    }

    public void useGlideLoadImgForHead(ImageView imageView, int drawable)
    {
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.defaultheadimg);
        options.placeholder(R.mipmap.defaultheadimg);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(this).load(drawable).apply(options).into(imageView);
    }

    public void useGlideLoadImgForHead(ImageView imageView, String imgPath)
    {
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.defaultheadimg);
        options.placeholder(R.mipmap.defaultheadimg);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(this).load(imgPath).apply(options).into(imageView);
    }

    protected void bindBaseMvpPresenter(BaseMvp_Presenter baseMvpPresenter)
    {
        if(null != baseMvpPresenter)
        {
            mPresenters.add(baseMvpPresenter);
            baseMvpPresenter.attachContextAndViewLayer(mActivity,this);
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public void showToast(String str)
    {
        showToast(str,24);

    }

    public void showToast(String str,float strSize)
    {
        PromptBoxUtils.showToast(mActivity,str,strSize, TypedValue.COMPLEX_UNIT_MM, Gravity.CENTER,0);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public BaseProgressDialog showLoadingDialog()
    {
        return PromptBoxUtils.showLoadingDialog(mActivity,"请稍等",2,15,false,null);
    }

    public void dismissLoadingDialog(BaseProgressDialog progressDialog)
    {
        PromptBoxUtils.dismissLoadingDialog(progressDialog);
    }

    public BaseProgressDialog showLoadingDialog(boolean isCanceledOnTouchOutside,BaseProgressDialog.OnClickOutsideListener onClickOutsideListener)
    {
        return PromptBoxUtils.showLoadingDialog(mActivity,"请稍等",2,15,isCanceledOnTouchOutside,onClickOutsideListener);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public void dismissPromptDialog(BaseDialog baseDialog)
    {
        PromptBoxUtils.dismissPromptDialog(baseDialog);
    }

    public BaseDialog showPromptDialog(String titleStr,String contentStr,String falseStr,String trueStr)
    {
        return showPromptDialog(titleStr,contentStr,falseStr,trueStr,true,null,null,null);
    }

    public BaseDialog showPromptDialog(String titleStr,String contentStr,String falseStr,String trueStr,boolean isCanceledOnTouchOutside,View.OnClickListener trueOnClickListener,View.OnClickListener falseOnClickListener,BaseDialog.OnClickOutsideListener onClickOutsideListener)
    {
        return PromptBoxUtils.showPromptDialog(mActivity,titleStr,Color.argb(255,51,51,51), 36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),
                                       View.VISIBLE,contentStr, Color.argb(255,102,102,102),30,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),
                                             falseStr,Color.argb(255,51,51,51),36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.VISIBLE,
                                              trueStr,Color.argb(255,51,51,51),36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.VISIBLE,
                                                              isCanceledOnTouchOutside,trueOnClickListener,falseOnClickListener,onClickOutsideListener);
    }
}