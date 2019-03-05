package com.yuan.devlibrary._10Hardware._2Bluetooth;

import java.util.ArrayList;

/**配合EventBus框架来提示->操作远端Ble蓝牙设备的不同进度*/
public class BleOut_CResponseClass
{
    /******开始搜索远端BLE设备的信号类******/
    public static class BeginSearchBleBtClazz
    {}

    /******结束搜索远端BLE设备的信号类******/
    public static class FinishSearchBleBtClazz
    {}

    /******完成搜索远端BLE设备的信号类******/
    public static class CompleteSearchBleBtClazz
    {
        private static CompleteSearchBleBtClazz mInstance;
        private ArrayList<BleIn_ADeviceBean> deviceList;

        private  CompleteSearchBleBtClazz()
        {

        }

        public synchronized static final CompleteSearchBleBtClazz getInstance()
        {
            if(mInstance == null)
                mInstance = new CompleteSearchBleBtClazz();
            return mInstance;
        }

        public ArrayList<BleIn_ADeviceBean> getDeviceList()
        {
            return deviceList;
        }

        public void setDeviceList(ArrayList<BleIn_ADeviceBean> deviceList)
        {
            this.deviceList = deviceList;
        }
    }

    /******重新连接远端BLE设备的信号类******/
    public static class ReConnectBleBtClazz
    {}

    /******正在连接远端BLE设备的信号类******/
    public static class ConnectingBleBtClazz
    {}

    /******已经连接远端BLE设备的信号类******/
    public static class ConnectedBleBtClazz
    {}

    /******连接远端BLE设备失败的信号类******/
    public static class ConnectedFailBleBtClazz
    {}

    /******正在断开手机与远端BLE设备的连接的信号类******/
    public static class DisConnectingBleBtClazz
    {}

    /******已经断开手机与远端BLE设备的连接的信号类******/
    public static class DisConnectedBleBtClazz
    {}

    /*********断开手机与远端BLE设备失败的信号类*********/
    public static class DisConnectedFailBleBtClazz
    {}

    /******正在读取远端BLE设备的一个特征属性的信号类******/
    public static class ReadingBleBtDataClazz
    {}

    /******已经读取远端BLE设备的一个特征属性的信号类******/
    public static class ReadedBleBtDataClazz
    {}

    /******正在写给远端BLE设备的一个特征属性的信号类******/
    public static class WritingBleBtDataClazz
    {}

    /******已经写给远端BLE设备的一个特征属性的信号类******/
    public static class WritedBleBtDataClazz
    {}

    /**已经读取远端BLE设备中一个发生变化的特征属性的信号类**/
    public static class ReadedBleBtChangeDataClazz
    {}

    /**正在计算手机与远端BLE设备之间的距离的信号类**/
    public static class ReadingBleBtRssiClazz
    {}

    /**计算完毕手机与远端BLE设备之间的距离的信号类**/
    public static class ReadedBleBtRssiClazz
    {}
}