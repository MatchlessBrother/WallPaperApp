package company.petrifaction.boss.ui.base;

import android.view.View;
import company.petrifaction.boss.base.BaseApp;
import com.yuan.devlibrary._11___Widget.promptBox.BaseDialog;
import com.yuan.devlibrary._11___Widget.promptBox.BaseProgressDialog;

public interface BaseMvp_View
{
    /**************获取Application**********/
    BaseApp getBaseApp();

    /************显示Toast提示框************/
    void showToast(String str);

    /************显示Toast提示框************/
    void showToast(String str,float strSize);

    /**********************************************************************************************/
    /**********************************************************************************************/

    /**显示加载进度框*/
    BaseProgressDialog showLoadingDialog();

    /**隐藏加载进度框*/
    void dismissLoadingDialog(BaseProgressDialog progressDialog);

    /**显示加载进度框*/
    BaseProgressDialog showLoadingDialog(boolean isCanceledOnTouchOutside,BaseProgressDialog.OnClickOutsideListener onClickOutsideListener);

    /**********************************************************************************************/
    /*************************************隐藏默认提示框*******************************************/
    void dismissPromptDialog(BaseDialog baseDialog);

    /**显示默认提示框，参数含义自己根据名字看，字体大小值默认以Sp为准，颜色属性值默认以动态Color生*
     ********成法为准,背景属性值默认以动态ColorDrawable生成法为准,其余则按照普通情况使用即可*******/
    public BaseDialog showPromptDialog(String titleStr,String contentStr,String falseStr,String trueStr,boolean isCanceledOnTouchOutside,View.OnClickListener trueOnClickListener,View.OnClickListener falseOnClickListener,BaseDialog.OnClickOutsideListener onClickOutsideListener);
}