package com.mediatek.internal.telephony;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class WifiSarController implements DynamicSarService.SarControllerClient {
    private static final String CLASS_PATH_HYPER_OS_CUST_FEATURE_RESOLVE = "miui.os.HyperOSCustFeatureResolve";
    private static final String CONFIG_SPECIAL_TAG = "config_special_tag";
    private static final String CONFIG_SUPPORT_CN_SAR = "config_support_cn_sar";
    private static final String CONFIG_SUPPORT_PROCESS_SAR_SENSOR_STATE = "config_support_process_sar_sensor_state";
    private static final String CONFIG_WIFI_SAR_PATTERN = "config_wifi_sar_pattern";
    private static final String METHOD_GET_BOOLEAN = "getBoolean";
    private static final String METHOD_GET_INT = "getInt";
    private static final String PROPERTY_DYNAMIC_WIFI_SAR = "persist.wifi.sar";
    private static final String PROPERTY_PRODUCT_MOD_DEVICE = "ro.product.mod_device";
    private static int mAudioReceiverState;
    private static int mBTPanState;
    private static int mHotspotState;
    private static int mModemState;
    private static int mSarSensor2State;
    private static int mSarSensorState;
    private static int mWifiState;
    private Context mContext;
    private Class mCustFeatureClass;
    private Method mGetBooleanMethod;
    private Method mGetIntMethod;
    private DynamicSarService mService;
    private WifiManager mWifiManager;
    private static int mMccState = 3;
    private static int mPaternIndex = 0;
    private static int mSarSet = 0;
    private static int mCnSarSet = -1;
    private static int mCurCcSupportTaSar = 0;
    private static boolean mIsTaSarSupportStateChanged = false;
    private static int mDbsState = 0;
    private static int mIsTaSarSupport = 0;
    private static List<String> mCurDsiTaPlimtsList = null;
    private final String TAG = "WifiSarController";
    private final int DSI0 = 0;
    private final int DSI1 = 1;
    private final int DSI2 = 2;
    private final int DSI3 = 3;
    private final int DSI4 = 4;
    private final int DSI5 = 5;
    private final int DSI6 = 6;
    private final int DSI7 = 7;
    private final int DSI8 = 8;
    private final int DSI9 = 9;
    private final int DSI10 = 10;
    private final int DSI11 = 11;
    private final int DSI12 = 12;
    private final int DSI18 = 18;
    private final int DSI19 = 19;
    private final int DSI20 = 20;
    private final int DSI21 = 21;
    private final int DSI22 = 22;
    private final int DSI23 = 23;
    private final int DSI24 = 24;
    private final int DSI25 = 25;
    private final int DSI26 = 26;
    private final int DSI_SAR_DISABLE = 100;

    public static boolean isNeeded() {
        return WifiManagerCompatible.isNeeded();
    }

    public WifiSarController(Context context) {
        this.mService = null;
        this.mCustFeatureClass = null;
        this.mGetIntMethod = null;
        this.mGetBooleanMethod = null;
        Log.d("WifiSarController", "WifiSarController init...");
        if (!SystemProperties.getBoolean(PROPERTY_DYNAMIC_WIFI_SAR, true)) {
            Log.d("WifiSarController", "disable wifi sar");
            return;
        }
        try {
            this.mCustFeatureClass = Class.forName(CLASS_PATH_HYPER_OS_CUST_FEATURE_RESOLVE);
            if (this.mCustFeatureClass != null) {
                this.mGetIntMethod = this.mCustFeatureClass.getMethod(METHOD_GET_INT, String.class, Integer.TYPE);
                this.mGetBooleanMethod = this.mCustFeatureClass.getMethod(METHOD_GET_BOOLEAN, String.class, Boolean.TYPE);
                if (this.mGetIntMethod != null) {
                    mPaternIndex = ((Integer) this.mGetIntMethod.invoke(null, CONFIG_WIFI_SAR_PATTERN, 0)).intValue();
                }
            }
            Log.d("WifiSarController", "wifi sar pattern is " + mPaternIndex);
        } catch (Exception e) {
            Log.e("WifiSarController", "get HyperOSCustFeatureResolve Int error: " + e);
        }
        Log.d("WifiSarController", "SAR patern is " + mPaternIndex);
        this.mContext = context;
        this.mWifiManager = (WifiManager) this.mContext.getSystemService("wifi");
        if (mPaternIndex != 0) {
            this.mService = DynamicSarService.getInstance(context);
        }
        if (this.mService != null) {
            if (mPaternIndex != 1) {
                if (mPaternIndex != 2) {
                    if (mPaternIndex != 3) {
                        if (mPaternIndex != 4) {
                            if (mPaternIndex != 5) {
                                if (mPaternIndex != 6) {
                                    if (mPaternIndex != 7) {
                                        if (mPaternIndex != 8) {
                                            if (mPaternIndex == 9) {
                                                this.mService.registerStateChangeListener(0, this);
                                            } else if (mPaternIndex == 20) {
                                                this.mService.registerStateChangeListener(0, this);
                                                this.mService.registerStateChangeListener(5, this);
                                            } else if (mPaternIndex == 21) {
                                                this.mService.registerStateChangeListener(0, this);
                                                this.mService.registerStateChangeListener(5, this);
                                                this.mService.registerStateChangeListener(9, this);
                                            } else if (mPaternIndex == 22 || mPaternIndex == 23) {
                                                this.mService.registerStateChangeListener(0, this);
                                                this.mService.registerStateChangeListener(5, this);
                                                this.mService.registerStateChangeListener(1, this);
                                            } else if (mPaternIndex == 24) {
                                                this.mService.registerStateChangeListener(0, this);
                                                this.mService.registerStateChangeListener(5, this);
                                            } else if (mPaternIndex == 25) {
                                                this.mService.registerStateChangeListener(0, this);
                                                this.mService.registerStateChangeListener(5, this);
                                                this.mService.registerStateChangeListener(2, this);
                                            } else if (mPaternIndex == 26) {
                                                this.mService.registerStateChangeListener(0, this);
                                                if (SystemProperties.get(PROPERTY_PRODUCT_MOD_DEVICE, "").endsWith("_global")) {
                                                    this.mService.registerStateChangeListener(1, this);
                                                    this.mService.registerStateChangeListener(2, this);
                                                    this.mService.registerStateChangeListener(5, this);
                                                    this.mService.registerStateChangeListener(6, this);
                                                    this.mService.registerStateChangeListener(7, this);
                                                    this.mService.registerStateChangeListener(15, this);
                                                    if (this.mService.isTaSarConfigEnable() && this.mService.isTaCloudEnable()) {
                                                        Log.d("WifiSarController", "register TA status");
                                                        this.mService.registerStateChangeListener(16, this);
                                                    }
                                                }
                                            }
                                        } else {
                                            this.mService.registerStateChangeListener(0, this);
                                            if (SystemProperties.get(PROPERTY_PRODUCT_MOD_DEVICE, "").endsWith("_global")) {
                                                this.mService.registerStateChangeListener(1, this);
                                                this.mService.registerStateChangeListener(2, this);
                                                this.mService.registerStateChangeListener(5, this);
                                                this.mService.registerStateChangeListener(6, this);
                                                this.mService.registerStateChangeListener(7, this);
                                                if (this.mService.isTaSarConfigEnable() && this.mService.isTaCloudEnable()) {
                                                    Log.d("WifiSarController", "register TA status");
                                                    this.mService.registerStateChangeListener(16, this);
                                                }
                                            }
                                        }
                                    } else {
                                        this.mService.registerStateChangeListener(0, this);
                                    }
                                } else {
                                    this.mService.registerStateChangeListener(0, this);
                                    if (SystemProperties.get(PROPERTY_PRODUCT_MOD_DEVICE, "").endsWith("_global")) {
                                        this.mService.registerStateChangeListener(6, this);
                                        this.mService.registerStateChangeListener(9, this);
                                        this.mService.registerStateChangeListener(15, this);
                                    }
                                }
                            } else {
                                this.mService.registerStateChangeListener(0, this);
                                if (SystemProperties.get(PROPERTY_PRODUCT_MOD_DEVICE, "").endsWith("_global")) {
                                    this.mService.registerStateChangeListener(1, this);
                                    this.mService.registerStateChangeListener(2, this);
                                    this.mService.registerStateChangeListener(5, this);
                                    this.mService.registerStateChangeListener(6, this);
                                    this.mService.registerStateChangeListener(7, this);
                                }
                            }
                        } else {
                            this.mService.registerStateChangeListener(0, this);
                            this.mService.registerStateChangeListener(1, this);
                            this.mService.registerStateChangeListener(2, this);
                            this.mService.registerStateChangeListener(5, this);
                            this.mService.registerStateChangeListener(6, this);
                            this.mService.registerStateChangeListener(7, this);
                        }
                    } else {
                        this.mService.registerStateChangeListener(0, this);
                        this.mService.registerStateChangeListener(1, this);
                        this.mService.registerStateChangeListener(2, this);
                        this.mService.registerStateChangeListener(5, this);
                        this.mService.registerStateChangeListener(6, this);
                    }
                } else {
                    this.mService.registerStateChangeListener(0, this);
                    this.mService.registerStateChangeListener(1, this);
                    this.mService.registerStateChangeListener(5, this);
                }
            } else {
                this.mService.registerStateChangeListener(0, this);
                this.mService.registerStateChangeListener(1, this);
                this.mService.registerStateChangeListener(3, this);
                this.mService.registerStateChangeListener(2, this);
                this.mService.registerStateChangeListener(5, this);
            }
        }
        Log.d("WifiSarController", "WifiSarController init done");
    }

    public int Convert_SarSensorState(int value) {
        if (value == 1 || value == 3 || value == 4) {
            return 1;
        }
        return value;
    }

    @Override // com.mediatek.internal.telephony.DynamicSarService.SarControllerClient
    public void onStateChanged(int type, int value) {
        Log.d("WifiSarController", "onStateChanged: type = " + type + ", value = " + value);
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
                if (this.mService.isTaSarConfigEnable() && mCurCcSupportTaSar != this.mService.isCcSupportTaSar()) {
                    mCurCcSupportTaSar = this.mService.isCcSupportTaSar();
                    mIsTaSarSupportStateChanged = true;
                }
                break;
            case 7:
                mBTPanState = value;
                break;
            case 9:
                boolean supportProcessOtherSarSensorState = false;
                try {
                    if (this.mGetBooleanMethod != null) {
                        supportProcessOtherSarSensorState = ((Boolean) this.mGetBooleanMethod.invoke(null, CONFIG_SUPPORT_PROCESS_SAR_SENSOR_STATE, false)).booleanValue();
                    }
                } catch (Exception e) {
                    Log.e("WifiSarController", "get HyperOSCustFeatureResolve Boolean error: " + e);
                }
                if (supportProcessOtherSarSensorState) {
                    mSarSensor2State = Convert_SarSensorState(value);
                } else {
                    mSarSensor2State = value;
                }
                break;
            case 15:
                mDbsState = value;
                break;
            case 16:
                mCurCcSupportTaSar = value;
                mIsTaSarSupportStateChanged = true;
                break;
        }
        updateWifiSarSet();
    }

    private void updateWifiSarSet() {
        int sarSet = 100;
        if (mPaternIndex == 1) {
            sarSet = calculateSarSetPatern1();
        } else if (mPaternIndex == 2) {
            sarSet = calculateSarSetPatern2();
        } else if (mPaternIndex == 3) {
            sarSet = calculateSarSetPatern3();
        } else if (mPaternIndex == 4) {
            boolean isPissarroTag = false;
            try {
                if (this.mGetBooleanMethod != null) {
                    isPissarroTag = ((Boolean) this.mGetBooleanMethod.invoke(null, CONFIG_SPECIAL_TAG, false)).booleanValue();
                }
            } catch (Exception e) {
                Log.e("WifiSarController", "get HyperOSCustFeatureResolve Boolean error: " + e);
            }
            if (isPissarroTag) {
                sarSet = calculatePissarroSarSetPatern4();
            } else {
                sarSet = calculateSarSetPatern4();
            }
        } else if (mPaternIndex == 5) {
            sarSet = calculateSarSetPatern5();
        } else if (mPaternIndex == 6) {
            sarSet = calculateSarSetPatern6();
        } else if (mPaternIndex == 7) {
            sarSet = calculateSarSetPatern7();
        } else if (mPaternIndex == 8) {
            sarSet = calculateSarSetPatern8();
        } else if (mPaternIndex == 9) {
            sarSet = calculateSarSetPatern9();
        } else if (mPaternIndex == 20) {
            sarSet = calculateSarSetPatern20();
        } else if (mPaternIndex == 21) {
            sarSet = calculateSarSetPatern21();
        } else if (mPaternIndex == 22) {
            sarSet = calculateSarSetPatern22();
        } else if (mPaternIndex == 23) {
            sarSet = calculateSarSetPatern23();
        } else if (mPaternIndex == 24) {
            sarSet = calculateSarSetPatern24();
        } else if (mPaternIndex == 25) {
            sarSet = calculateSarSetPatern25();
        } else if (mPaternIndex == 26) {
            sarSet = calculateSarSetPatern26();
        }
        if (SystemProperties.get(PROPERTY_PRODUCT_MOD_DEVICE, "").endsWith("_global")) {
            try {
                Log.d("WifiSarController", "sar set is " + sarSet);
                boolean z = mIsTaSarSupportStateChanged;
                DynamicSarService dynamicSarService = this.mService;
                if (z) {
                    if (dynamicSarService.isTaSarEnable()) {
                        setTaSarPlimits(sarSet);
                        WifiManagerCompatible.setSARLimit(this.mContext, 100);
                    } else {
                        WifiManagerCompatible.setSARLimit(this.mContext, sarSet);
                        WifiManagerCompatible.enableTaSar(this.mContext, false);
                    }
                } else if (dynamicSarService.isTaSarEnable()) {
                    setTaSarPlimits(sarSet);
                } else {
                    WifiManagerCompatible.setSARLimit(this.mContext, sarSet);
                }
                mSarSet = sarSet;
                mIsTaSarSupportStateChanged = false;
                return;
            } catch (Exception e2) {
                Log.e("WifiSarController", "error happend when updateWifiSarSet." + e2);
                return;
            }
        }
        if (mCnSarSet != sarSet) {
            Log.d("WifiSarController", "setSARLimit: " + sarSet);
            WifiManagerCompatible.setSARLimit(this.mContext, sarSet);
            mCnSarSet = sarSet;
        }
    }

    private void getCurrentDsiPlimit(int sarSet) {
        try {
            String[] allDsiTaPlimts = this.mService.getTaPlimitArray();
            if (allDsiTaPlimts != null && sarSet <= allDsiTaPlimts.length && sarSet >= 0) {
                String[] curDsiTaPlimits = allDsiTaPlimts[sarSet].split(";");
                mCurDsiTaPlimtsList = new ArrayList();
                for (String curDsiTaPlimit : curDsiTaPlimits) {
                    if (TextUtils.isEmpty(curDsiTaPlimit)) {
                        Log.e("WifiSarController", "curDsiTaPlimits is empty");
                    } else {
                        Log.d("WifiSarController", "taPlimts: " + curDsiTaPlimit);
                        mCurDsiTaPlimtsList.add(curDsiTaPlimit);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("WifiSarController", "error happend when getCurrentDsiPlimit...");
        }
    }

    private void setTaSarPlimits(int sarSet) {
        getCurrentDsiPlimit(sarSet);
        WifiManagerCompatible.setTaSARLimit(this.mContext, mCurDsiTaPlimtsList);
    }

    private int calculateSarSetPatern1() {
        if (mWifiState == 0 && mHotspotState == 0) {
            return 100;
        }
        if (mAudioReceiverState == 1) {
            if (mModemState == 1) {
                return 1;
            }
            return 0;
        }
        if (mHotspotState == 1) {
            if (mSarSensorState == 0) {
                return 100;
            }
            return 4;
        }
        if (mModemState == 1) {
            if (mSarSensorState == 1) {
                return 3;
            }
            if (mSarSensorState != 1) {
                return 100;
            }
            return 2;
        }
        if (mSarSensorState == 0) {
            return 100;
        }
        return 0;
    }

    private int calculateSarSetPatern2() {
        if (mWifiState == 0 && mHotspotState == 0) {
            return 100;
        }
        if (mWifiState == 1 && mHotspotState == 1) {
            int staFreq = this.mWifiManager.getConnectionInfo().getFrequency();
            int sapBand = this.mWifiManager.getSoftApConfiguration().getBand();
            if ((staFreq < 5000 && sapBand == 2) || (staFreq > 5000 && sapBand == 1)) {
                return 1;
            }
        }
        if (mAudioReceiverState != 1) {
            return 100;
        }
        return 0;
    }

    private int calculateSarSetPatern3() {
        if (mWifiState == 0 && mHotspotState == 0) {
            return 100;
        }
        if (mHotspotState == 1) {
            return 8;
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

    private int calculateSarSetPatern4() {
        if (mWifiState == 0 && mHotspotState == 0) {
            return 100;
        }
        if (mHotspotState == 1 || mBTPanState == 1) {
            return 12;
        }
        if (mAudioReceiverState == 1) {
            if (mMccState == 1) {
                int sarSet = mModemState != 1 ? 0 : 1;
                return sarSet;
            }
            if (mMccState == 2) {
                int sarSet2 = mModemState != 1 ? 2 : 3;
                return sarSet2;
            }
            if (mMccState != 3) {
                return 100;
            }
            int sarSet3 = mModemState == 1 ? 5 : 4;
            return sarSet3;
        }
        if (mHotspotState != 0) {
            return 100;
        }
        if (mMccState == 1) {
            int sarSet4 = mModemState == 1 ? 7 : 6;
            return sarSet4;
        }
        if (mMccState == 2) {
            int sarSet5 = mModemState == 1 ? 9 : 8;
            return sarSet5;
        }
        if (mMccState != 3) {
            return 100;
        }
        int sarSet6 = mModemState == 1 ? 11 : 10;
        return sarSet6;
    }

    private int calculatePissarroSarSetPatern4() {
        if (mWifiState == 0 && mHotspotState == 0) {
            return 100;
        }
        if (mHotspotState == 1 || mBTPanState == 1) {
            if (mAudioReceiverState == 1 && mMccState == 2) {
                return 3;
            }
            if (mAudioReceiverState == 1 && mMccState == 3) {
                return 5;
            }
            return 12;
        }
        if (mAudioReceiverState == 1) {
            if (mMccState == 1) {
                int sarSet = mModemState != 1 ? 0 : 1;
                return sarSet;
            }
            if (mMccState == 2) {
                int sarSet2 = mModemState != 1 ? 2 : 3;
                return sarSet2;
            }
            if (mMccState != 3) {
                return 100;
            }
            int sarSet3 = mModemState == 1 ? 5 : 4;
            return sarSet3;
        }
        if (mHotspotState != 0) {
            return 100;
        }
        if (mMccState == 1) {
            int sarSet4 = mModemState == 1 ? 7 : 6;
            return sarSet4;
        }
        if (mMccState == 2) {
            int sarSet5 = mModemState == 1 ? 9 : 8;
            return sarSet5;
        }
        if (mMccState != 3) {
            return 100;
        }
        int sarSet6 = mModemState == 1 ? 11 : 10;
        return sarSet6;
    }

    private int calculateSarSetPatern5() {
        if (isCNSARCTRLDevices()) {
            if (mWifiState == 0 || mWifiState != 1) {
                return 100;
            }
            return 0;
        }
        if (mWifiState == 0 && mHotspotState == 0) {
            return 100;
        }
        if (mHotspotState == 1 || mBTPanState == 1) {
            if (mAudioReceiverState == 1 && mMccState == 2) {
                return 3;
            }
            if (mAudioReceiverState == 1 && mMccState == 3) {
                return 5;
            }
            if (mMccState == 1) {
                int sarSet = mAudioReceiverState != 1 ? 7 : 1;
                return sarSet;
            }
            return 12;
        }
        if (mAudioReceiverState == 1) {
            if (mMccState == 1) {
                int sarSet2 = mModemState != 1 ? 0 : 1;
                return sarSet2;
            }
            if (mMccState == 2) {
                int sarSet3 = mModemState != 1 ? 2 : 3;
                return sarSet3;
            }
            if (mMccState != 3) {
                return 100;
            }
            int sarSet4 = mModemState == 1 ? 5 : 4;
            return sarSet4;
        }
        if (mHotspotState != 0) {
            return 100;
        }
        if (mMccState == 1) {
            int sarSet5 = mModemState != 1 ? 6 : 7;
            return sarSet5;
        }
        if (mMccState == 2) {
            int sarSet6 = mModemState == 1 ? 9 : 8;
            return sarSet6;
        }
        if (mMccState != 3) {
            return 100;
        }
        int sarSet7 = mModemState == 1 ? 11 : 10;
        return sarSet7;
    }

    private int calculateSarSetPatern6() {
        if (isCNSARCTRLDevices()) {
            if (mWifiState == 0 || mWifiState != 1) {
                return 100;
            }
            return 0;
        }
        if (mDbsState == 0) {
            if (mMccState == 1) {
                int sarSet = mSarSensor2State != 1 ? 0 : 1;
                return sarSet;
            }
            if (mMccState == 2) {
                int sarSet2 = mSarSensor2State != 1 ? 2 : 3;
                return sarSet2;
            }
            if (mMccState != 3) {
                return 100;
            }
            int sarSet3 = mSarSensor2State == 1 ? 5 : 4;
            return sarSet3;
        }
        if (mDbsState != 1) {
            return 100;
        }
        if (mMccState == 1) {
            int sarSet4 = mSarSensor2State == 1 ? 19 : 18;
            return sarSet4;
        }
        if (mMccState == 2) {
            int sarSet5 = mSarSensor2State == 1 ? 21 : 20;
            return sarSet5;
        }
        if (mMccState != 3) {
            return 100;
        }
        int sarSet6 = mSarSensor2State == 1 ? 23 : 22;
        return sarSet6;
    }

    private int calculateSarSetPatern7() {
        if (!isCNSARCTRLDevices() || mWifiState == 0 || mWifiState != 1) {
            return 100;
        }
        return 0;
    }

    private int calculateSarSetPatern8() {
        if (isCNSARCTRLDevices()) {
            if (mWifiState == 0 || mWifiState != 1) {
                return 100;
            }
            return 0;
        }
        if (mWifiState == 0 && mHotspotState == 0) {
            return 100;
        }
        if (mHotspotState == 1 || mBTPanState == 1) {
            if (mAudioReceiverState == 1) {
                int sarSet = mMccState != 1 ? 3 : 1;
                return sarSet;
            }
            int sarSet2 = mMccState != 1 ? 8 : 5;
            return sarSet2;
        }
        if (mAudioReceiverState == 1) {
            if (mMccState == 1) {
                int sarSet3 = mModemState != 1 ? 0 : 1;
                return sarSet3;
            }
            int sarSet4 = mModemState != 1 ? 2 : 3;
            return sarSet4;
        }
        if (mHotspotState != 0) {
            return 100;
        }
        if (mMccState == 1) {
            int sarSet5 = mModemState != 1 ? 4 : 5;
            return sarSet5;
        }
        int sarSet6 = mModemState == 1 ? 7 : 6;
        return sarSet6;
    }

    private int calculateSarSetPatern9() {
        if (!isCNSARCTRLDevices() || mWifiState == 0 || mWifiState != 1) {
            return 100;
        }
        return 0;
    }

    private int calculateSarSetPatern20() {
        int sarSet = 100;
        if (mWifiState == 1) {
            if (mAudioReceiverState == 1) {
                sarSet = 1;
            } else {
                sarSet = 2;
            }
        }
        Log.d("WifiSarController", "calculateSarSetPatern20 sarSet = " + sarSet);
        return sarSet;
    }

    private int calculateSarSetPatern21() {
        int sarSet = 100;
        if (mWifiState == 1) {
            if (mAudioReceiverState == 1) {
                sarSet = 1;
            } else if (mSarSensor2State == 0) {
                sarSet = 2;
            } else if (mSarSensor2State == 1 || mSarSensor2State == 3 || mSarSensor2State == 4) {
                sarSet = 3;
            }
        }
        Log.d("WifiSarController", "calculateSarSetPatern21 mSarSensor2State = " + mSarSensor2State);
        Log.d("WifiSarController", "calculateSarSetPatern21 sarSet = " + sarSet);
        return sarSet;
    }

    private int calculateSarSetPatern22() {
        int sarSet = 100;
        if (mWifiState == 1 || mHotspotState == 1) {
            if (mAudioReceiverState == 1) {
                sarSet = 1;
            } else {
                sarSet = 2;
            }
        }
        Log.d("WifiSarController", "calculateSarSetPatern22 sarSet = " + sarSet);
        return sarSet;
    }

    private int calculateSarSetPatern23() {
        int sarSet = 100;
        String region = SystemProperties.get("ro.miui.region", (String) null);
        Log.d("WifiSarController", "wifisar region + " + region);
        if (mWifiState == 1 || mHotspotState == 1) {
            if (mAudioReceiverState == 1) {
                if ("CN".equals(region)) {
                    Log.d("WifiSarController", "cn no sar");
                } else if ("IN".equals(region)) {
                    sarSet = 1;
                } else if ("JP".equals(region)) {
                    sarSet = 3;
                } else {
                    sarSet = "KR".equals(region) ? 5 : 7;
                }
            } else if ("CN".equals(region)) {
                Log.d("WifiSarController", "cn no sar");
            } else if ("IN".equals(region)) {
                sarSet = 2;
            } else if ("JP".equals(region)) {
                sarSet = 4;
            } else {
                sarSet = "KR".equals(region) ? 6 : 8;
            }
        }
        Log.d("WifiSarController", "calculateSarSetPatern23 sarSet = " + sarSet);
        return sarSet;
    }

    private int calculateSarSetPatern24() {
        int sarSet = 100;
        if (mWifiState == 1) {
            if (mAudioReceiverState == 1) {
                sarSet = 1;
            } else {
                sarSet = 2;
            }
        }
        Log.d("WifiSarController", "calculateSarSetPatern24 sarSet = " + sarSet);
        return sarSet;
    }

    private int calculateSarSetPatern25() {
        int sarSet = 100;
        if (mWifiState == 1) {
            if (mAudioReceiverState == 1) {
                sarSet = mModemState != 1 ? 0 : 1;
            } else {
                sarSet = mModemState == 1 ? 3 : 2;
            }
        }
        Log.d("WifiSarController", "calculateSarSetPatern25 sarSet = " + sarSet);
        return sarSet;
    }

    private int calculateSarSetPatern26() {
        if (isCNSARCTRLDevices()) {
            if (mWifiState == 0 || mWifiState != 1) {
                return 100;
            }
            return 0;
        }
        if (mWifiState == 0 && mHotspotState == 0) {
            return 100;
        }
        if (mDbsState == 0) {
            if (mHotspotState == 1 || mBTPanState == 1) {
                if (mAudioReceiverState == 1) {
                    int sarSet = mMccState != 1 ? 3 : 1;
                    return sarSet;
                }
                int sarSet2 = mMccState != 1 ? 8 : 5;
                return sarSet2;
            }
            if (mAudioReceiverState == 1) {
                if (mMccState == 1) {
                    int sarSet3 = mModemState != 1 ? 0 : 1;
                    return sarSet3;
                }
                int sarSet4 = mModemState != 1 ? 2 : 3;
                return sarSet4;
            }
            if (mHotspotState != 0) {
                return 100;
            }
            if (mMccState == 1) {
                int sarSet5 = mModemState != 1 ? 4 : 5;
                return sarSet5;
            }
            int sarSet6 = mModemState == 1 ? 7 : 6;
            return sarSet6;
        }
        if (mDbsState != 1) {
            return 100;
        }
        if (mHotspotState == 1 || mBTPanState == 1) {
            if (mAudioReceiverState == 1) {
                int sarSet7 = mMccState != 1 ? 21 : 19;
                return sarSet7;
            }
            int sarSet8 = mMccState != 1 ? 26 : 23;
            return sarSet8;
        }
        if (mAudioReceiverState == 1) {
            if (mMccState == 1) {
                int sarSet9 = mModemState != 1 ? 18 : 19;
                return sarSet9;
            }
            int sarSet10 = mModemState != 1 ? 20 : 21;
            return sarSet10;
        }
        if (mHotspotState != 0) {
            return 100;
        }
        if (mMccState == 1) {
            int sarSet11 = mModemState != 1 ? 22 : 23;
            return sarSet11;
        }
        int sarSet12 = mModemState == 1 ? 25 : 24;
        return sarSet12;
    }

    private boolean isCNSARCTRLDevices() {
        String product_device = SystemProperties.get(PROPERTY_PRODUCT_MOD_DEVICE, "");
        boolean isSupportCnSar = false;
        try {
            if (this.mGetBooleanMethod != null) {
                isSupportCnSar = ((Boolean) this.mGetBooleanMethod.invoke(null, CONFIG_SUPPORT_CN_SAR, false)).booleanValue();
            }
        } catch (Exception e) {
            Log.e("WifiSarController", "get HyperOSCustFeatureResolve Boolean error: " + e);
        }
        if (product_device.endsWith("_global") || !isSupportCnSar) {
            return false;
        }
        return true;
    }
}
