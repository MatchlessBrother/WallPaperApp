package com.yuan.devlibrary._10Hardware._2Bluetooth;

import android.os.Parcel;
import android.os.Parcelable;

/*****************远端BLE设备的信息*****************/
public class BleIn_ADeviceBean implements Parcelable
{
    /******远端BLE设备的名字******/
    private String mDeviceName;
    /******远端BLE设备的地址******/
    private String mDeviceAddress;

    public BleIn_ADeviceBean(){}

    public BleIn_ADeviceBean(Parcel parcel)
    {
        mDeviceName = parcel.readString();
        mDeviceAddress = parcel.readString();
    }

    public BleIn_ADeviceBean(String mDeviceName, String mDeviceAddress)
    {
        this.mDeviceName = mDeviceName;
        this.mDeviceAddress = mDeviceAddress;
    }

    public int hashCode()
    {
        return this.mDeviceName.hashCode()*6 + this.mDeviceAddress.hashCode()*8;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;

        if(obj != null && obj instanceof BleIn_ADeviceBean)
        {
            BleIn_ADeviceBean device = (BleIn_ADeviceBean)obj;
            if(mDeviceName.equals(device.getmDeviceName()) && mDeviceAddress.equals(device.getmDeviceAddress()))
            {
                return true;
            }
        }
        return false;
    }

    public String getmDeviceName()
    {
        return mDeviceName;
    }

    public String getmDeviceAddress()
    {
        return mDeviceAddress;
    }

    public void setmDeviceName(String mDeviceName)
    {
        this.mDeviceName = mDeviceName;
    }

    public void setmDeviceAddress(String mDeviceAddress)
    {
        this.mDeviceAddress = mDeviceAddress;
    }

    public int describeContents()
    {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mDeviceName);
        dest.writeString(mDeviceAddress);
    }

    public static final Parcelable.Creator<BleIn_ADeviceBean> CREATOR = new Parcelable.Creator<BleIn_ADeviceBean>()
    {
        public BleIn_ADeviceBean createFromParcel(Parcel parcel)
        {
            return new BleIn_ADeviceBean(parcel);
        }

        public BleIn_ADeviceBean[] newArray(int size)
        {
            return new BleIn_ADeviceBean[size];
        }
    };
}