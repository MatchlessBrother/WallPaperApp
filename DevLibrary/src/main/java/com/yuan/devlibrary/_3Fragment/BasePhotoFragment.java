package com.yuan.devlibrary._3Fragment;

import java.io.File;
import java.util.List;
import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import java.util.LinkedList;
import android.view.Gravity;
import com.yuan.devlibrary.R;
import android.content.Intent;
import android.app.AlertDialog;
import android.widget.TextView;
import android.view.WindowManager;
import android.view.LayoutInflater;
import android.util.DisplayMetrics;
import android.provider.MediaStore;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import com.luck.picture.lib.PictureSelector;
import static android.app.Activity.RESULT_OK;
import android.support.v4.app.ActivityCompat;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.yuan.devlibrary._12_______Utils.MemoryUtils;
import com.yuan.devlibrary._12_______Utils.PromptBoxUtils;

public abstract class BasePhotoFragment extends BaseFragment
{
    /**********************图片选择工具**********************/
    private PictureSelector mPicturesSelector;
    /**********************图片存储路径**********************/
    protected String mPicturesCachePath;
    /*******************图片选择工具的样式*******************/
    protected Integer mPictureSelectorTheme;
    /*****************记录已经选取的图片集合*****************/
    protected List<LocalMedia> mSelectedMedias;
    /***************从图库中选取图片的选择模式***************/
    protected Integer mChoosePicturesMode;
    /***************从图库中选取图片的最大数量***************/
    protected Integer mChoosePicturesMaxSize;
    /***************从图库中选取图片的最小数量***************/
    protected Integer mChoosePicturesMinSize;
    /********裁剪图片时所采用的形状样式(0方形，1圆形)********/
    protected Integer mCropShapeStyle;
    /******************图库中是否显示Gif图片*****************/
    protected boolean mIsShowGif;
    /**************从图库中选取图片时是否带音乐**************/
    protected boolean mEnableSound;
    /***************从图库中选取图片后是否裁剪***************/
    protected boolean mEnableCrop;
    /*****************裁剪图片时是否显示工具栏***************/
    protected boolean mIsShowCropControls;
    /**************裁剪图片时是否可以移动裁剪框**************/
    protected boolean mIsDragCropBox;
    /*************裁剪图片时是否显示裁剪框的线条*************/
    protected boolean mIsShowCropFrame;
    /*************裁剪图片时是否显示裁剪框的边框*************/
    protected boolean mIsShowCropGrid;
    /**********裁剪图片后保存图片时是否采用异步模式**********/
    protected boolean mIsCropActionAsy;
    /**用户本次的操作对象是相机还是图库(true相机,false图库)**/
    private boolean mIsUserOperateCamera;
    /********************************************************/
    /*******表示用户在读写外置内存权限中缺少的具体权限*******/
    private String mCurrentLackPermission;
    /***********用户是否完全获取读写外置内存的权限***********/
    private boolean mIsGetStoragePermissions;
    /************读写外置内存权限所包含的具体权限************/
    private String[] mStoragePermissions = new String[]
    {
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /*********************************************************************************/
    /**************************从图库中选取图片的选择模式：单选***********************/
    protected static final int CHOOSE_PICTURES_MODE_SINGLE   = PictureConfig.SINGLE;
    /**************************从图库中选取图片的选择模式：多选***********************/
    protected static final int CHOOSE_PICTURES_MODE_MULTIPLE = PictureConfig.MULTIPLE;
    /*****************************裁剪图片时的形状样式：方形**************************/
    protected static final int CROP_PICTURES_SHAPE_SQUARE                    = 0x0000;
    /*****************************裁剪图片时的形状样式：圆形**************************/
    protected static final int CROP_PICTURES_SHAPE_CIRCULAR                  = 0x0001;
    /***************************获取最终图片路径的RequestCode值***********************/
    private static final int REQUEST_CODE_PICTURES_PATH                      = 0x0001;
    /*****************************获取相机权限的RequestCode值*************************/
    private static final int REQUEST_CODE_PERMISSION_CAMER                   = 0x0002;
    /************************获取外置内存读写权限的RequestCode值**********************/
    private static final int REQUEST_CODE_PERMISSION_MEMORY                  = 0x0003;


    /*****************************************初始化基本数据***************************************/
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mIsShowGif = false;
        mEnableCrop = true;
        mEnableSound = false;
        mSelectedMedias = null;
        mIsDragCropBox = false;
        mIsShowCropGrid = true;
        mIsShowCropFrame = true;
        mIsCropActionAsy = false;
        mIsShowCropControls = true;
        mChoosePicturesMinSize = 1;
        mIsUserOperateCamera = true;
        mIsGetStoragePermissions = false;
        mChoosePicturesMaxSize = Integer.MAX_VALUE;
        mCropShapeStyle = CROP_PICTURES_SHAPE_SQUARE;
        mPicturesSelector = PictureSelector.create(this);
        mChoosePicturesMode = CHOOSE_PICTURES_MODE_SINGLE;
        mPictureSelectorTheme = R.style.pictureSelector_default_style;
        mPicturesCachePath = MemoryUtils.getBestFilesPath(mActivity) + File.separator + "pictures";
        File cachePathFile = new File(mPicturesCachePath);if(!cachePathFile.exists()) cachePathFile.mkdirs();
    }

    /**************************************准备启动照相机获取图片**********************************/
    protected void readyStartCamera()
    {
        PackageManager packageManager = mActivity.getPackageManager();
        /*******************判定手机是否含有可用的摄像头**********************/
        if (packageManager.hasSystemFeature(packageManager.FEATURE_CAMERA))
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ComponentName componentName = intent.resolveActivity(packageManager);
            /*****************判定手机是否含有摄像头驱动软件******************/
            if(componentName != null)
            {
                /**************判断应用是否正确获取了照相的权限***************/
                int cameraPermissionState = ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), Manifest.permission.CAMERA);
                if(cameraPermissionState == PackageManager.PERMISSION_GRANTED)
                {
                    /*********判断应用是否正确获取了读写外置内存的权限**********/
                    for(int index = 0;index < mStoragePermissions.length;index++)
                    {
                        if(ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), mStoragePermissions[index]) == PackageManager.PERMISSION_DENIED)
                        {
                            mCurrentLackPermission = mStoragePermissions[index];
                            mIsGetStoragePermissions = false;
                            break;
                        }
                        if(index == mStoragePermissions.length - 1)
                        {
                            mIsGetStoragePermissions = true;
                            mCurrentLackPermission = "";
                            break;
                        }
                    }
                    if(mIsGetStoragePermissions)
                    {
                        /*********************启动照相机**********************/
                        /*******************/startCamera();/******************/
                        /*********************启动照相机**********************/
                    }
                    else
                    {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,mCurrentLackPermission))
                            ActivityCompat.requestPermissions(mActivity,mStoragePermissions,REQUEST_CODE_PERMISSION_MEMORY);
                        else
                            PromptBoxUtils.showPermissionDialog(mActivity,"亲，当前操作行为缺少读写系统内存的权限哟！请进入系统设置页面后查看并修改当前应用的相关权限后再继续使用，谢谢！","去设置",null,null);
                    }
                }
                else
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.CAMERA))
                        ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.CAMERA},REQUEST_CODE_PERMISSION_CAMER);
                    else
                        PromptBoxUtils.showPermissionDialog(mActivity,"亲，当前操作行为缺少使用相机的权限哟！请进入系统设置页面后查看并修改当前应用的相关权限后再继续使用，谢谢！","去设置",null,null);
                }
            }
            else
                PromptBoxUtils.showToast(mActivity,"亲，启动照相机失败了！\n因为摄像头缺少驱动哟！");
        }
        else
            PromptBoxUtils.showToast(mActivity,"亲，启动照相机失败了！\n因为无可用的摄像头哟！");
    }

    /**************************************正式启动照相机获取图片**********************************/
    protected void startCamera()
    {
        mPicturesSelector.openCamera(PictureMimeType.ofImage())
                .isCamera(true).compress(true).videoQuality(0)
                .isZoomAnim(true).previewEggs(true).videoMaxSecond(0)
                .imageSpanCount(4).isGif(mIsShowGif).isDragFrame(false)
                .scaleEnabled(true).previewImage(true).previewVideo(false)
                .rotateEnabled(true).sizeMultiplier(0.5f).recordVideoSecond(0)
                .enableCrop(mEnableCrop).cropCompressQuality(88).minimumCompressSize(100)
                .enablePreviewAudio(false).synOrAsy(!mIsCropActionAsy).theme(mPictureSelectorTheme)
                .openClickSound(mEnableSound).showCropGrid(mIsShowCropGrid).selectionMedia(mSelectedMedias)
                .showCropFrame(mIsShowCropFrame).videoMinSecond(Integer.MAX_VALUE).imageFormat(PictureMimeType.JPEG)
                .selectionMode(mChoosePicturesMode).maxSelectNum(mChoosePicturesMaxSize).minSelectNum(mChoosePicturesMinSize)
                .compressSavePath(mPicturesCachePath).freeStyleCropEnabled(mIsDragCropBox).setOutputCameraPath(mPicturesCachePath)
                .hideBottomControls(!mIsShowCropControls).circleDimmedLayer(mCropShapeStyle == CROP_PICTURES_SHAPE_CIRCULAR ? true : false).forResult(REQUEST_CODE_PICTURES_PATH);
    }

    /***************************************准备启动图库获取图片***********************************/
    protected void readyStartGallery()
    {
        /*********判断应用是否正确获取了读写外置内存的权限**********/
        for(int index = 0;index < mStoragePermissions.length;index++)
        {
            if(ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), mStoragePermissions[index]) == PackageManager.PERMISSION_DENIED)
            {
                mCurrentLackPermission = mStoragePermissions[index];
                mIsGetStoragePermissions = false;
                break;
            }
            if(index == mStoragePermissions.length - 1)
            {
                mIsGetStoragePermissions = true;
                mCurrentLackPermission = "";
                break;
            }
        }
        if(mIsGetStoragePermissions)
        {
            /********************启动图库选择图片******************/
            /*******************/startGallery();/******************/
            /********************启动图库选择图片******************/
        }
        else
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,mCurrentLackPermission))
                ActivityCompat.requestPermissions(mActivity,mStoragePermissions,REQUEST_CODE_PERMISSION_MEMORY);
            else
                PromptBoxUtils.showPermissionDialog(mActivity,"亲，当前操作行为缺少读写系统内存的权限哟！请进入系统设置页面后查看并修改当前应用的相关权限后再继续使用，谢谢！","去设置",null,null);
        }
    }

    /***************************************正式启动图库获取图片***********************************/
    protected void startGallery()
    {
        mPicturesSelector.openGallery(PictureMimeType.ofImage())
                .compress(true).videoQuality(0).isZoomAnim(true)
                .previewEggs(true).videoMaxSecond(0).imageSpanCount(4)
                .isGif(mIsShowGif).isDragFrame(false).scaleEnabled(true)
                .previewImage(true).previewVideo(false).rotateEnabled(true)
                .sizeMultiplier(0.5f).recordVideoSecond(0).enableCrop(mEnableCrop)
                .cropCompressQuality(88).minimumCompressSize(100).enablePreviewAudio(false)
                .synOrAsy(!mIsCropActionAsy).theme(mPictureSelectorTheme).openClickSound(mEnableSound)
                .showCropGrid(mIsShowCropGrid).selectionMedia(mSelectedMedias).showCropFrame(mIsShowCropFrame)
                .videoMinSecond(Integer.MAX_VALUE).imageFormat(PictureMimeType.JPEG).selectionMode(mChoosePicturesMode)
                .maxSelectNum(mChoosePicturesMaxSize).minSelectNum(mChoosePicturesMinSize).compressSavePath(mPicturesCachePath)
                .freeStyleCropEnabled(mIsDragCropBox).setOutputCameraPath(mPicturesCachePath).hideBottomControls(!mIsShowCropControls)
                .isCamera(ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(),Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED?true:false)
                .circleDimmedLayer(mCropShapeStyle == CROP_PICTURES_SHAPE_CIRCULAR ? true : false).forResult(REQUEST_CODE_PICTURES_PATH);/***与照相略有不同***/
    }

    /*******************************清除裁剪和压缩过程中产生的图片缓存*****************************/
    public void onDestroy()
    {
        PictureFileUtils.deleteCacheDirFile(mActivity);
        mStoragePermissions = null;
        mPicturesSelector = null;
        mSelectedMedias = null;
        super.onDestroy();
    }

    /****************************************获取最终图片的路径************************************/
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICTURES_PATH && null != data)
        {
            List<LocalMedia> selectedMedias = PictureSelector.obtainMultipleResult(data);
            LinkedList<String> selectedMediasPath = new LinkedList<String>();
            for(LocalMedia media : selectedMedias)
            {
                if(media.isCut() && media.isCompressed())
                    selectedMediasPath.add(media.getCompressPath().trim());
                else if(media.isCompressed())
                    selectedMediasPath.add(media.getCompressPath().trim());
                else if(media.isCut())
                    selectedMediasPath.add(media.getCutPath().trim());
                else
                    selectedMediasPath.add(media.getPath().trim());
            }
            setOnNewImgPathListener(selectedMediasPath);
        }
    }

    /***********************通过此回调函数:把新获取的照片路径返回给相关的Fragment******************/
    protected abstract void setOnNewImgPathListener(LinkedList<String> bitmapPaths);

    /*************************************显示获取图片方式的选择框**********************************
     ****************************fontTextSize：描述获取图片方式文字的大小***************************
     **************************fontTextSizeType：描述获取图片方式文字的类型*************************
     *************************fontTextColorRes：描述获取图片方式文字的颜色值***********************/
    protected void showSelectPicturesDialog(Float fontTextSize,Integer fontTextSizeType,Integer fontTextColorRes)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
        alertDialog.show();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.inflater_selectphotodialog,null);
        alertDialog.setContentView(view);
        TextView cameraTv = (TextView)view.findViewById(R.id.selectphotodialog_camera);
        TextView galleryTv = (TextView)view.findViewById(R.id.selectphotodialog_gallery);
        TextView cancelTv = (TextView)view.findViewById(R.id.selectphotodialog_cancel);
        cameraTv.setTextColor(fontTextColorRes);
        cameraTv.setTextSize(fontTextSizeType,fontTextSize);
        galleryTv.setTextColor(fontTextColorRes);
        galleryTv.setTextSize(fontTextSizeType,fontTextSize);
        cancelTv.setTextColor(fontTextColorRes);
        cancelTv.setTextSize(fontTextSizeType,fontTextSize);
        cameraTv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mIsUserOperateCamera = true;
                alertDialog.dismiss();
                readyStartCamera();

            }
        });
        galleryTv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mIsUserOperateCamera = false;
                alertDialog.dismiss();
                readyStartGallery();
            }
        });
        cancelTv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mIsUserOperateCamera = true;
                alertDialog.dismiss();
            }
        });
        Window window = alertDialog.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        window.setWindowAnimations(R.style.BottomOpenDialogAnim);
        window.getDecorView().setBackgroundResource(R.color.transparent);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = displayMetrics.widthPixels;
        params.height = alertDialog.getWindow().getAttributes().height;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    /************************************动态申请应用权限的回调函数********************************/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(REQUEST_CODE_PERMISSION_CAMER == requestCode && null != grantResults && grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0])
        {
            /*********判断应用是否正确获取了读写外置内存的权限**********/
            for(int index = 0;index < mStoragePermissions.length;index++)
            {
                if(ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(), mStoragePermissions[index]) == PackageManager.PERMISSION_DENIED)
                {
                    mCurrentLackPermission = mStoragePermissions[index];
                    mIsGetStoragePermissions = false;
                    break;
                }
                if(index == mStoragePermissions.length - 1)
                {
                    mIsGetStoragePermissions = true;
                    mCurrentLackPermission = "";
                    break;
                }
            }
            if(mIsGetStoragePermissions)
            {
                /*********************启动照相机**********************/
                /*******************/startCamera();/******************/
                /*********************启动照相机**********************/
            }
            else
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,mCurrentLackPermission))
                    ActivityCompat.requestPermissions(mActivity,mStoragePermissions,REQUEST_CODE_PERMISSION_MEMORY);
                else
                    PromptBoxUtils.showPermissionDialog(mActivity,"亲，当前操作行为缺少读写系统内存的权限哟！请进入系统设置页面后查看并修改当前应用的相关权限后再继续使用，谢谢！","去设置",null,null);
            }
        }
        else if(REQUEST_CODE_PERMISSION_MEMORY == requestCode && null != grantResults && grantResults.length > 0)
        {
            for(int index = 0;index < grantResults.length;index++)
            {
                if(PackageManager.PERMISSION_DENIED == grantResults[index])
                {
                    mIsGetStoragePermissions = false;
                    break;
                }
                if(index == grantResults.length - 1)
                {
                    mIsGetStoragePermissions = true;
                    break;
                }
            }
            if(mIsGetStoragePermissions)
            {
                if(mIsUserOperateCamera)
                {
                    /********************启动照相机********************/
                    /******************/startCamera();/****************/
                    /********************启动照相机********************/
                }
                else
                {
                    /******************启动图库选择图片****************/
                    /*****************/startGallery();/****************/
                    /******************启动图库选择图片****************/
                }
            }
        }
    }
}