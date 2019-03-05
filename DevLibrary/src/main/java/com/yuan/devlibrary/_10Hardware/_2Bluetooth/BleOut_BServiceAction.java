package com.yuan.devlibrary._10Hardware._2Bluetooth;

import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;

/******使用EventBus框架来提示->操作远端Ble蓝牙设备的不同进度******/
public class BleOut_BServiceAction implements BleIn_CServiceAction
{
    private static BleOut_BServiceAction mInstance;
    private BleOut_BServiceAction(){}

    /**************************以单例模式获取对象**********************/
    public synchronized static final BleOut_BServiceAction getInstance()
    {
        if(mInstance == null)
            mInstance = new BleOut_BServiceAction();
        return mInstance;
    }

    /**提示正在搜索远端BLE设备**/
    public void beginSearchBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.BeginSearchBleBtClazz());
    }

    /**提示结束搜索远端BLE设备**/
    public void finishSearchBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.FinishSearchBleBtClazz());
    }

    /**提示完成搜索远端BLE设备**/
    public void completeSearchBleBt(ArrayList<BleIn_ADeviceBean> deviceList)
    {
        BleOut_CResponseClass.CompleteSearchBleBtClazz csb = BleOut_CResponseClass.CompleteSearchBleBtClazz.getInstance();
        csb.setDeviceList(deviceList);
        EventBus.getDefault().post(csb);
    }

    /**提示重新连接远端BLE设备**/
    public void reConnectBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReConnectBleBtClazz());
    }

    /**提示正在连接远端BLE设备**/
    public void connectingBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ConnectingBleBtClazz());
    }

    /**提示已经连接远端BLE设备**/
    public void connectedBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ConnectedBleBtClazz());
    }

    /**提示连接远端BLE设备失败**/
    public void connectedFailBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ConnectedFailBleBtClazz());
    }

    /**提示正在断开手机与远端BLE设备的连接**/
    public void disConnectingBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.DisConnectingBleBtClazz());
    }

    /**提示已经断开手机与远端BLE设备的连接**/
    public void disConnectedBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.DisConnectedBleBtClazz());
    }

    /**提示断开手机与远端BLE设备的连接失败**/
    public void disConnectedFailBleBt()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.DisConnectedFailBleBtClazz());
    }

    /**提示正在读取远端BLE设备的一个特征属性**/
    public void readingBleBtData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadingBleBtDataClazz());
    }

    /**提示已经读取远端BLE设备的一个特征属性**/
    public void readedBleBtData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadedBleBtDataClazz());
    }

    /**提示正在写给远端BLE设备的一个特征属性**/
    public void writingBleBtData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.WritingBleBtDataClazz());
    }

    /**提示已经写给远端BLE设备的一个特征属性**/
    public void writedBleBtData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.WritedBleBtDataClazz());
    }

    /**提示已经读取远端BLE设备中一个发生变化的特征属性**/
    public void readedBleBtChangeData()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadedBleBtChangeDataClazz());
    }

    /**提示正在计算手机与远端BLE设备之间的距离**/
    public void readingBleBtRssi()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadingBleBtRssiClazz());
    }

    /**提示计算完毕手机与远端BLE设备之间的距离**/
    public void readedBleBtRssi()
    {
        EventBus.getDefault().post(new BleOut_CResponseClass.ReadedBleBtRssiClazz());
    }
}