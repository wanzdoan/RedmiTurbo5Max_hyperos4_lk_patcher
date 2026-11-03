package com.mediatek.internal.telephony;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemProperties;
import android.util.Log;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothSarController implements DynamicSarService.SarControllerClient {
    private static final String PROPERTY_DYNAMIC_BLUETOOTH_SAR = "persist.bluetooth.sar";
    private static int mAudioReceiverState;
    private static int mBluetoothPanState;
    private static int mBluetoothState;
    private static int mHotspotState;
    private static int mModemState;
    private static int mSarSensorState;
    private static int mWifiState;
    private Context mContext;
    private DynamicSarService mService;
    private WifiManager mWifiManager;
    private static int mMccState = 3;
    private static int mPaternIndex = 0;
    private static int mSarSet = 0;
    private final String TAG = "BluetoothSarController";
    private final int DSI0 = 0;
    private final int DSI1 = 1;
    private final int DSI2 = 2;
    private final int DSI3 = 3;
    private final int DSI4 = 4;
    private final int DSI5 = 5;
    private final int DSI6 = 6;
    private final int DSI7 = 7;
    private final int DSI8 = 8;
    private final int DSI_SAR_DISABLE = 100;
    private final String PATERN1_DEVCIE_LIST = "tucana,davinci,davinciin";
    private final String PATERN2_DEVCIE_LIST = "bomb,atom,cezanne,aresin,matisse,pissarro,pissarropro,agate,amber,xaga,xagapro,xagain,xagaproin,plato,corot,xig04,zircon,duchamp,rothko,degas,rodin,xig07,aristotle,klimt,goya,dash,klee";
    private final String PATERN3_DEVCIE_LIST = "turner";

    public static boolean isNeeded() {
        return BluetoothAdapterCompatible.isNeeded();
    }

    public BluetoothSarController(Context context) {
        int i;
        this.mService = null;
        Log.d("BluetoothSarController", "BluetoothSarController init...");
        if (!SystemProperties.getBoolean(PROPERTY_DYNAMIC_BLUETOOTH_SAR, true)) {
            Log.d("BluetoothSarController", "disable bluetooth sar");
            return;
        }
        boolean patern1Device = false;
        boolean patern2Device = false;
        boolean patern3Device = false;
        try {
            Class<?> cls = Class.forName("miui.os.HyperOSCustFeatureResolve");
            i = 3;
            try {
                Method method = cls.getDeclaredMethod("getBoolean", String.class, Boolean.TYPE);
                patern1Device = ((Boolean) method.invoke(cls, "patern1_device", false)).booleanValue();
                patern2Device = ((Boolean) method.invoke(cls, "patern2_device", false)).booleanValue();
                patern3Device = ((Boolean) method.invoke(cls, "persist.sys.support.sar.device", false)).booleanValue();
            } catch (Exception e) {
                e = e;
                Log.e("BluetoothSarController", "get HyperOSCustFeatureResolve Boolean error: " + e);
            }
        } catch (Exception e2) {
            e = e2;
            i = 3;
        }
        String device = Build.DEVICE.toLowerCase();
        if ("tucana,davinci,davinciin".indexOf(device) == -1 && !patern1Device) {
            if ("bomb,atom,cezanne,aresin,matisse,pissarro,pissarropro,agate,amber,xaga,xagapro,xagain,xagaproin,plato,corot,xig04,zircon,duchamp,rothko,degas,rodin,xig07,aristotle,klimt,goya,dash,klee".indexOf(device) != -1 || patern2Device) {
                mPaternIndex = 2;
            } else if ("turner".indexOf(device) != -1 || patern3Device) {
                mPaternIndex = i;
            }
            Log.d("BluetoothSarController", "device: " + device + ", SAR patern: " + mPaternIndex);
            this.mContext = context;
            this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
            if (mPaternIndex != 0) {
                this.mService = DynamicSarService.getInstance(context);
            }
            if (this.mService != null) {
                if (mPaternIndex != 1) {
                    if (mPaternIndex == 2) {
                        this.mService.registerStateChangeListener(0, this);
                        this.mService.registerStateChangeListener(1, this);
                        this.mService.registerStateChangeListener(2, this);
                        this.mService.registerStateChangeListener(5, this);
                        this.mService.registerStateChangeListener(7, this);
                        this.mService.registerStateChangeListener(8, this);
                    } else if (mPaternIndex == i) {
                        this.mService.registerStateChangeListener(0, this);
                        this.mService.registerStateChangeListener(1, this);
                        this.mService.registerStateChangeListener(8, this);
                    }
                } else {
                    this.mService.registerStateChangeListener(8, this);
                    this.mService.registerStateChangeListener(0, this);
                    this.mService.registerStateChangeListener(1, this);
                    this.mService.registerStateChangeListener(2, this);
                    this.mService.registerStateChangeListener(5, this);
                    this.mService.registerStateChangeListener(6, this);
                }
            }
            Log.d("BluetoothSarController", "BluetoothSarController init done");
        }
    }

    @Override // com.mediatek.internal.telephony.DynamicSarService.SarControllerClient
    public void onStateChanged(int type, int value) {
        Log.d("BluetoothSarController", "onStateChanged: type = " + type + ", value = " + value);
        switch (type) {
            case 0:
                mWifiState = value;
                break;
            case 1:
                mHotspotState = value;
                break;
            case 2:
                mModemState = value;
                break;
            case 3:
                mSarSensorState = value;
                break;
            case 5:
                mAudioReceiverState = value;
                break;
            case 6:
                mMccState = value;
                break;
            case 7:
                mBluetoothPanState = value;
                break;
            case 8:
                mBluetoothState = value;
                break;
        }
        updateBluetoothSarSet();
    }

    private void updateBluetoothSarSet() {
        int sarSet = 100;
        Log.d("BluetoothSarController", "updateBluetoothSarSet");
        if (mPaternIndex == 1) {
            sarSet = calculateSarSetPatern1();
        } else if (mPaternIndex == 2) {
            sarSet = calculateSarSetPatern2();
        } else if (mPaternIndex == 3) {
            sarSet = calculateSarSetPatern3();
        }
        if (mSarSet != sarSet) {
            Log.d("BluetoothSarController", "setSARLimit: " + sarSet);
            BluetoothAdapterCompatible.setSARLimit(sarSet);
            mSarSet = sarSet;
        }
    }

    private int calculateSarSetPatern1() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            return 100;
        }
        if (mAudioReceiverState == 1) {
            if (mMccState == 1) {
                int sarSet = mModemState != 1 ? 0 : 1;
                return sarSet;
            }
            int sarSet2 = mModemState == 1 ? 3 : 2;
            return sarSet2;
        }
        if (mHotspotState != 0) {
            return 100;
        }
        if (mMccState == 1) {
            int sarSet3 = mModemState == 1 ? 5 : 4;
            return sarSet3;
        }
        int sarSet4 = mModemState == 1 ? 7 : 6;
        return sarSet4;
    }

    private int calculateSarSetPatern2() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        int staFreq = 0;
        int sapBand = 0;
        if (!mBluetoothAdapter.isEnabled()) {
            return 100;
        }
        if (mWifiState == 1) {
            staFreq = this.mWifiManager.getConnectionInfo().getFrequency();
        }
        if (mHotspotState == 1) {
            sapBand = this.mWifiManager.getSoftApConfiguration().getBand();
        }
        if ((mBluetoothPanState == 0 && ((mHotspotState != 1 || (sapBand & 2) == 0) && mAudioReceiverState == 1 && mBluetoothState == 1)) || ((mBluetoothPanState == 1 || (mHotspotState == 1 && (sapBand & 2) != 0)) && mBluetoothState == 1)) {
            return 1;
        }
        if (mAudioReceiverState == 0 && ((mWifiState != 1 || staFreq <= 5000) && mModemState == 0 && ((mHotspotState != 1 || (sapBand & 2) == 0) && mBluetoothPanState == 0 && mBluetoothState == 1))) {
            return 2;
        }
        if (mAudioReceiverState == 0 && ((mWifiState != 1 || staFreq <= 5000) && mModemState == 1 && ((mHotspotState != 1 || (sapBand & 2) == 0) && mBluetoothPanState == 0 && mBluetoothState == 1))) {
            return 3;
        }
        if (mAudioReceiverState == 0 && mWifiState == 1 && staFreq > 5000 && mModemState == 0 && ((mHotspotState != 1 || (sapBand & 2) == 0) && mBluetoothPanState == 0 && mBluetoothState == 1)) {
            return 4;
        }
        if (mAudioReceiverState == 0 && mWifiState == 1 && staFreq > 5000 && mModemState == 1 && ((mHotspotState != 1 || (sapBand & 2) == 0) && mBluetoothPanState == 0 && mBluetoothState == 1)) {
            return 5;
        }
        return 100;
    }

    private int calculateSarSetPatern3() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        int staFreq = 0;
        int sapBand = 0;
        if (!mBluetoothAdapter.isEnabled()) {
            return 100;
        }
        if (mWifiState == 1) {
            staFreq = this.mWifiManager.getConnectionInfo().getFrequency();
            Log.d("BluetoothSarController", "calculateSarSetPatern3, staFreq = " + staFreq);
        }
        if (mHotspotState == 1) {
            sapBand = this.mWifiManager.getSoftApConfiguration().getBand();
            Log.d("BluetoothSarController", "calculateSarSetPatern3, sapBand = " + sapBand);
        }
        if (mBluetoothState == 1 && ((mWifiState != 1 || staFreq <= 5000) && (mHotspotState != 1 || (sapBand & 2) == 0))) {
            return 1;
        }
        if ((mBluetoothState == 1 && mWifiState == 1 && staFreq > 5000 && (mHotspotState != 1 || (sapBand & 2) == 0)) || ((mBluetoothState == 1 && ((mWifiState != 1 || staFreq <= 5000) && mHotspotState == 1 && (sapBand & 2) != 0)) || (mBluetoothState == 1 && mWifiState == 1 && staFreq > 5000 && mHotspotState == 1 && (sapBand & 2) != 0))) {
            return 2;
        }
        return 100;
    }
}
