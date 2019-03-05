package com.yuan.devlibrary._10Hardware._2Bluetooth;

import java.util.ArrayList;

/******这个接口用于外部操作BLE蓝牙设备所对应的响应事件******/
/******实现这个接口的类必须要以单例模式存在,以方便使用******/
public interface BleIn_CServiceAction
{
    /********正在搜索远端BLE设备********/
    public void beginSearchBleBt();
    /********结束搜索远端BLE设备********/
    public void finishSearchBleBt();
    /********完成搜索远端BLE设备********/
    public void completeSearchBleBt(ArrayList<BleIn_ADeviceBean> deviceList);
    /********重新连接远端BLE设备********/
    public void reConnectBleBt();
    /********正在连接远端BLE设备********/
    public void connectingBleBt();
    /********已经连接远端BLE设备********/
    public void connectedBleBt();
    /********连接远端BLE设备失败********/
    public void connectedFailBleBt();
    /******正在断开手机与远端BLE设备的连接******/
    public void disConnectingBleBt();
    /******已经断开手机与远端BLE设备的连接******/
    public void disConnectedBleBt();
    /******断开手机与远端BLE设备的连接失败******/
    public void disConnectedFailBleBt();
    /*****正在读取远端BLE设备的一个特征属性*****/
    public void readingBleBtData();
    /*****已经读取远端BLE设备的一个特征属性*****/
    public void readedBleBtData();
    /*****正在写给远端BLE设备的一个特征属性*****/
    public void writingBleBtData();
    /*****已经写给远端BLE设备的一个特征属性*****/
    public void writedBleBtData();
    /**已经读取远端BLE设备中一个发生变化的特征属性**/
    public void readedBleBtChangeData();
    /******正在计算手机与远端BLE设备之间的距离******/
    public void readingBleBtRssi();
    /******计算完毕手机与远端BLE设备之间的距离******/
    public void readedBleBtRssi();
}