package com.yuan.devlibrary._10Hardware._2Bluetooth;

import java.util.UUID;

/******操作远端BLE设备时需要的所有UUID******/
public class BleOut_AUuidData
{
    /**********************************搜索远端BLE设备中指定的服务UUID*******************************/
    public static final UUID SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    /******************************读取远端BLE设备发送给手机的特征属性UUID***************************/
    public static final UUID SERVICE_READUUID = UUID.fromString("0000fff7-0000-1000-8000-00805f9b34fb");
    /**********************************手机发送给远端BLE设备的特征属性UUID***************************/
    public static final UUID SERVICE_WRITEUUID = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb");
    /**********************************远端BLE设备中特征数据关于描述的UUID***************************/
    public static final UUID SERVICE_DESCRIPTORUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
}