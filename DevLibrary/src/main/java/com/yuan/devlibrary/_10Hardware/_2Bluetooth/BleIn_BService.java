package com.yuan.devlibrary._10Hardware._2Bluetooth;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.UUID;
import android.os.Build;
import android.os.Binder;
import android.os.Looper;
import android.view.View;
import java.util.HashMap;
import java.util.HashSet;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Window;
import java.util.ArrayList;
import android.app.Service;
import android.view.Gravity;
import android.widget.Toast;
import android.app.Activity;
import com.yuan.devlibrary.R;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import android.app.AlertDialog;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattCharacteristic;

/*********手机对远端Ble设备的所有操作********/
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleIn_BService extends Service
{
    private BluetoothManager                mBtManager;
    private BluetoothAdapter                mBtAdapter;
    private BluetoothAdapter.LeScanCallback mBtAdapterCallback;
    private BluetoothGatt                   mBtGatt;
    private BluetoothGattCallback           mBtGattCallback;
    private BluetoothGattService            mBtGattService;
    private BluetoothGattCharacteristic     mBtGattRCharacteristic;
    private BluetoothGattCharacteristic     mBtGattWCharacteristic;
    private BluetoothGattDescriptor         mBtGattCharacterDescriptor;
    private BleBinder                       mBleBinder;

    /****************************表示当前手机是否可以使用蓝牙功能************************/
    private Boolean                      mBtAvailable    = true;
    /**************************表示当前手机是否可以使用BLE蓝牙功能***********************/
    private Boolean                      mBleAvailable   = true;
    /***********表示当前手机每次扫描周边BLE设备默认允许花费的时间,以毫秒为单位***********/
    private Integer                      mScanningTime   = 8000;
    /**表示当前手机是否正处于扫描周边BLE设备的操作中,true表明正在扫描，false表明扫描结束*/
    private Boolean                      mScanningDevice = false;
    /************************表示当前手机扫描周边BLE设备时指定的设备UUID值***************/
    private UUID[]                       mScanDeviceUUIDs = new UUID[]{};
    /************************表示当前手机需要发现指定服务的UUID值************************/
    private UUID                         mServiceUUID = UUID.randomUUID();
    /************************表示当前手机需要读取指定特征的UUID值************************/
    private UUID                         mRCharacteristicUUID = UUID.randomUUID();
    /************************表示当前手机需要书写指定特征的UUID值************************/
    private UUID                         mWCharacteristicUUID = UUID.randomUUID();
    /************************表示当前手机是否和BLE远程设备已经连接***********************/
    private Boolean                      mConedDevice = false;
    /************************表示当前手机连接远端的BLE设备是手动连接还是自动连接*********/
    private Boolean                      mIsReConnect = false;
    /****************************表示当前手机请求连接远程BLE设备时指定的地址*************/
    private String                       mRequestDeviceAddress="";
    /****************************表示与当前手机处于连接状态的BLE远程设备的地址***********/
    private String                       mCurConedDeviceAddress="";
    /**表示当前手机已经扫描完毕周边的BLE设备,并且把扫描后得到的所有BLE设备都放在这个Set集合中*/
    private Set<BleIn_ADeviceBean> mScanDeviceSet = new HashSet<BleIn_ADeviceBean>();
    /**表示当前手机已经扫描完毕周边的BLE设备,并且把扫描后得到的所有BLE设备都放在这个Map集合中*/
    private Map<String, BluetoothDevice> mScanDeviceMap  = new HashMap<String, BluetoothDevice>();
    /**表示当前手机端处于搜索远端Ble设备的状态，持续搜索远端BLE设备直到取消或则完成此次搜索操作**/
    private final Handler                mSearchHandler  = new Handler();
    private final Runnable               mSearchRunnable = new Runnable()
    {
        public void run()
        {
            finishSearchBleBt(true);
        }
    };
    /***表示当前手机端处于重新连接设备的状态，重新连接设备直到连接设备成功才会停止当前连接操作***/
    private final Handler                      mReConDeviceHandler = new Handler();
    private final Runnable                     mReConDeviceRunnable = new Runnable()
    {
        public void run()
        {
            abnormalConnectBleBt();
            mReConDeviceHandler.postDelayed(this,30000);
        }
    };
    /*******表示手机和BLE设备断开连接时，确包断开操作的正确性，否则重新扫描并连接以往的设备*******/
    private final Handler                       mDisConDeviceHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            notifyDisConnectBleBt();
        }
    };
    /**因为Toast只在Act主线程中显示，因此我们可以通过Handler将一个自定义的线程运行于Act主线程之上，这样便可显示Toast*/
    private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    public void onCreate()
    {
        super.onCreate();
        mBleBinder = new BleBinder();
        mBtManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        /**使用BLE蓝牙通讯功能本来应该调用下面的方法获取BluetoothAdapter,但为使软件在不可用BLE功能的老手机上仍能正常运行*********
         * 则需要使用上面的方法,以便兼容老手机的蓝牙操作方式来获取BluetoothAdapter，防止软件在低版本手机上不能正常安装的问题。***
         * mBtAdapter = ((BluetoothManager)activity.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();*********************
         * 同时为了防止软件在BLE功能不可用时导致的程序崩溃，我们则需在使用BLE功能时自行判定是否可用后再继续逻辑操作便可避免问题***/
        if (mBtAdapter == null)
        {
            mBtAvailable = false;
            quickToast("亲，无法使用BLE蓝牙功能！\n因为当前手机没有可用的蓝牙设备哟！");
        }

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2 || ! getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            mBleAvailable = false;
            quickToast("亲，无法使用BLE蓝牙功能！\n因为当前手机的蓝牙设备不支持BLE通讯协议哟！");
        }

        if (mBtAvailable && mBleAvailable)
        {
            mBtAdapterCallback = new BluetoothAdapter.LeScanCallback()
            {
                /*******手机发起搜索周边BLE设备的时候，每搜索到一个设备就回调一次该方法，*******/
                /***所以我在该方法中搜集当次搜索到的所有设备，然后传递给Act显示，以便用户选择***/
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
                {
                    mScanDeviceMap.put(device.getAddress(), device);
                    mScanDeviceSet.add(new BleIn_ADeviceBean(device.getName(), device.getAddress()));
                }
            };

            mBtGattCallback = new BluetoothGattCallback()
            {
                /*********手机与从端设备连接或则断开时，关联状态发生改变，从而回调该方法********/
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
                {
                    super.onConnectionStateChange(gatt, status, newState);
                    /***表示成功的执行了一次GATT行为，至于执行了什么则需要自行判断才可得知***/
                    if (BluetoothGatt.GATT_SUCCESS == status)
                    {
                        /**********表示手机与从端BLE设备正式连接********/
                        if(BluetoothProfile.STATE_CONNECTED == newState)
                        {
                            try
                            {
                                Thread.sleep(800);
                                mConedDevice = true;
                                mBtGatt.discoverServices();
                                mCurConedDeviceAddress = mRequestDeviceAddress;
                                mReConDeviceHandler.removeCallbacks(mReConDeviceRunnable);
                                Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                                intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_CONNECTEDBLEBT);
                                startService(intent);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        /**********表示手机与从端BLE设备正式断开********/
                        else if(BluetoothProfile.STATE_DISCONNECTED == newState)
                        {
                            try
                            {
                                Thread.sleep(600);
                                mConedDevice = false;
                                mDisConDeviceHandler.sendEmptyMessage(0);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        /**********表示手机与从端BLE设备连接失败********/
                        if(BluetoothProfile.STATE_CONNECTED == newState)
                        {
                            if(!mIsReConnect)
                            {
                                Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                                intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_CONNECTEDFAILBLEBT);
                                startService(intent);
                                quickToast("亲，连接指定的BLE蓝牙设备失败了哟！");
                            }
                            else
                            {
                                quickToast("亲，重新连接BLE蓝牙设备失败了哟！马上为您再次连接......");
                            }
                        }
                        /**********表示手机与从端BLE设备断开失败********/
                        else if(BluetoothProfile.STATE_DISCONNECTED == newState)
                        {
                            Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                            intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_DISCONNECTEDFAILBLEBT);
                            startService(intent);
                            quickToast("亲，断开指定的BLE蓝牙设备失败了哟！");
                        }
                    }
                }

                /*********手机连接从端设备后，发现从端设备的Service数据时，则回调该方法*******/
                public void onServicesDiscovered(BluetoothGatt gatt, int status)
                {
                    super.onServicesDiscovered(gatt, status);
                    if(BluetoothGatt.GATT_SUCCESS == status)
                    {
                        if(mServiceUUID != null)
                        {
                            if (mBtGatt.getService(mServiceUUID) != null)
                            {
                                mBtGattService = mBtGatt.getService(mServiceUUID);
                                List<BluetoothGattCharacteristic> BtGattCharacteristics = mBtGattService.getCharacteristics();
                                if(BtGattCharacteristics != null)
                                {
                                    for (BluetoothGattCharacteristic characteristic : BtGattCharacteristics)
                                    {
                                        if (!setCharacteristicNotification(characteristic, true))
                                        {
                                            quickToast("亲,UUid为：" +characteristic.getUuid()+ "的特征属性，为其设置监听特征数据变化的功能失败了哟！");
                                        }
                                    }
                                }
                                else
                                    quickToast("亲,当前连接的BLE设备没有任何的特征数据哟！");
                            }
                            else
                                quickToast("亲，指定的BLE设备没有相关的Service哟！");
                        }
                        else
                            quickToast("亲，没有指明所要查找BLE设备中特定的Service哟！");
                    }
                    else
                        quickToast("亲，查找指定BLE设备的所有Service失败了哟！");
                }

                /*************手机连接从端设备后，手机读取从端设备发来的Characteristic数据,则回调该方法***********/
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
                {
                    super.onCharacteristicChanged(gatt, characteristic);
                    Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                    intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_READEDBLEBTCHANGEDATA);
                    startService(intent);
                }

                /*******************手机连接从端设备后，手机读取从端设备发来的Characteristic数据,则回调该方法**************/
                public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
                {
                    super.onCharacteristicRead(gatt, characteristic, status);
                    if(BluetoothGatt.GATT_SUCCESS == status)
                    {
                        Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                        intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_READEDBLEBTDATA);
                        startService(intent);
                    }
                    else
                        quickToast("亲，读取远端BLE设备发送给手机数据的操作失败了哟！");
                }

                /*******************手机连接从端设备后，手机发送Characteristic数据到从端设备,则回调该方法******************/
                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
                {
                    super.onCharacteristicWrite(gatt, characteristic, status);
                    if(BluetoothGatt.GATT_SUCCESS == status)
                    {
                        Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                        intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_WRITEDBLEBTDATA);
                        startService(intent);
                    }
                    else
                        quickToast("亲，发送命令给远端BLE设备的操作失败了哟！");
                }

                /***手机连接从端设备后，手机与从端设备的蓝牙信号强度，rssi值越小代表距离越近,信号越强。反之则越远,越弱。***/
                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status)
                {
                    super.onReadRemoteRssi(gatt, rssi, status);
                    if(BluetoothGatt.GATT_SUCCESS == status)
                    {
                        Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                        intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_READEDBLEBTRSSI);
                        startService(intent);
                    }
                    else
                        quickToast("亲，获取手机与BLE设备相隔距离的操作失败了哟！");
                }
            };
        }
    }

    /***********快速显示Toast标签的方法**********/
    private void quickToast(final String hintStr)
    {
        mMainThreadHandler.post(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), hintStr, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /******判定当前手机是否拥有蓝牙设备和蓝牙设备是否可用BLE通讯，全部满足返回true，否则返回false******/
    private Boolean baseIsAvailable()
    {
        if(mBtAvailable)
        {
            if(mBleAvailable)
                return true;
            else
                quickToast("亲，无法使用BLE蓝牙功能！\n因为当前手机的蓝牙设备不支持BLE通讯协议哟！");
        }
        else
            quickToast("亲，无法使用BLE蓝牙功能！\n因为当前手机没有可用的蓝牙设备哟！");
        return false;
    }

    /**************当用户扫描BLE设备时，发现蓝牙设备没有打开则提醒用户打开蓝牙设备************/
    private void openLocalBt(final Activity activity,final String notifyStr,final UUID[] uuids)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.show();
        View view = LayoutInflater.from(activity).inflate(R.layout.inflater_turnonbluetooth, null);
        alertDialog.setContentView(view);

        TextView hintTv = (TextView) view.findViewById(R.id.bta_hint);
        TextView offTv = (TextView) view.findViewById(R.id.bta_off);
        TextView onTv = (TextView) view.findViewById(R.id.bta_on);
        hintTv.setText(notifyStr);
        offTv.setText("拒绝");
        onTv.setText("允许");

        offTv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialog.dismiss();
            }
        });
        onTv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    mBtAdapter.enable();
                    alertDialog.dismiss();
                    Thread.sleep(800);
                    mBleBinder.startSearchDevices(activity, notifyStr, uuids);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Window window = alertDialog.getWindow();
        window.setWindowAnimations(R.style.CenterOpenDialogAnim);
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = displayMetrics.widthPixels / 5 * 4;
        params.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(params);
    }

    /******当用户误操作导致手机和BLE设备断开连接时，则自动进行再次扫描和连接以往的设备******/
    private void notifyDisConnectBleBt()
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        alertDialog.show();

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.inflater_reconnectblebt, null);
        alertDialog.setContentView(view);
        TextView hintTv = (TextView) view.findViewById(R.id.bta_hint);
        TextView offTv = (TextView) view.findViewById(R.id.bta_off);
        TextView reOnTv = (TextView) view.findViewById(R.id.bta_on);
        hintTv.setText("亲，手机与蓝牙设备的连接已经断开了哟！需要帮您重新连接吗？");
        offTv.setText("不用了");
        reOnTv.setText("重新连接吧！");
        offTv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mIsReConnect = false;
                mRequestDeviceAddress = "";
                mCurConedDeviceAddress = "";
                mBtGatt.close();
                mBtGatt = null;
                mBtGattService = null;
                mServiceUUID = UUID.randomUUID();
                mBtGattRCharacteristic = null;
                mBtGattWCharacteristic = null;
                mBtGattCharacterDescriptor = null;
                alertDialog.dismiss();
                Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_DISCONNECTEDBLEBT);
                startService(intent);
            }
        });

        reOnTv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                mIsReConnect = true;
                if (deviceBrandIsSamSung())
                    mBtGatt.connect();
                else
                {
                    mBtGatt.close();
                    mReConDeviceHandler.post(mReConDeviceRunnable);
                }
                Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_RECONNECTBLEBT);
                startService(intent);
                alertDialog.dismiss();
            }
        });

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        Window window = alertDialog.getWindow();
        window.setWindowAnimations(R.style.CenterOpenDialogAnim);
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        params.width = displayMetrics.widthPixels / 5 * 4;
        params.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(params);
    }

    /***********开始搜索可用的BLE蓝牙设备***********/
    private void beginSearchBleBt(final UUID[] uuids)
    {
        mSearchHandler.postDelayed(mSearchRunnable, mScanningTime);
        mScanningDevice = true;
        mScanDeviceSet.clear();
        mScanDeviceMap.clear();
        if(uuids != null && uuids.length != 0)
        {
            mScanDeviceUUIDs = uuids;
            mBtAdapter.startLeScan(mScanDeviceUUIDs, mBtAdapterCallback);
        }
        else
        {
            mScanDeviceUUIDs = new UUID[]{};
            mBtAdapter.startLeScan(mBtAdapterCallback);
        }
        Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
        intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_BEGINSEARCHBLEBT);
        startService(intent);
    }

    /*****************停止搜索可用的BLE蓝牙设备****************/
    private void finishSearchBleBt(final Boolean searchComplete)
    {
        mScanningDevice = false;
        mBtAdapter.stopLeScan(mBtAdapterCallback);
        mSearchHandler.removeCallbacks(mSearchRunnable);
        if(searchComplete)
        {
            Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_COMPLETESEARCHBLEBT);
            intent.putParcelableArrayListExtra("ActionData", new ArrayList<BleIn_ADeviceBean>(mScanDeviceSet));
            startService(intent);
        }
        else
        {
            Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
            intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_FINISHSEARCHBLEBT);
            startService(intent);
        }
    }

    /*********************根据地址连接指定的BLE蓝牙设备******************/
    private void connectBleBt(final String address,final UUID serviceUUID)
    {
        if(mScanDeviceMap.get(address) != null)
        {
            mRequestDeviceAddress = address;
            mServiceUUID = serviceUUID;
            mBtGatt = mScanDeviceMap.get(address).connectGatt(getApplicationContext(), false, mBtGattCallback);
            if(!mIsReConnect)
            {
                Intent intent = new Intent(BleIn_BService.this, BleIn_DActionService.class);
                intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_CONNECTINGBLEBT);
                startService(intent);
            }
        }
        else
        {
            if(!mIsReConnect)
                quickToast("亲，连接的BLE蓝牙设备不在连接范围内哟！");
            else
                quickToast("亲，可能重连的BLE蓝牙设备不在连接范围内哟！马上为您再次连接......");
        }
    }

    /********************当用户误操作导致手机与BLE设备断开连接后，则自动进行再次扫描和连接以往的设备*********************/
    /**进行再次扫描是为了判定此次断开连接的原因是否是在于BLE设备端（比如:关闭手换设备等等，这种情况则不需要再次连接。）**/
    private void abnormalConnectBleBt()
    {
        mBtAdapter.enable();
        new Handler().postDelayed(new Runnable()
        {
            public void run()
            {
                mScanningDevice = false;
                mBtAdapter.stopLeScan(mBtAdapterCallback);
                connectBleBt(mCurConedDeviceAddress, mServiceUUID);
            }
        }, 6000);

        mScanningDevice = true;
        mScanDeviceSet.clear();
        mScanDeviceMap.clear();
        if(mScanDeviceUUIDs != null && mScanDeviceUUIDs.length != 0)
            mBtAdapter.startLeScan(mScanDeviceUUIDs, mBtAdapterCallback);
        else
            mBtAdapter.startLeScan(mBtAdapterCallback);
    }

    /**手动停止手机重新连接之前的BLE设备**/
    private void stopAbnormalConnectBleBt()
    {
        mReConDeviceHandler.removeCallbacks(mReConDeviceRunnable);
        mBtGatt.disconnect();
        mBtGatt.close();
        mBtGatt = null;
        mIsReConnect = false;
        mRequestDeviceAddress = "";
        mCurConedDeviceAddress = "";
        mBtGattService = null;
        mServiceUUID = UUID.randomUUID();
        mBtGattRCharacteristic = null;
        mBtGattWCharacteristic = null;
        mBtGattCharacterDescriptor = null;
    }

    /**与指定的BLE蓝牙设备断开连接**/
    private void disConnectBleBt()
    {
       if(mBtGatt != null)
       {
           mBtGatt.disconnect();
           Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
           intent.putExtra("ActionFlag",BleIn_DActionService.ACTION_DISCONNECTINGBLEBT);
           startService(intent);
       }
       else
           quickToast("亲，当前手机没有连接任何BLE设备，无需断开连接哟！");
    }

    /*******************************为BLE设备特征数据添加->监听其变化的功能********************************/
    private Boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic,Boolean enable)
    {
        if (mBtAdapter == null || mBtGatt == null || mBtGattService == null)
        {
            quickToast("亲，因为手机未与任何BLE设备连接，所以添加监听数据变化的功能失败了哟！");
            return false;
        }
        if (characteristic == null)
        {
            quickToast("亲，因为没有特征数据，所以添加监听数据变化的功能失败了哟！");
            return false;
        }
        if (!mBtGatt.setCharacteristicNotification(characteristic, enable))
        {
            quickToast("亲，因为添加监听数据变化的操作失败，所以添加监听数据变化的功能失败了哟！");
            return false;
        }

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(BleOut_AUuidData.SERVICE_DESCRIPTORUUID);
        if (descriptor == null)
            return true;

         if(enable)
         {
             if((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0)
                 descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
             else
                 descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
         }
         else
             descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        return mBtGatt.writeDescriptor(descriptor);
    }

    /****************手机端从指定的BLE设备读取数据****************/
    private void ReadDatasFromBleBt(final UUID rCharacteristicUUID)
    {
        if(mBtGatt != null)
        {
            if (mBtGattService != null)
            {
                if(rCharacteristicUUID != null)
                {
                    mRCharacteristicUUID = rCharacteristicUUID;
                    if (mBtGattService.getCharacteristic(mRCharacteristicUUID) != null)
                    {
                        mBtGattRCharacteristic = mBtGattService.getCharacteristic(mRCharacteristicUUID);
                        mBtGatt.readCharacteristic(mBtGattRCharacteristic);
                        Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                        intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_READINGBLEBTDATA);
                        startService(intent);
                    }
                    else
                        quickToast("亲，因为指定的BLE设备中，指明的Service里面没有查找到相关的特征属性，所以无法进行读取操作哟！");
                }
                else
                    quickToast("亲，因为没有指定读取特征属性的UUID，所以无法进行读取操作哟！");
            }
            else
                quickToast("亲，因为没有查找到BLE设备中指定的Service，所以无法进行读取操作哟！");
        }
        else
            quickToast("亲，因为当前手机没有连接任何BLE设备，所以无法进行读取操作哟！");
    }

    /***************************手机端发送数据到指定的BLE设备***************************/
    private void writeDatasToBleBt(final UUID wCharacteristicUUID,final byte[] byteArray)
    {
        if(mBtGatt != null)
        {
            if (mBtGattService != null)
            {
                if(wCharacteristicUUID != null)
                {
                    mWCharacteristicUUID = wCharacteristicUUID;
                    if (mBtGattService.getCharacteristic(mWCharacteristicUUID) != null)
                    {
                        mBtGattWCharacteristic = mBtGattService.getCharacteristic(mWCharacteristicUUID);
                        if (byteArray != null && byteArray.length != 0)
                        {
                            mBtGattWCharacteristic.setValue(byteArray);
                            mBtGattWCharacteristic .setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                            mBtGatt.writeCharacteristic(mBtGattWCharacteristic);
                            Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
                            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_WRITINGBLEBTDATA);
                            startService(intent);
                        }
                        else
                            quickToast("亲，因为发送给BLE设备的命令为空，所以无法进行写入操作哟！");
                    }
                    else
                        quickToast("亲，因为指定的BLE设备中，指明的Service里面没有查找到相关的特征属性，所以无法进行写入操作哟！");
                }
                else
                    quickToast("亲，因为没有指定写入特征属性的UUID，所以无法进行写入操作哟！");
            }
            else
                quickToast("亲，因为没有查找到BLE设备中指定的Service，所以无法进行写入操作哟！");
        }
        else
            quickToast("亲，因为当前手机没有连接任何BLE设备，所以无法进行写入操作哟！");
    }

    /*****************************获取手机端与BLE设备之间的距离*************************/
    private void getRssiFromBleBt()
    {
        if(mBtGatt != null)
        {
            mBtGatt.readRemoteRssi();
            Intent  intent = new Intent(BleIn_BService.this,BleIn_DActionService.class);
            intent.putExtra("ActionFlag", BleIn_DActionService.ACTION_READINGBLEBTRSSI);
            startService(intent);
        }
        else
            quickToast("亲，因为当前手机没有连接任何BLE设备，所以无法获取RSSI距离哟！");
    }

    /********判定手机品牌是否是三星********/
    private Boolean deviceBrandIsSamSung()
    {
        String deviceBrand = android.os.Build.BRAND.toLowerCase();
        if (deviceBrand.equals("samsung"))
            return true;
        return false;
    }

    /**提供给Activity操作BLE蓝牙设备的变量**/
    public class BleBinder extends Binder
    {
        /**************开始搜寻手机周边所有的BLE设备,有UUID[]则用指定UUID[],没有则填写NULL**************/
        public void startSearchDevices(final Activity activity,final String notifyStr,final UUID[] uuids)
        {
            if(baseIsAvailable())
            {
                if (mBtAdapter.isEnabled())
                    beginSearchBleBt(uuids);
                else
                    openLocalBt(activity,notifyStr,uuids);
            }
        }

        /*******************停止搜寻手机周边所有的BLE设备****************/
        public void stopSearchDevices()
        {
            if(baseIsAvailable())
            {
                finishSearchBleBt(false);
            }
        }

        /*******************手机连接指定地址的BLE设备********************/
        public void conDevice(final String address,final UUID serviceUUID)
        {
            if(baseIsAvailable())
            {
                connectBleBt(address, serviceUUID);
            }
        }

        /*************与指定地址的BLE设备断开连接***********/
        public void disConDevice()
        {
            if(baseIsAvailable())
            {
                disConnectBleBt();
            }
        }

        /**********手动停止手机重新连接之前的BLE设备********/
        public void stopReConDevice()
        {
            if(baseIsAvailable())
            {
                stopAbnormalConnectBleBt();
            }
        }

        /************手机端从指定的BLE设备读取数据**********/
        public void readDatas(final UUID rCharacteristicUUID)
        {
            if(baseIsAvailable())
            {
                ReadDatasFromBleBt(rCharacteristicUUID);
            }
        }

        /************************手机端发送数据到指定的BLE设备**********************/
        public void writeDatas(final UUID wCharacteristicUUID,final byte[] byteArray)
        {
            if(baseIsAvailable())
            {
                writeDatasToBleBt(wCharacteristicUUID, byteArray);
            }
        }

        /************************获取手机端与BLE设备之间的距离**********************/
        public void getRssi()
        {
            if(baseIsAvailable())
            {
                getRssiFromBleBt();
            }
        }

        /*********************判定手机是否处于正在扫描周边BLE设备的过程中******************/
        public Boolean isScanningDevices()
        {
            return mScanningDevice;
        }

        /*********************设置手机每次扫描周边BLE设备允许花费的时间********************/
        public void setScanDevicesTime(final Integer scanTime)
        {
            mScanningTime = scanTime;
        }

        /***********判定手机与从端设备的链接状态,连接状态返回true，否则返回false***********/
        public Boolean getConnectState()
        {
            return mConedDevice;
        }
    }

    /***********提供给Activity操作BLE蓝牙设备的变量***********/
    public IBinder onBind(Intent intent)
    {
        return mBleBinder;
    }
}