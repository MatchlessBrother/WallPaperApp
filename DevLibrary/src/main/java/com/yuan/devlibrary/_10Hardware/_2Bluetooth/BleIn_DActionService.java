package com.yuan.devlibrary._10Hardware._2Bluetooth;

import android.os.Handler;
import android.os.IBinder;
import android.app.Service;
import android.widget.Toast;
import android.content.Intent;

/******手机对远端Ble设备操作时对应的操作状态******/
public class BleIn_DActionService extends Service
{
    private BleIn_CServiceAction mServiceAction;
    /*******************正在搜索远端BLE设备中******************/
    public static final int ACTION_BEGINSEARCHBLEBT = 0x0001;

    /********************结束搜索远端BLE设备*******************/
    public static final int ACTION_FINISHSEARCHBLEBT = 0x0002;

    /*******************完成搜索远端BLE设备********************/
    public static final int ACTION_COMPLETESEARCHBLEBT = 0x0003;

    /*******************重新连接远端BLE设备********************/
    public static final int ACTION_RECONNECTBLEBT = 0x0004;

    /*******************正在连接远端BLE设备********************/
    public static final int ACTION_CONNECTINGBLEBT = 0x0005;

    /*******************已经连接远端BLE设备********************/
    public static final int ACTION_CONNECTEDBLEBT = 0x0006;

    /*******************连接远端BLE设备失败********************/
    public static final int ACTION_CONNECTEDFAILBLEBT = 0x0007;

    /**************正在断开手机与远端BLE设备的连接*************/
    public static final int ACTION_DISCONNECTINGBLEBT = 0x0008;

    /**************已经断开手机与远端BLE设备的连接*************/
    public static final int ACTION_DISCONNECTEDBLEBT = 0x0009;

    /**************断开手机与远端BLE设备的连接失败*************/
    public static final int ACTION_DISCONNECTEDFAILBLEBT = 0x000A;

    /*************正在读取远端BLE设备的一个特征属性************/
    public static final int ACTION_READINGBLEBTDATA = 0x000B;

    /*************已经读取远端BLE设备的一个特征属性************/
    public static final int ACTION_READEDBLEBTDATA = 0x000C;

    /*************正在写给远端BLE设备的一个特征属性************/
    public static final int ACTION_WRITINGBLEBTDATA = 0x000D;

    /*************已经写给远端BLE设备的一个特征属性************/
    public static final int ACTION_WRITEDBLEBTDATA = 0x000E;

    /*********已经读取远端BLE设备一个发生变化的特征属性********/
    public static final int ACTION_READEDBLEBTCHANGEDATA = 0x000F;

    /************正在计算手机与远端BLE设备之间的距离***********/
    public static final int ACTION_READINGBLEBTRSSI = 0x0010;

    /************计算完毕手机与远端BLE设备之间的距离***********/
    public static final int ACTION_READEDBLEBTRSSI = 0x0011;

    public void onCreate()
    {
        super.onCreate();
        mServiceAction = BleOut_BServiceAction.getInstance();
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if(mServiceAction == null)
            mServiceAction = BleOut_BServiceAction.getInstance();
        switch (intent.getIntExtra("ActionFlag",-1))
        {
            case ACTION_BEGINSEARCHBLEBT:mServiceAction.beginSearchBleBt();break;
            case ACTION_FINISHSEARCHBLEBT:mServiceAction.finishSearchBleBt();break;
            case ACTION_COMPLETESEARCHBLEBT:mServiceAction.completeSearchBleBt(intent.<BleIn_ADeviceBean>getParcelableArrayListExtra("ActionData"));break;
            case ACTION_RECONNECTBLEBT:mServiceAction.reConnectBleBt();break;
            case ACTION_CONNECTINGBLEBT:mServiceAction.connectingBleBt();break;
            case ACTION_CONNECTEDBLEBT:mServiceAction.connectedBleBt();break;
            case ACTION_CONNECTEDFAILBLEBT:mServiceAction.connectedFailBleBt();break;
            case ACTION_DISCONNECTINGBLEBT:mServiceAction.disConnectingBleBt();break;
            case ACTION_DISCONNECTEDBLEBT:mServiceAction.disConnectedBleBt();break;
            case ACTION_DISCONNECTEDFAILBLEBT:mServiceAction.disConnectedFailBleBt();break;
            case ACTION_READINGBLEBTDATA:mServiceAction.readingBleBtData();break;
            case ACTION_READEDBLEBTDATA:mServiceAction.readedBleBtData();break;
            case ACTION_WRITINGBLEBTDATA:mServiceAction.writingBleBtData();break;
            case ACTION_WRITEDBLEBTDATA:mServiceAction.writedBleBtData();break;
            case ACTION_READEDBLEBTCHANGEDATA:mServiceAction.readedBleBtChangeData();break;
            case ACTION_READINGBLEBTRSSI:mServiceAction.readingBleBtRssi();break;
            case ACTION_READEDBLEBTRSSI:mServiceAction.readedBleBtRssi();break;
            default:
            {
                new Handler().post(new Runnable()
                {
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(), "对不起，当前对Ble蓝牙设备的操作没有相应的响应事件！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void onDestroy()
    {
        mServiceAction = null;
        super.onDestroy();
        System.gc();
    }
}