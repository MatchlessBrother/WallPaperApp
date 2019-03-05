package com.yuan.devlibrary._1App;

import java.io.File;
import java.util.Date;
import java.io.Writer;
import android.util.Log;
import android.os.Looper;
import android.view.View;
import java.io.PrintWriter;
import java.io.IOException;
import android.view.Gravity;
import java.io.StringWriter;
import com.yuan.devlibrary.R;
import android.widget.TextView;
import android.app.AlertDialog;
import java.io.FileOutputStream;
import android.view.WindowManager;
import java.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.util.DisplayMetrics;
import java.io.FileNotFoundException;
import com.yuan.devlibrary._12_______Utils.MemoryUtils;

public class BaseUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler
{
    private static BaseUncaughtExceptionHandler mHandler;
    private        BaseApplication              mApplication;
    private        String      mExceptionalFilesBestStorePath;

    private BaseUncaughtExceptionHandler()
    {

    }

    public static BaseUncaughtExceptionHandler getInstance()
    {
        if (mHandler == null)
            mHandler = new BaseUncaughtExceptionHandler();
        return mHandler;
    }

    public void registerUncaughtExceptionHandler(BaseApplication application)
    {
        mApplication = application;
        Thread.setDefaultUncaughtExceptionHandler(mHandler);
        mExceptionalFilesBestStorePath = MemoryUtils.getBestFilesPath(mApplication) +  File.separator + "exceptionalLogs";
    }

    public void uncaughtException(Thread thread, Throwable ex)
    {
       /**把异常的详细信息打印在控制台和Logcat面板上*/
       System.out.println(getParticularInfos(ex));
       Log.e("ProgramCrashes", getParticularInfos(ex));
       createExceptionalReportFile(getParticularInfos(ex));

       new Thread(new Runnable()
        {
            public void run()
            {
                Looper.prepare();
                /**这是简略的崩溃提示框*******************************************************************************************************************************************************/
                showCrashDialog("啊哦，网络发生异常啦！请重新启动程序",R.color.black,"知道了", R.color.crashdialog_btnstrcolor);
                /**这是详细的崩溃提示框*******************************************************************************************************************************************************/
                //showCrashDialog("异常提示：",R.color.defaultwhite,R.color.crashdialog_titlebgcolor, "亲，网络发生异常啦！请您重新启动程序。\n由此给您带来的不便请敬请谅解，谢谢。", R.color.defaultblack,"知道了", R.color.crashdialog_btnstrcolor);
                Looper.loop();
            }
        }).start();
    }

    /********************************获取程序崩溃异常的详细信息，以便准确捕获问题所在，初步感觉异常的封装是一层一层的，要逐步解封才能得到最完善和最准确的崩溃信息详情****************************/
    private String getParticularInfos(Throwable ex)
    {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        String result = writer.toString();
        return result;
    }

    public void createExceptionalReportFile(String exceptionStr)
    {
        File exceptionalFile = new File(mExceptionalFilesBestStorePath);
        if(!exceptionalFile.exists())
            exceptionalFile.mkdirs();

        exceptionalFile = new File(mExceptionalFilesBestStorePath + File.separator + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) +".txt");
        FileOutputStream exceptionalFileOutputStream = null;
        try
        {
            exceptionalFileOutputStream = new FileOutputStream(exceptionalFile);
            exceptionalFileOutputStream.write(exceptionStr.getBytes());
            exceptionalFileOutputStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**************************************************************程序崩溃时弹出的提示框,最好根据当前应用设置合适的主题色以便适配当前应用*******************************************************/
    private void showCrashDialog(final String contentStr,final int contentStrColor,final String btnStr,final int btnStrColor)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(mApplication.getCurrentActivity()).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        View view = LayoutInflater.from(mApplication.getCurrentActivity()).inflate(R.layout.inflater_crashdialogdefaultone, null);
        alertDialog.setContentView(view);
        TextView content = view.findViewById(R.id.crashdialog_content);
        content.setText(contentStr.trim());
        content.setTextColor(mApplication.getCurrentActivity().getResources().getColor(contentStrColor));
        TextView complete = view.findViewById(R.id.crashdialog_complete);
        complete.setText(btnStr.trim());
        complete.setTextColor(mApplication.getCurrentActivity().getResources().getColor(btnStrColor));
        complete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
                mApplication.finishAllActivity();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mApplication.getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = displayMetrics.widthPixels*2/3;
        params.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(params);
    }

    /**************************************************************程序崩溃时弹出的提示框,最好根据当前应用设置合适的主题色以便适配当前应用*******************************************************/
    private void showCrashDialog(final String titleStr,final int titleStrColor,final int titleBgColor,final String contentStr,final int contentStrColor,final String btnStr,final int btnStrColor)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(mApplication.getCurrentActivity()).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        View view = LayoutInflater.from(mApplication.getCurrentActivity()).inflate(R.layout.inflater_crashdialogdefaulttwo, null);
        alertDialog.setContentView(view);
        TextView title = view.findViewById(R.id.crashdialog_title);
        title.setText(titleStr.trim());
        title.setBackgroundResource(titleBgColor);
        title.setTextColor(mApplication.getCurrentActivity().getResources().getColor(titleStrColor));
        TextView content = view.findViewById(R.id.crashdialog_content);
        content.setText(contentStr.trim());
        content.setTextColor(mApplication.getCurrentActivity().getResources().getColor(contentStrColor));
        TextView complete = view.findViewById(R.id.crashdialog_complete);
        complete.setText(btnStr.trim());
        complete.setTextColor(mApplication.getCurrentActivity().getResources().getColor(btnStrColor));
        complete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
                mApplication.finishAllActivity();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mApplication.getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = displayMetrics.widthPixels - 160;
        params.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(params);
    }
}