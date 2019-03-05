package company.petrifaction.boss.ui.main.activity.view;

import java.io.File;
import java.util.Map;
import java.util.List;
import android.net.Uri;
import android.Manifest;
import android.os.Build;
import java.util.HashMap;
import android.view.View;
import java.io.IOException;
import android.view.Gravity;
import android.graphics.Color;
import android.content.Intent;
import android.util.TypedValue;
import android.widget.CheckBox;
import android.widget.TextView;
import io.reactivex.Observable;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.webkit.MimeTypeMap;
import company.petrifaction.boss.R;
import android.provider.MediaStore;
import android.app.WallpaperManager;
import java.io.FileNotFoundException;
import android.widget.RelativeLayout;
import android.widget.CompoundButton;
import android.graphics.BitmapFactory;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import android.support.annotation.NonNull;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.DownloadTask;
import company.petrifaction.boss.base.BaseAct;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnShowRationale;
import android.graphics.drawable.ColorDrawable;
import company.petrifaction.boss.bean.PhotoData;
import permissions.dispatcher.PermissionRequest;
import com.github.chrisbanes.photoview.PhotoView;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import company.petrifaction.boss.util.MediaScanner;
import company.petrifaction.boss.util.FavoriteTools;
import com.yuan.devlibrary._12_______Utils.MemoryUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.yuan.devlibrary._12_______Utils.PromptBoxUtils;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener3;
import com.yuan.devlibrary._11___Widget.promptBox.BasePopupWindow;
import com.yuan.devlibrary._11___Widget.promptBox.BaseProgressDialog;

@RuntimePermissions
public class PreviewPhotoAct extends BaseAct implements View.OnClickListener
{
    private CheckBox mFavorite;
    private ImageView mDownload;
    private ImageView mWallPaper;
    private PhotoData mPhotoData;
    private FrameLayout mFrameLayout;
    private PhotoView mPreviewPhotoView;
    private RelativeLayout mRelativeLayout;
    private WallpaperManager mWallpaperManager;
    /***操作类型:0下载，1桌面壁纸，2锁屏壁纸，3所有壁纸***/
    private int mActionStyle = 0;
    private String mDownloadPath;
    private DownloadListener3 mDownloadListener;
    public static final String IMG_PATH = "imgPath";
    private Map<String,DownloadTask> mDownloadTaskMap;
    private BaseProgressDialog mDownloadProgressDialog;

    protected int setLayoutResID()
    {
        return R.layout.activity_previewphoto;
    }

    protected void initWidgets(View rootView)
    {
        super.initWidgets(rootView);
        setTitleMoreIconVisible(View.VISIBLE);
        setTitleMoreDrawableIcon(R.drawable.icon_set);
        setTitleContent(getString(R.string.previewwallpaper));
        mWallpaperManager = WallpaperManager.getInstance(this);
        mFavorite = (CheckBox)rootView.findViewById(R.id.img_favorite);
        mDownload = (ImageView)rootView.findViewById(R.id.img_download);
        mWallPaper = (ImageView)rootView.findViewById(R.id.img_wallpaper);
        mRelativeLayout = (RelativeLayout)rootView.findViewById(R.id.img_all);
        mPreviewPhotoView = (PhotoView)findViewById(R.id.previewphoto_photoview);
        mFrameLayout = (FrameLayout)rootView.findViewById(R.id.previewphoto_content);
    }

    protected void initDatas()
    {
        mPhotoData = (PhotoData)getIntent().getParcelableExtra(IMG_PATH);
        mDownloadTaskMap = new HashMap<String,DownloadTask>();
        mDownloadPath = MemoryUtils.getBestFilesPath(this);
        mDownloadListener = new DownloadListener3()
        {
            protected void started(@NonNull DownloadTask task)
            {
                mDownloadProgressDialog = showLoadingDialog();
            }

            protected void warn(@NonNull DownloadTask task)
            {
                showToast(getString(R.string.errorsencounteredindownloadingwallpaper));
                dismissLoadingDialog(mDownloadProgressDialog);
            }

            protected void canceled(@NonNull DownloadTask task)
            {
                showToast(getString(R.string.cancelwallpaperdownload));
                dismissLoadingDialog(mDownloadProgressDialog);
            }

            protected void completed(@NonNull DownloadTask task)
            {
                if(!task.getFile().exists())
                {
                    try
                    {
                        task.getFile().createNewFile();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                if(mActionStyle == 0)
                {
                    dismissLoadingDialog(mDownloadProgressDialog);
                    showToast(getString(R.string.successfuldownloadofwallpaper));
                }
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                {
                    if(null != task.getFile())
                        scanFileForOld(task.getFile());
                }
                else
                {
                    String filePath = task.getFile().getAbsolutePath();
                    if(null != filePath && !"".equals(filePath) && filePath.lastIndexOf(".") != -1)
                    {
                        scanFileForNew(filePath,MimeTypeMap.getSingleton().getMimeTypeFromExtension(filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length())));
                    }
                }
                if(mActionStyle == 0 && null != task.getFile() && task.getFile().exists())
                    task.getFile().delete();
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
                {
                    switch(mActionStyle)
                    {
                        case 1:
                        case 2:
                        case 3:
                        {
                            final String finalFileIntegrityPath = task.getFile().getAbsolutePath();
                            Observable.just("").map(new Function<String,String>()/*******/
                            {
                                public String apply(String noteStr) throws Exception
                                {
                                    mWallpaperManager.setBitmap(BitmapFactory.decodeFile(finalFileIntegrityPath));
                                    return noteStr;
                                }
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                            {
                                public void accept(String noteStr) throws Exception
                                {
                                    dismissLoadingDialog(mDownloadProgressDialog);
                                    showToast(getString(R.string.settingwallpapersuccessfully));
                                    if(new File(finalFileIntegrityPath).exists()) new File(finalFileIntegrityPath).delete();
                                }
                            });
                            break;
                        }
                    }
                }
                else
                {
                    final String finalFileIntegrityPath = task.getFile().getAbsolutePath();
                    Observable.just("").map(new Function<String,String>()/*******/
                    {
                        public String apply(String noteStr) throws Exception
                        {
                            switch(mActionStyle)
                            {
                                case 2:mWallpaperManager.setBitmap(BitmapFactory.decodeFile(finalFileIntegrityPath),null,true,WallpaperManager.FLAG_LOCK);break;
                                case 1:mWallpaperManager.setBitmap(BitmapFactory.decodeFile(finalFileIntegrityPath),null,true,WallpaperManager.FLAG_SYSTEM);break;
                                case 3:mWallpaperManager.setBitmap(BitmapFactory.decodeFile(finalFileIntegrityPath),null,true,WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);break;
                            }
                            return noteStr;
                        }
                    }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                    {
                        public void accept(String noteStr) throws Exception
                        {
                            switch(mActionStyle)
                            {
                                case 2:showToast(getString(R.string.settinglockwallpapersuccessfully));break;
                                case 1:showToast(getString(R.string.settingsystemwallpapersuccessfully));break;
                                case 3:showToast(getString(R.string.settingwallpapersuccessfully));break;
                            }
                            dismissLoadingDialog(mDownloadProgressDialog);
                            if(new File(finalFileIntegrityPath).exists()) new File(finalFileIntegrityPath).delete();
                        }
                    });
                }
            }

            protected void error(@NonNull DownloadTask task, @NonNull Exception e)
            {
                showToast(getString(R.string.failedtodownloadwallpaper));
                dismissLoadingDialog(mDownloadProgressDialog);
            }

            public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause)
            {

            }

            public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength)
            {

            }

            public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength)
            {

            }
        };
    }

    protected void initLogic()
    {
        List<PhotoData> favoritePhotoDatas = FavoriteTools.getPhotoDatas(this);
        for(int index = 0;index < favoritePhotoDatas.size();index++)
        {
            if(null != mPhotoData && null != mPhotoData.getDownloadUrl() && null != favoritePhotoDatas.get(index) && null != favoritePhotoDatas.get(index).getDownloadUrl() &&
               favoritePhotoDatas.get(index).getDownloadUrl().trim().equals(mPhotoData.getDownloadUrl().trim()) && !"".equals(favoritePhotoDatas.
               get(index).getDownloadUrl().trim()) && !"".equals(mPhotoData.getDownloadUrl().trim()))
            {
                mFavorite.setChecked(true);
                break;
            }
        }
        /***************************************************************************************/
        if(null != mPhotoData.getDownloadUrl() && !"".equals(mPhotoData.getDownloadUrl().trim()))
            useGlideLoadImg(mPreviewPhotoView,mPhotoData.getDownloadUrl());
        else
        {
            setTitleMoreIconVisible(View.GONE);
            mPreviewPhotoView.setVisibility(View.GONE);
        }
        mRelativeLayout.setVisibility(View.GONE);
        mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    mPhotoData.setFavorite(true);
                    FavoriteTools.addPhotoData(PreviewPhotoAct.this,mPhotoData);
                }
                else
                {
                    mPhotoData.setFavorite(false);
                    FavoriteTools.removePhotoData(PreviewPhotoAct.this,mPhotoData);
                }
            }
        });
        /***************************************************************************************/
        mDownload.setOnClickListener(this);
        mWallPaper.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        super.onClick(view);
        switch(view.getId())
        {
            case R.id.img_wallpaper:PreviewPhotoActPermissionsDispatcher.setWallpaperWithPermissionCheck(this);break;
            case R.id.img_download:PreviewPhotoActPermissionsDispatcher.downloadWallpaperWithPermissionCheck(this);break;
        }
    }

    protected void onTitleMoreIconClick()
    {
        super.onTitleMoreIconClick();
        if(mRelativeLayout.getVisibility() == View.VISIBLE)
        {
            mRelativeLayout.setVisibility(View.GONE);
        }
        else if(mRelativeLayout.getVisibility() == View.GONE)
        {
            mRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void downloadAndSetWallpaper()
    {
        String fileName = null;
        String fileIntegrityPath = null;
        DownloadTask downloadTask = null;
        if(mDownloadTaskMap.containsKey(mPhotoData.getDownloadUrl().trim()))
        {
            downloadTask = mDownloadTaskMap.get(mPhotoData.getDownloadUrl().trim());
            fileName = downloadTask.getFilename();/**********************/
            fileIntegrityPath = mDownloadPath + File.separator + fileName;
        }
        else
        {
            fileName = mPhotoData.getDownloadUrl().trim().substring(mPhotoData.getDownloadUrl().trim().lastIndexOf("/") + 1,mPhotoData.getDownloadUrl().trim().length());/*******************/
            downloadTask = new DownloadTask.Builder(mPhotoData.getDownloadUrl().trim(),mDownloadPath,fileName).setMinIntervalMillisCallbackProcess(30).setPassIfAlreadyCompleted(true).build();
            mDownloadTaskMap.put(mPhotoData.getDownloadUrl().trim(),downloadTask);
            fileIntegrityPath = mDownloadPath + File.separator + fileName;
        }
        StatusUtil.Status downloadStatus = StatusUtil.getStatus(downloadTask);
        if(downloadStatus == StatusUtil.Status.PENDING)
            showToast(getString(R.string.waitingfordownload));
        else if(downloadStatus == StatusUtil.Status.RUNNING)
            showToast(getString(R.string.downloadingwallpaper));
        else if(downloadStatus == StatusUtil.Status.COMPLETED)
        {
            if(mActionStyle == 0)
                showToast(getString(R.string.successfuldownloadofwallpaper));
            dismissLoadingDialog(mDownloadProgressDialog);/*****************/
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            {
                if(null != downloadTask.getFile())
                    scanFileForOld(downloadTask.getFile());
            }
            else
            {
                String filePath = downloadTask.getFile().getAbsolutePath();
                if(null != filePath && !"".equals(filePath) && filePath.lastIndexOf(".") != -1)
                {
                    scanFileForNew(filePath,MimeTypeMap.getSingleton().getMimeTypeFromExtension(filePath.substring(filePath.lastIndexOf(".") + 1,filePath.length())));
                }
            }
            if(mActionStyle == 0 && null != downloadTask.getFile() && downloadTask.getFile().exists())
                downloadTask.getFile().delete();
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            {
                switch(mActionStyle)
                {
                    case 1:
                    case 2:
                    case 3:
                    {
                        final String finalFileIntegrityPath = fileIntegrityPath;
                        final BaseProgressDialog progressDialog = showLoadingDialog();
                        Observable.just("").map(new Function<String,String>()/*******/
                        {
                            public String apply(String noteStr) throws Exception
                            {
                                mWallpaperManager.setBitmap(BitmapFactory.decodeFile(finalFileIntegrityPath));
                                return noteStr;
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                        {
                            public void accept(String noteStr) throws Exception
                            {
                                dismissLoadingDialog(progressDialog);
                                showToast(getString(R.string.settingwallpapersuccessfully));
                                if(new File(finalFileIntegrityPath).exists()) new File(finalFileIntegrityPath).delete();
                            }
                        });
                        break;
                    }
                }
            }
            else
            {
                final String finalFileIntegrityPath = fileIntegrityPath;
                final BaseProgressDialog progressDialog = showLoadingDialog();
                Observable.just("").map(new Function<String,String>()/*******/
                {
                    public String apply(String noteStr) throws Exception
                    {
                        switch(mActionStyle)
                        {
                            case 2:mWallpaperManager.setBitmap(BitmapFactory.decodeFile(finalFileIntegrityPath),null,true,WallpaperManager.FLAG_LOCK);break;
                            case 1:mWallpaperManager.setBitmap(BitmapFactory.decodeFile(finalFileIntegrityPath),null,true,WallpaperManager.FLAG_SYSTEM);break;
                            case 3:mWallpaperManager.setBitmap(BitmapFactory.decodeFile(finalFileIntegrityPath),null,true,WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);break;
                        }
                        return noteStr;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                {
                    public void accept(String noteStr) throws Exception
                    {
                        switch(mActionStyle)
                        {
                            case 1:showToast(getString(R.string.settingsystemwallpapersuccessfully));break;
                            case 2:showToast(getString(R.string.settinglockwallpapersuccessfully));break;
                            case 3:showToast(getString(R.string.settingwallpapersuccessfully));break;
                        }
                        dismissLoadingDialog(progressDialog);
                        if(new File(finalFileIntegrityPath).exists()) new File(finalFileIntegrityPath).delete();
                    }
                });
            }
        }
        else if(downloadStatus ==  StatusUtil.Status.UNKNOWN)
            downloadTask.enqueue(mDownloadListener);
        else if(downloadStatus == StatusUtil.Status.IDLE)
            downloadTask.enqueue(mDownloadListener);
    }

    private void scanFileForOld(File file)
    {
        try
        {
            MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),file.getAbsolutePath(),file.getName(),"beautiful picture!");
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);/*******/
            sendBroadcast(intent);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void scanFileForNew(String filePath,String mimeType)
    {
        try
        {
            MediaStore.Images.Media.insertImage(mActivity.getContentResolver(),filePath,filePath.substring(filePath.lastIndexOf("/") + 1,filePath.length()),"beautiful picture!");
            MediaScanner mediaScanner = new MediaScanner(this);
            String[] filesPath = new String[]{filePath};
            String[] mimesType = new String[]{mimeType};
            mediaScanner.scanFiles(filesPath,mimesType);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void downloadWallpaper()
    {
        mActionStyle = 0;
        downloadAndSetWallpaper();
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void setWallpaper()
    {
        View popView = getLayoutInflater().inflate(R.layout.layout_wallpaper,null);/***/
        TextView homeScreen = (TextView)popView.findViewById(R.id.wallpaper_homescreen);
        TextView lockScreen = (TextView)popView.findViewById(R.id.wallpaper_lockscreen);
        TextView bothScreen = (TextView)popView.findViewById(R.id.wallpaper_both);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
        {
            lockScreen.setVisibility(View.GONE);
            bothScreen.setVisibility(View.GONE);
        }
        /****************************************************************/
        final BasePopupWindow basePopupWindow = new BasePopupWindow(this);
        basePopupWindow.setOutsideTouchable(true);/**********************/
        basePopupWindow.setContentView(popView);/************************/
        basePopupWindow.showAtLocation(mFrameLayout,Gravity.CENTER,0,0);
        homeScreen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mActionStyle = 1;
                basePopupWindow.dismiss();
                downloadAndSetWallpaper();
            }
        });
        /******************************************************/
        lockScreen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mActionStyle = 2;
                basePopupWindow.dismiss();
                downloadAndSetWallpaper();
            }
        });
        /******************************************************/
        bothScreen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mActionStyle = 3;
                basePopupWindow.dismiss();
                downloadAndSetWallpaper();
            }
        });
    }

    @OnShowRationale({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void rationaleOfPermission(final PermissionRequest request)
    {
        PromptBoxUtils.showPromptDialog(this,getString(R.string.permissionprompt),Color.argb(255, 51, 51, 51), 36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.GONE,
        getString(R.string.rationaleofpermission),Color.argb(255, 102, 102, 102), 30,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),getString(R.string.cancel),Color.argb(255, 51, 51, 51), 36,TypedValue.
        COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.VISIBLE,getString(R.string.next),Color.argb(255, 51, 51, 51),36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.VISIBLE,false,new View.OnClickListener()
        {
            public void onClick(View v)
            {
                request.proceed();
            }
        },
        new View.OnClickListener()
        {
            public void onClick(View v)
            {
                request.cancel();
            }
        },null);
    }

    @OnPermissionDenied({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void rejectedOfPermission()
    {
        PromptBoxUtils.showPromptDialog(this,getString(R.string.permissionprompt),Color.argb(255,51,51,51),36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.GONE,
        getString(R.string.rejectedofpermission),Color.argb(255,102,102,102),30,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),"Invalid option!",Color.argb(255,51,51,51),36,TypedValue.
        COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.GONE,getString(R.string.isee),Color.argb(255,51,51,51),36,TypedValue.COMPLEX_UNIT_MM,new ColorDrawable(0xffffffff),View.VISIBLE,true,null,null,null);
    }

    @OnNeverAskAgain({Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE})
    public void foreverRejectedOfPermission()
    {
        PromptBoxUtils.showPermissionDialog(this,getString(R.string.foreverrejectedofpermission),getString(R.string.setting),null,null);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        PreviewPhotoActPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }
}