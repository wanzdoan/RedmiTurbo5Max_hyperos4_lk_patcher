package com.android.phone;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.MobileDataUtils;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IRemoteCallback;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.Rlog;
import android.telephony.emergency.EmergencyNumber;
import android.telephony.tcpka.IModemTcpKeepAliveIndCallback;
import android.text.TextUtils;
import com.android.ims.ImsManager;
import com.android.internal.os.BackgroundThread;
import com.android.internal.telephony.IccCardActivateHelper;
import com.android.internal.telephony.MiuiIccPhoneBookInterfaceManager;
import com.android.internal.telephony.MiuiNetworkControllerImpl;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.imsphone.MiuiImsControllerImpl;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.MiuiUiccControllerImpl;
import com.android.internal.telephony.uicc.UiccController;
import com.android.phone.dsda.MiuiDsdaManager;
import com.android.phone.module.IFiveGPowerModule;
import com.android.phone.module.INetworkModule;
import com.android.phone.module.ISatelliteModule;
import com.android.phone.networkmode.NetworkMode;
import com.android.phone.roaming.RoamingManager;
import com.android.phone.statistics.ExternalProjectStatistic;
import com.android.phone.statistics.FiveGAStatistics;
import com.android.phone.statistics.MiuiDsdaStatistic;
import com.android.phone.utils.MiuiNewCallUtils;
import com.android.phone.utils.ModemUtils;
import com.android.services.telephony.TelecomAccountRegistry;
import com.android.services.telephony.TelephonyConnectionService;
import com.miui.internal.telephony.IHyperTelephonyCallback;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.CotaOpconfigManager;
import miui.telephony.DefaultSimManager;
import miui.telephony.HyperTelephonyBase;
import miui.telephony.MiuiTelephony;
import miui.telephony.MiuiTelephonyBase;
import miui.telephony.ServiceProviderUtils;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;
import miui.util.FeatureParser;

/* JADX INFO: loaded from: classes.dex */
public class MiuiPhoneInterfaceManager extends MiuiTelephonyBase {
    public static final boolean IS_JLQ;
    public static final boolean IS_MTK;
    public static final boolean IS_PINECONE;
    public static final boolean IS_PROJECT_SUPPORT_ESIM;
    public static final boolean IS_QCOM;
    public static final boolean IS_XRING;
    private static String LOG_TAG = "MiuiPhoneInterfaceManager";
    public static final int PHONE_COUNT = TelephonyManager.getDefault().getPhoneCount();
    private static volatile MiuiPhoneInterfaceManager sInstance;
    private AppOpsManager mAppOps;
    private Context mContext;
    private MiuiPhoneInterfaceManagerHandler mHandler;
    private ImeiMeidSource mImeiMeidSource;
    private UiccController mUiccController;
    private boolean mIsVoiceAndDataAllowed = false;
    private final HyperTelephonyStub mHyperTelephonyStub = new HyperTelephonyStub();

    public boolean dialForNtn(String str) {
        return false;
    }

    public List getFiveGPrecisionPosition(int i) {
        return null;
    }

    public boolean isSatelliteBox() {
        return false;
    }

    public boolean isShowCdmaUnavailableAlert() {
        return false;
    }

    public void listenFiveGPrecisionPositionChange(int i, int i2) {
    }

    public void sendSatelliteMessage(String str, String str2) {
    }

    public boolean setFiveGPrecisionPositionEnable(boolean z, int i) {
        return false;
    }

    static {
        boolean zEquals = "jlq".equals(FeatureParser.getString("vendor"));
        IS_JLQ = zEquals;
        IS_QCOM = "qcom".equals(FeatureParser.getString("vendor")) || zEquals;
        IS_MTK = "mediatek".equals(FeatureParser.getString("vendor"));
        IS_XRING = "xring".equals(FeatureParser.getString("vendor"));
        IS_PINECONE = "pinecone".equals(FeatureParser.getString("vendor"));
        IS_PROJECT_SUPPORT_ESIM = SystemProperties.getBoolean("ro.vendor.miui.support_esim", false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private MiuiPhoneInterfaceManager() {
        if (ServiceManager.getService("miui.radio.extphone") == null) {
            Rlog.d(LOG_TAG, "Adding IMiuiPhoneInterfaceManager to ServiceManager as miui.radio.extphone");
            ServiceManager.addService("miui.radio.extphone", this);
        }
    }

    public static MiuiPhoneInterfaceManager getInstance() {
        if (sInstance == null) {
            synchronized (MiuiPhoneInterfaceManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new MiuiPhoneInterfaceManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
        this.mImeiMeidSource = new ImeiMeidSource(this.mContext);
        this.mHandler = new MiuiPhoneInterfaceManagerHandler(this, BackgroundThread.get().getLooper());
        this.mUiccController = UiccController.getInstance();
    }

    final class HyperTelephonyStub extends HyperTelephonyBase {
        private final RemoteCallbackList mCallbacks;

        private HyperTelephonyStub() {
            this.mCallbacks = new RemoteCallbackList();
        }

        public void registerCallback(String str, int i, IHyperTelephonyCallback iHyperTelephonyCallback) {
            if (iHyperTelephonyCallback == null) {
                Rlog.w(MiuiPhoneInterfaceManager.LOG_TAG, "registerCallback: callback is null");
                return;
            }
            if (str == null || str.isEmpty()) {
                Rlog.w(MiuiPhoneInterfaceManager.LOG_TAG, "registerCallback: callingPkg is null or empty");
                return;
            }
            MiuiPhoneInterfaceManager.this.mAppOps.checkPackage(Binder.getCallingUid(), str);
            Rlog.d(MiuiPhoneInterfaceManager.LOG_TAG, "registerCallback: pkg=" + str + ", slotId=" + i);
            this.mCallbacks.register(iHyperTelephonyCallback, Integer.valueOf(i));
        }

        public void unregisterCallback(String str, int i, IHyperTelephonyCallback iHyperTelephonyCallback) {
            if (iHyperTelephonyCallback == null) {
                Rlog.w(MiuiPhoneInterfaceManager.LOG_TAG, "unregisterCallback: callback is null");
                return;
            }
            if (str == null || str.isEmpty()) {
                Rlog.w(MiuiPhoneInterfaceManager.LOG_TAG, "unregisterCallback: callingPkg is null or empty");
                return;
            }
            MiuiPhoneInterfaceManager.this.mAppOps.checkPackage(Binder.getCallingUid(), str);
            Rlog.d(MiuiPhoneInterfaceManager.LOG_TAG, "unregisterCallback: pkg=" + str + ", slotId=" + i);
            this.mCallbacks.unregister(iHyperTelephonyCallback);
        }

        public boolean isEmergencyCallPending() {
            TelephonyConnectionService telephonyConnectionService;
            TelecomAccountRegistry telecomAccountRegistry = TelecomAccountRegistry.getInstance(null);
            return (telecomAccountRegistry == null || (telephonyConnectionService = telecomAccountRegistry.getTelephonyConnectionService()) == null || !telephonyConnectionService.isEmergencyCallPending()) ? false : true;
        }
    }

    public IBinder asHyperBinder() {
        return this.mHyperTelephonyStub;
    }

    public void setDefaultVoiceSlotId(int i, String str) {
        DefaultSimManager.getInstance().setDefaultVoiceSlotId(i, str);
    }

    public boolean setDefaultDataSlotId(int i, String str) {
        return DefaultSimManager.getInstance().setDefaultDataSlotId(i, str);
    }

    public int getSystemDefaultSlotId() {
        if (Binder.getCallingUid() == 1000) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return DefaultSimManager.getInstance().getSystemDefaultSlotId();
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        return DefaultSimManager.getInstance().getSystemDefaultSlotId();
    }

    public void recordTelephonyEvent(Bundle bundle) {
        ExternalProjectStatistic.recordTelephonyEvent(bundle);
    }

    public boolean isIccCardActivate(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return MiuiPhoneUtils.isIccCardActivated(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setIccCardActivate(int i, boolean z) {
        IccCardActivateHelper.setIccCardActivate(i, z);
    }

    public String getDeviceId(String str) {
        return this.mImeiMeidSource.getDeviceId(str);
    }

    public String getSmallDeviceId(String str) {
        return this.mImeiMeidSource.getSmallDeviceId(str);
    }

    public List getDeviceIdList(String str) {
        return this.mImeiMeidSource.getDeviceIdList(str);
    }

    public List getImeiList(String str) {
        return this.mImeiMeidSource.getImeiList(str);
    }

    public List getMeidList(String str) {
        return this.mImeiMeidSource.getMeidList(str);
    }

    public String getImei(int i, String str) {
        boolean zIsSupportSatelliteByDevice = isSupportSatelliteByDevice();
        ImeiMeidSource imeiMeidSource = this.mImeiMeidSource;
        if (zIsSupportSatelliteByDevice) {
            String ntnImei = imeiMeidSource.getNtnImei(this.mContext, i);
            ImeiMeidSource imeiMeidSource2 = this.mImeiMeidSource;
            if (ntnImei == null) {
                return imeiMeidSource2.getImei(i, str);
            }
            return imeiMeidSource2.getNtnImei(this.mContext, i);
        }
        return imeiMeidSource.getImei(i, str);
    }

    public String getImei(int i) {
        return this.mImeiMeidSource.getImei(i);
    }

    public String getMeid(int i, String str) {
        return this.mImeiMeidSource.getMeid(i, str);
    }

    public int getUiccSlotForCardId(String str) {
        return this.mUiccController.getUiccSlotForCardId(str);
    }

    public boolean isImsRegistered(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        boolean z = false;
        try {
            try {
                Phone phone = PhoneFactory.getPhone(i);
                MiuiImsControllerImpl miuiImsControllerImpl = (MiuiImsControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiImsControllerImpl.class, i);
                if (phone != null && miuiImsControllerImpl != null) {
                    if (phone.isImsRegistered() && miuiImsControllerImpl.isVolteEnabled()) {
                        z = true;
                    }
                    return z;
                }
            } catch (Exception e) {
                Rlog.d(LOG_TAG, "isImsRegistered exception=" + e);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isVideoTelephonyAvailable(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                Phone phone = PhoneFactory.getPhone(i);
                if (phone != null) {
                    return phone.isVideoEnabled();
                }
            } catch (Exception e) {
                Rlog.d(LOG_TAG, "isVideoTelephonyAvailable exception=" + e);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isWifiCallingAvailable(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                Phone phone = PhoneFactory.getPhone(i);
                MiuiImsControllerImpl miuiImsControllerImpl = (MiuiImsControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiImsControllerImpl.class, i);
                if (phone != null && miuiImsControllerImpl != null) {
                    return miuiImsControllerImpl.isVoWiFiEnabled();
                }
            } catch (Exception e) {
                Rlog.d(LOG_TAG, "isWifiCallingAvailable exception=" + e);
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isVolteEnabledByUser() {
        try {
            if (TelephonyManager.getDefault().isDualVolteSupported()) {
                for (int i = 0; i < PHONE_COUNT; i++) {
                    if (isVolteEnabledByUserForSlot(i)) {
                        return true;
                    }
                }
                return false;
            }
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            if (SubscriptionManager.isRealSlotId(defaultDataSlotId)) {
                return isVolteEnabledByUserForSlot(defaultDataSlotId);
            }
            return false;
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "isVolteEnabledByUser exception", e);
            return false;
        }
    }

    public boolean isVolteEnabledByUserForSlot(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ImsManager.getInstance(this.mContext, i).isEnhanced4gLteModeSettingEnabledByUser();
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "isVolteEnabledByUserForSlot exception", e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isVtEnabledByPlatform() {
        try {
            if (TelephonyManager.getDefault().isDualVolteSupported()) {
                for (int i = 0; i < PHONE_COUNT; i++) {
                    if (isVtEnabledByPlatformForSlot(i)) {
                        return true;
                    }
                }
                return false;
            }
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            if (SubscriptionManager.isRealSlotId(defaultDataSlotId)) {
                return isVtEnabledByPlatformForSlot(defaultDataSlotId);
            }
            return false;
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "isVtEnabledByPlatform exception", e);
            return false;
        }
    }

    public boolean isVtEnabledByPlatformForSlot(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ImsManager.getInstance(this.mContext, i).isVtEnabledByPlatform();
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "isVtEnabledByPlatformForSlot exception", e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isVolteEnabledByPlatform() {
        try {
            if (TelephonyManager.getDefault().isDualVolteSupported()) {
                for (int i = 0; i < PHONE_COUNT; i++) {
                    if (isVolteEnabledByPlatformForSlot(i)) {
                        return true;
                    }
                }
                return false;
            }
            int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
            if (SubscriptionManager.isRealSlotId(defaultDataSlotId)) {
                return isVolteEnabledByPlatformForSlot(defaultDataSlotId);
            }
            return false;
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "isVolteEnabledByPlatform exception", e);
            return false;
        }
    }

    public boolean isVolteEnabledByPlatformForSlot(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ImsManager.getInstance(this.mContext, i).isVolteEnabledByPlatform();
        } catch (Exception e) {
            Rlog.d(LOG_TAG, "isVolteEnabledByPlatformForSlot exception", e);
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isSameOperator(String str, String str2) {
        return ServiceProviderUtils.isSameServiceProvider(str, str2);
    }

    public String getSpn(String str, int i, String str2, boolean z) {
        Phone phone;
        if (TelephonyManager.isCustForJpSb() && "44000".equals(TelephonyManager.getDefault().getNetworkOperatorForSlot(i))) {
            Rlog.d(LOG_TAG, "Show SoftBank SPN is 44000");
            return TelephonyManager.getDefault().getNetworkOperatorForSlot(i);
        }
        IccRecords iccRecords = this.mUiccController.getIccRecords(i, 2);
        if (iccRecords != null && (ServiceProviderUtils.isChinaTelecom(iccRecords.getOperatorNumeric()) || ServiceProviderUtils.isMacauTelecom(iccRecords.getOperatorNumeric()))) {
            str = iccRecords.getOperatorNumeric();
        }
        if (HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "spn_show_pnn_or_operator_name", false) && (phone = PhoneFactory.getPhone(i)) != null && phone.getIccRecords() != null && phone.getServiceState() != null) {
            String pnnHomeName = phone.getIccRecords().getPnnHomeName();
            if (!TextUtils.isEmpty(pnnHomeName)) {
                Rlog.d(LOG_TAG, "getSpn pnn=" + pnnHomeName);
                return pnnHomeName;
            }
            boolean z2 = phone.getServiceState().getDataRoamingType() == 3;
            String operatorAlpha = phone.getServiceState().getOperatorAlpha();
            Rlog.d(LOG_TAG, "getSpn isInternationalRoaming =" + z2 + "; operatorName=" + operatorAlpha);
            if (!TextUtils.isEmpty(operatorAlpha) && !z2) {
                return operatorAlpha;
            }
        }
        if (HyperOSCustFeatureResolve.getBooleanForPhoneId(i, "spn_show_sim_slot_id", false)) {
            return String.format(this.mContext.getResources().getString(286196648), Integer.valueOf(i + 1));
        }
        return ServiceProviderUtils.get(str, i, str2, z);
    }

    public String onOperatorNumericOrNameSet(int i, String str, String str2) {
        return ServiceProviderUtils.onOperatorNumericOrNameSet(i, str, str2);
    }

    public static int getPhoneCount() {
        return PHONE_COUNT;
    }

    public boolean canReadPhoneState(String str, String str2) {
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", str2);
            return true;
        } catch (SecurityException unused) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PHONE_STATE", str2);
            return this.mAppOps.noteOp(51, Binder.getCallingUid(), str) == 0;
        }
    }

    public static boolean checkIfCallerIsSelfOrForegroundUser() {
        boolean z = Binder.getCallingUid() == Process.myUid();
        if (!z) {
            int callingUserId = UserHandle.getCallingUserId();
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    boolean z2 = ActivityManager.getCurrentUser() == callingUserId;
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    z = z2;
                } catch (Exception e) {
                    Rlog.e(LOG_TAG, "checkIfCallerIsSelfOrForegoundUser: Exception ex=" + e);
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                }
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        Rlog.d(LOG_TAG, "checkIfCallerIsSelfOrForegoundUser: ret=" + z);
        return z;
    }

    public Bundle getCellLocationForSlot(int i, String str) {
        Phone phone;
        try {
            this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION", null);
        } catch (SecurityException unused) {
            this.mContext.enforceCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION", "getCellLocationForSlot");
        }
        if (this.mAppOps.noteOp(0, Binder.getCallingUid(), str) != 0 || !checkIfCallerIsSelfOrForegroundUser() || (phone = PhoneFactory.getPhone(i)) == null) {
            return null;
        }
        Bundle bundle = new Bundle();
        phone.getCurrentCellIdentity().asCellLocation().fillInNotifierBundle(bundle);
        return bundle;
    }

    public boolean isGwsdSupport() {
        return SystemProperties.getInt("ro.vendor.mtk_gwsd_support", 0) == 1 && !Build.IS_INTERNATIONAL_BUILD;
    }

    public boolean isCellularNetworkSupport() {
        return MiuiPhoneUtils.isSupportSmartNetworkGameOptimization() || MiuiPhoneUtils.isSupportQcGameResidentCellOptimization();
    }

    public void setCallForwardingOption(int i, int i2, int i3, String str, ResultReceiver resultReceiver) {
        enforceModifyPermission();
        Phone phone = PhoneFactory.getPhone(i);
        if (isUtNotAllowedAsDataDisabled(phone)) {
            resultReceiver.send(-3, null);
        } else if (phone.getPhoneType() == 2 && !phone.isUtEnabled()) {
            resultReceiver.send(-2, null);
        } else {
            phone.setCallForwardingOption(i2, i3, str, 0, this.mHandler.obtainMessage(1, resultReceiver));
        }
    }

    public int getPhoneType(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() != 1001) {
            MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call getPhoneType.");
            return 0;
        }
        if (NetworkModeManager.getInstance() != null) {
            return NetworkModeManager.getPhoneType(i);
        }
        return 0;
    }

    public boolean isRemoveNetworkModeSettings(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.isRemoveNetworkModeSettings(i);
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isRemoveNetworkModeSettings.");
        return false;
    }

    public boolean isDisableLte(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.isDisableLte();
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isDisableLte.");
        return false;
    }

    public boolean isRemove2GOnly(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.isRemove2GOnly(i);
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isRemove2GOnly.");
        return false;
    }

    public boolean isRemovePrefer3G(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.isRemovePrefer3G(i);
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isRemovePrefer3G.");
        return false;
    }

    public boolean isRemoveTdScdma(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.isRemoveTdScdma(i);
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isRemoveTdScdma.");
        return false;
    }

    public boolean isRemoveCdma(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.isRemoveCdma(i);
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isRemoveCdma.");
        return false;
    }

    public boolean showWcdmaOnlyNetworkModes(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.showWcdmaOnlyNetworkModes(i);
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call showWcdmaOnlyNetworkModes.");
        return false;
    }

    public boolean isShowCdmaUnavailable(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.isShowCdmaUnavailable();
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isShowCdmaUnavailable.");
        return false;
    }

    public void setNetworkModeInDb(String str, int i, int i2) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() != 1001) {
            MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call setNetworkModeInDb.");
        } else if (NetworkModeManager.getInstance() != null) {
            NetworkModeManager.setNetworkModeInDb(i, i2);
        }
    }

    public boolean isNetworkModeLteOnly(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() == 1001) {
            return NetworkModeManager.getInstance() != null && NetworkModeManager.isNetworkModeLteOnly(i);
        }
        MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isNetworkModeLteOnly.");
        return false;
    }

    public int getFiveGNetworkMode(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() != 1001) {
            MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call getFiveGNetworkMode.");
            return 0;
        }
        if (NetworkModeManager.getInstance() != null) {
            return NetworkModeManager.getFiveGNetworkMode();
        }
        return 0;
    }

    public int[] getDeviceSupportModes(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() != 1001) {
            MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call getDeviceSupportModes.");
            return null;
        }
        int[][] deviceSupportModes = NetworkMode.getInstance().getDeviceSupportModes();
        if (deviceSupportModes != null) {
            return deviceSupportModes[i];
        }
        return new int[0];
    }

    public int[] getSupportModes(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() != 1001) {
            MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call getSupportModes.");
            return null;
        }
        int[][] supportModes = NetworkMode.getInstance().getSupportModes();
        if (supportModes != null) {
            return supportModes[i];
        }
        return new int[0];
    }

    private boolean isUtNotAllowedAsDataDisabled(Phone phone) {
        return (phone == null || phone.getIccCard() == null || phone.getIccCard().getIccRecords() == null || !ServiceProviderUtils.isChineseServiceProvider(phone.getIccCard().getIccRecords().getOperatorNumeric()) || (!phone.isImsRegistered() && !phone.isUtEnabled()) || MobileDataUtils.getInstance().isMobileEnable(this.mContext)) ? false : true;
    }

    public void enforceModifyPermission() {
        if (TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", null);
    }

    public void setUserFiveGEnabled(boolean z, int i) {
        enforcePhonePermission();
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserFiveGEnabled(z, i);
        }
    }

    public boolean isUserFiveGEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isUserFiveGEnabled(i);
        }
        return false;
    }

    public boolean isUserFiveGSaEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isUserFiveGSaEnabled(i);
        }
        return false;
    }

    public void setUserFiveGSaEnabled(boolean z, int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserFiveGSaEnabled(z, i);
        }
    }

    public void setDualSaEnabled(boolean z) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setDualSaEnabled(z);
        }
    }

    public boolean isDualSaEnabled() {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isDualSaEnabled();
        }
        return false;
    }

    public boolean isVoNREnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isVoNREnabled(i);
        }
        return false;
    }

    public void setUserVoNREnabled(int i, boolean z) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserVoNREnabled(i, z);
        }
    }

    public void setUserNrCAEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().execSetFiveGCarrierAggregationMode(i, i2);
        }
    }

    public boolean isNrCAEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().getFiveGCarrierAggregationStatus(i);
        }
        return false;
    }

    public void setCrbtDisable(boolean z) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setCrbtDisable(z);
        }
    }

    public boolean shouldDisplayCrbtButton() {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().shouldDisplayCrbtButton();
        }
        return false;
    }

    public int getNrConfigType(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().getNrConfigType(i);
        }
        return -1;
    }

    public int getXMNetworkType(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().getXMNetworkType(i);
        }
        return -1;
    }

    public boolean isFiveGCapable() {
        if (!FiveGManagerBase.isFiveGCapable()) {
            return false;
        }
        FiveGManagerBase.getInstance();
        return FiveGManagerBase.isFiveGCapable();
    }

    public int get5GIndicatorStatus(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().get5GIndicatorStatus(i);
        }
        return -1;
    }

    public int getUpperlayerStatus(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().getUpperlayerStatus(i);
        }
        return -1;
    }

    public int getRestrictDcnrStatus(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().getRestrictDcnrStatus(i);
        }
        return -1;
    }

    public boolean isGameFiveGOptimizeSupported() {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isGameFiveGOptimizeSupported();
        }
        return false;
    }

    public boolean isDualNrEnabled() {
        if (!FiveGManagerBase.isFiveGCapable()) {
            return false;
        }
        FiveGManagerBase.getInstance();
        return FiveGManagerBase.isDualNrEnabled();
    }

    public String getMobileNetworkCapability(int i) {
        if (!FiveGManagerBase.isFiveGCapable()) {
            return "";
        }
        return FiveGManagerBase.getInstance().getMobileNetworkCapability(i);
    }

    public void setMobileNetworkCapability(String str) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setMobileNetworkCapability(str);
        }
    }

    public int setEsimState(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
            if (miuiEsimManagerBase != null) {
                return miuiEsimManagerBase.setEsimState(i);
            }
            return -1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int getEsimGPIOState() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
            if (miuiEsimManagerBase != null) {
                return miuiEsimManagerBase.getEsimGPIOState();
            }
            return -1;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getProductEid() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
            if (miuiEsimManagerBase != null) {
                return miuiEsimManagerBase.getProductEid();
            }
            return "";
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isEsimActive() {
        MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
        if (miuiEsimManagerBase != null) {
            return miuiEsimManagerBase.isEsimActive();
        }
        return false;
    }

    public boolean isSupportEuiccBinding() {
        return MiuiPhoneUtils.isSupportEuiccBinding();
    }

    public boolean shouldRestrictNetworkForEuiccBinding(int i) {
        return i == 1 && isEsimActive() && isSupportEuiccBinding();
    }

    public boolean isSupportEsimForCountry(String str) {
        MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
        if (miuiEsimManagerBase != null) {
            return miuiEsimManagerBase.isSupportEsimForCountry(str);
        }
        return false;
    }

    public List getAllEsimProfiles() {
        MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
        if (miuiEsimManagerBase != null) {
            return miuiEsimManagerBase.getAllEsimProfiles();
        }
        return null;
    }

    public String[] openChannelEsim() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
            if (miuiEsimManagerBase != null) {
                return miuiEsimManagerBase.openChannelEsim();
            }
            return new String[]{""};
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String[] closeChannelEsim(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
            if (miuiEsimManagerBase != null) {
                return miuiEsimManagerBase.closeChannelEsim(i);
            }
            return new String[]{""};
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String[] sendApduEsim(int i, String str) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
            if (miuiEsimManagerBase != null) {
                return miuiEsimManagerBase.sendApduEsim(i, str);
            }
            return new String[]{""};
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean resetEsimMemoryForJP() {
        if (IS_QCOM && Binder.getCallingUid() == 1000) {
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            try {
                return resetEsimMemoryForJPInner();
            } finally {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        }
        return resetEsimMemoryForJPInner();
    }

    private boolean resetEsimMemoryForJPInner() {
        MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
        if (miuiEsimManagerBase != null) {
            return miuiEsimManagerBase.resetEsimMemoryForJP();
        }
        return false;
    }

    public String processBindingData(List list) {
        MiuiDeviceEuiccBindingMgrBase miuiDeviceEuiccBindingMgrBase = MiuiDeviceEuiccBindingMgrBase.getInstance();
        if (miuiDeviceEuiccBindingMgrBase != null) {
            return miuiDeviceEuiccBindingMgrBase.processBindingData(list);
        }
        return null;
    }

    public boolean isPsim2EsimAvailable(int i) {
        MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
        if (miuiEsimManagerBase != null) {
            return miuiEsimManagerBase.isPsim2EsimAvailable(i);
        }
        return false;
    }

    public boolean isRemoveEsimSwitch() {
        return MiuiEsimManagerBase.isRemoveEsimSwitch();
    }

    public String getCotaOpconfigVersionName() {
        return CotaOpconfigManager.getInstance().getCotaOpconfigVersionName();
    }

    public long getCotaOpconfigVersionCode() {
        return CotaOpconfigManager.getInstance().getCotaOpconfigVersionCode();
    }

    public boolean isCotaOpconfigUsed() {
        return CotaOpconfigManager.getInstance().isCotaOpconfigUsed();
    }

    public boolean setCotaOpconfigUsed(boolean z) {
        return CotaOpconfigManager.getInstance().setCotaOpconfigUsed(z);
    }

    public void enforcePhonePermission() {
        if (TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            return;
        }
        this.mContext.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", null);
    }

    public boolean isDsdaEnabled() {
        return MiuiDsdaManager.getInstance().isDsdaEnabled();
    }

    public void setDsdaEnabled(boolean z, int i) {
        MiuiDsdaManager.getInstance().setDsdaEnabled(z, i);
    }

    /* JADX WARN: Code duplicated, block: B:12:0x001a  */
    public boolean isConcurrentCallsPossible() {
        boolean z = true;
        boolean z2 = false;
        if (IS_MTK || IS_XRING) {
            int i = SystemProperties.getInt("vendor.radio.dsda.state", 4);
            if (i > 100) {
                i /= 100;
            }
            if (i != 2 && i != 3) {
                z = false;
            }
            z2 = z;
        } else if (IS_QCOM) {
            if (3 != SystemProperties.getInt("ril.multisim.voice_capability", 0)) {
                z = false;
            }
            z2 = z;
        }
        Rlog.d(LOG_TAG, "isConcurrentCallsPossible " + z2);
        return z2;
    }

    public Intent getFeatureInfoIntentByCloud(String str) {
        FiveGCloudController.getInstance();
        return FiveGCloudController.getFeatureInfoIntentByCloud(str);
    }

    public boolean isFeatureDisabledByCloud(String str) {
        FiveGCloudController.getInstance();
        return FiveGCloudController.isFeatureDisabledByCloud(str);
    }

    public boolean isFeatureEnabledByCloud(String str) {
        FiveGCloudController.getInstance();
        return FiveGCloudController.isFeatureEnabledByCloud(str);
    }

    public boolean shouldHideIntelligentDualSimButton() {
        return MiuiPhoneUtils.shouldHideIntelligentDualSimButton();
    }

    public String getCdmaImsi(int i) {
        if (TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            return MiuiPhoneUtils.getCdmaImsi(i);
        }
        return null;
    }

    public String getCellId(int i) {
        if (TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            return MiuiPhoneUtils.getCellId(i);
        }
        return null;
    }

    public long getLteCellId(int i) {
        if (TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            return MiuiPhoneUtils.getLteCellId(i);
        }
        return -1L;
    }

    public int getLteLac(int i) {
        if (TelephonyManager.checkCallingOrSelfPermissionGranted(Binder.getCallingUid())) {
            return MiuiPhoneUtils.getLteLac(i);
        }
        return -1;
    }

    public int getPciByPhoneId(int i) {
        return MiuiPhoneUtils.getPciByPhoneId(i);
    }

    public String getCloudEccData() {
        return CloudEccController.getCloudEccData();
    }

    public Bundle getPhoneStatus(int i, String str, boolean z) {
        if (Binder.getCallingUid() != 1001) {
            Rlog.d(LOG_TAG, "uid  " + Binder.getCallingUid() + " is not allowed");
            return null;
        }
        return MiuiPhoneUtils.getPhoneStatus(i, z);
    }

    public int callQcrilHookGet(String str, int i) {
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit == null) {
            return 0;
        }
        int iCallQcrilHookGet = modemUtilsInit.callQcrilHookGet(str, i);
        Rlog.d(LOG_TAG, "callQcrilHookGet: method=" + str + ", phoneId=" + i + ", result=" + iCallQcrilHookGet);
        return iCallQcrilHookGet;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (Binder.getCallingUid() != 1001) {
            return;
        }
        printWriter.println("MiuiPhoneInterfaceManager:");
        this.mImeiMeidSource.dump(printWriter);
        DefaultSimManager.getInstance().dump(fileDescriptor, printWriter, strArr);
        if (TelephonyManager.isCarrierCotaSupported()) {
            CotaOpconfigManager.getInstance().dump(fileDescriptor, printWriter, strArr);
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            printWriter.println("Phone[" + phone.getPhoneId() + "]:");
            MiuiIccPhoneBookInterfaceManager miuiIccPhoneBookInterfaceManager = ((MiuiUiccControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiUiccControllerImpl.class, phone.getPhoneId())).getMiuiIccPhoneBookInterfaceManager();
            if (miuiIccPhoneBookInterfaceManager != null) {
                miuiIccPhoneBookInterfaceManager.dump(fileDescriptor, printWriter, strArr);
            }
        }
    }

    public boolean setSatelliteEnable(int i, boolean z) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSatelliteEnable(i, z);
    }

    public int getSatelliteState(int i) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getSatelliteState(i);
    }

    public boolean isSatelliteButtonEditable() {
        return MiuiPhoneUtils.isSatelliteButtonEditable(this.mContext);
    }

    public boolean isSupportSatelliteByCarrier() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            int phoneCount = getPhoneCount();
            boolean z = SystemProperties.getBoolean("persist.radio.satellite.sim.enabled", false);
            for (int i = 0; i < phoneCount; i++) {
                Phone phone = MiuiPhoneUtils.getPhone(i);
                boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_satellite_support", false);
                if (z && phone.getIccCard().hasIccCard()) {
                    Rlog.d(LOG_TAG, "Allow all cards to use satellite is opened.");
                    return true;
                }
                if (booleanConfigForSubId) {
                    return true;
                }
            }
            return false;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isSupportSatelliteByDevice() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            boolean z = true;
            boolean z2 = this.mContext.getResources().getBoolean(R.bool.config_support_satellite) || SystemProperties.getBoolean("ro.tiantong", false);
            String str = SystemProperties.get("ro.product.model", "");
            Rlog.d(LOG_TAG, "support satellite device=" + z2 + ", model = " + str);
            if (Build.IS_INTERNATIONAL_BUILD || (!z2 && !"2311BPN23C".equals(str))) {
                z = false;
            }
            return z;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isSupportSatelliteSignalBoost() {
        boolean z = this.mContext.getResources().getBoolean(R.bool.config_support_satellite_signal_boost);
        Rlog.d(LOG_TAG, "support satellite signal boost device=" + z);
        return !Build.IS_INTERNATIONAL_BUILD && z;
    }

    public Map compensateSatAntenna(float f, float f2, float f3, float f4) {
        return MiuiPhoneUtils.compensateSatAntenna(f, f2, f3, f4);
    }

    public boolean isSatelliteEnabled() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteEnabled();
    }

    public boolean isHighPenetrationEnabled() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isHighPenetrationEnabled();
    }

    public int getEnabledSatelliteSlotId() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getEnabledSatelliteSlotId();
    }

    public void setSatelliteEnableByUser(boolean z, int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSatelliteEnableByUser(this.mContext, z, i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setSatelliteDataEnableByUser(boolean z) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSatelliteDataEnableByUser(this.mContext, z);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isSatelliteDataEnabled() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteDataEnabled();
    }

    public void setSatelliteSignalBoostEnable(boolean z, int i) {
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSatelliteSignalBoostEnable(z, i);
    }

    public boolean isSatelliteSignalBoostEnabled() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSatelliteSignalBoostEnabled();
    }

    class MiuiPhoneInterfaceManagerHandler extends Handler {
        public MiuiPhoneInterfaceManagerHandler(MiuiPhoneInterfaceManager miuiPhoneInterfaceManager, Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Rlog.d(MiuiPhoneInterfaceManager.LOG_TAG, "Received message:" + message.what);
            if (message.what != 1) {
                return;
            }
            Object obj = message.obj;
            if (obj instanceof AsyncResult) {
                AsyncResult asyncResult = (AsyncResult) obj;
                ResultReceiver resultReceiver = (ResultReceiver) asyncResult.userObj;
                int i = asyncResult.exception != null ? -1 : 0;
                if (resultReceiver != null) {
                    resultReceiver.send(i, null);
                }
            }
        }
    }

    public void requestNetworkBySlot(int i) {
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).requestNetworkBySlot(i);
    }

    public void releaseNetworkBySlot(int i) {
        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).releaseNetworkBySlot(i);
    }

    public boolean isDsdaFullConcurrencyState() {
        if (MiuiDsdaStatistic.getInstance() != null) {
            return MiuiDsdaStatistic.getInstance().isDsdaFullConcurrencyState();
        }
        return false;
    }

    public boolean isDsdaFullOrTxsConcurrencyState() {
        if (!IS_QCOM || MiuiDsdaStatistic.getInstance() == null) {
            return false;
        }
        return MiuiDsdaStatistic.getInstance().isDsdaFullOrTxsConcurrencyState();
    }

    public void setVoiceAndDataAllowed(boolean z) {
        this.mIsVoiceAndDataAllowed = z;
    }

    public boolean getVoiceAndDataAllowed() {
        return this.mIsVoiceAndDataAllowed;
    }

    public boolean is5gNewCallFeatureEnabled() {
        return MiuiNewCallUtils.isNewCallFeatureEnabled();
    }

    public boolean isSatelliteInThremalState() {
        return MiuiPhoneUtils.isSatelliteInThremalState();
    }

    public int startTcpMonitorRegister(String str, int i, String str2, int i2, int i3) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return MiuiPhoneUtils.startTcpMonitorRegister(str, i, str2, i2, i3);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int stopTcpMonitorRegister(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return MiuiPhoneUtils.stopTcpMonitorRegister(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int startTcpKeepAlive(String str, int i, String str2, int i2, int i3, int i4) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return MiuiPhoneUtils.startTcpKeepAlive(str, i, str2, i2, i3, i4);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int stopTcpKeepAlive(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return MiuiPhoneUtils.stopTcpKeepAlive(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void registerModemTcpKaCallback(IModemTcpKeepAliveIndCallback iModemTcpKeepAliveIndCallback) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            MiuiPhoneUtils.registerModemTcpKaCallback(iModemTcpKeepAliveIndCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void unregisterModemTcpKaCallback(IModemTcpKeepAliveIndCallback iModemTcpKeepAliveIndCallback) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            MiuiPhoneUtils.unregisterModemTcpKaCallback(iModemTcpKeepAliveIndCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isModemTcpKaSupported() {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return MiuiPhoneUtils.isDeviceModemTcpKaSupported();
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isSupportSatelliteEarpiece() {
        boolean z = this.mContext.getResources().getBoolean(R.bool.config_satellite_support_earpiece);
        Rlog.d(LOG_TAG, "support satellite earpiece=" + z);
        return z;
    }

    public void setCalibrationInformation(Bundle bundle) {
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setCalibrationInformation(bundle);
    }

    public int getNtnSnr() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getRetSnr();
    }

    public int getNtnTxPwr() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getRealTxPwr();
    }

    public int getNtnBeamId() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getNtnBeamId();
    }

    public String sendCitRequest(int i, int i2, String str) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).citRequest(i, i2, str);
    }

    public void sendCitCmdRequest(int i, int i2, String str, ResultReceiver resultReceiver) {
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).sendCitRequest(i, i2, str, resultReceiver);
    }

    public void recoverNtnNetwork() {
        if (isSatelliteBox()) {
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).recoverNtnNetwork();
        }
    }

    public void stopCitTest() {
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).stopCitTest();
    }

    public boolean isFiveGAEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isFiveGAEnabled(i);
        }
        return false;
    }

    public void setUserFiveGAEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserFiveGAEnabled(i, i2);
        }
    }

    public boolean isFiveGA4CCEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isFiveGA4CCEnabled(i);
        }
        return false;
    }

    public void setUserFiveGA4CCEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserFiveGA4CCEnabled(i, i2);
        }
    }

    public int getRoamingModeForPhoneId(int i) {
        if (RoamingManager.getInstance() == null) {
            return super.getRoamingModeForPhoneId(i);
        }
        return RoamingManager.getInstance().getRoamingModeForPhoneId(i);
    }

    public int[] getRoamingMode() {
        if (RoamingManager.getInstance() == null) {
            return super.getRoamingMode();
        }
        return RoamingManager.getInstance().getRoamingMode();
    }

    public boolean isHOMEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isHOMEnabled(i);
        }
        return false;
    }

    public void setUserHOMEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserHOMEnabled(i, i2);
        }
    }

    public boolean isSwulEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isSwulEnabled(i);
        }
        return false;
    }

    public void setUserSwulEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserSwulEnabled(i, i2);
        }
    }

    public boolean isR16PowerEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isR16PowerEnabled(i);
        }
        return false;
    }

    public void setUserR16PowerEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserR16PowerEnabled(i, i2);
        }
    }

    public void setUserNetSlicingEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserNetSlicingEnabled(i, i2);
        }
    }

    public boolean isSignalOptimizationSupported() {
        return MiuiPhoneUtils.isImsSignalOptimizationSupported(this.mContext);
    }

    public boolean isFullPowerModeEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isFullPowerModeEnabled(i);
        }
        return false;
    }

    public void setFullPowerModeEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setFullPowerModeEnabled(i, i2);
        }
    }

    public boolean isConditionalHandoverEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isConditionalHandoverEnabled(i);
        }
        return false;
    }

    public void setConditionalHandoverEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setConditionalHandoverEnabled(i, i2);
        }
    }

    public int get3GPPVersion(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().get3GPPVersion(i);
        }
        return -1;
    }

    public void set3GPPVersion(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().set3GPPVersion(i, i2);
        }
    }

    public boolean isSulEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isSulEnabled(i);
        }
        return false;
    }

    public void setSulEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setSulEnabled(i, i2);
        }
    }

    public boolean isUlCaEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isUlCaEnabled(i);
        }
        return false;
    }

    public void setUlCaEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUlCaEnabled(i, i2);
        }
    }

    public boolean isWUSEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isWUSEnabled(i);
        }
        return false;
    }

    public void setUserWUSEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserWUSEnabled(i, i2);
        }
    }

    public boolean isRRCSegmentationEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isRRCSegmentationEnabled(i);
        }
        return false;
    }

    public void setUserRRCSegmentationEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserRRCSegmentationEnabled(i, i2);
        }
    }

    public boolean isRRCInactiveEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isRRCInactiveEnabled(i);
        }
        return false;
    }

    public void setUserRRCInactiveEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserRRCInactiveEnabled(i, i2);
        }
    }

    public boolean isBWPSwitchEnabled(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGManagerBase.getInstance().isBWPSwitchEnabled(i);
        }
        return false;
    }

    public void setUserBWPSwitchEnabled(int i, int i2) {
        if (FiveGManagerBase.isFiveGCapable()) {
            FiveGManagerBase.getInstance().setUserBWPSwitchEnabled(i, i2);
        }
    }

    public Map getEmergencyNumberList() {
        Rlog.d(LOG_TAG, "getEmergencyNumberList");
        HashMap map = new HashMap();
        try {
            for (Phone phone : PhoneFactory.getPhones()) {
                if (phone.getEmergencyNumberTracker() != null && phone.getEmergencyNumberTracker().getEmergencyNumberList() != null) {
                    List emergencyNumberList = phone.getEmergencyNumberTracker().getEmergencyNumberList();
                    String emergencyCountryIso = phone.getEmergencyNumberTracker().getEmergencyCountryIso();
                    if (!TextUtils.isEmpty(emergencyCountryIso) && emergencyNumberList.size() > 0) {
                        List arrayList = new ArrayList(emergencyNumberList);
                        EmergencyNumber emergencyNumber = (EmergencyNumber) arrayList.get(arrayList.size() - 1);
                        arrayList.remove(emergencyNumber);
                        arrayList.add(new EmergencyNumber(emergencyNumber.getNumber(), emergencyCountryIso.toLowerCase(Locale.ROOT), emergencyNumber.getMnc(), emergencyNumber.getEmergencyServiceCategoryBitmask(), emergencyNumber.getEmergencyUrns(), emergencyNumber.getEmergencyNumberSourceBitmask(), emergencyNumber.getEmergencyCallRouting()));
                        emergencyNumberList = arrayList;
                    }
                    map.put(Integer.valueOf(phone.getSubId()), emergencyNumberList);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return map;
        }
    }

    public String findBestTZInMultiZones(String str, int i, List list, long j) {
        return ((MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, i)).findBestTZInMultiZones(str, list, j);
    }

    public boolean isAllowGeolocationTimeZoneDetection(List list, long j) {
        return ((MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, 0)).isAllowGeolocationTimeZoneDetection(list, j);
    }

    public boolean getBDServiceStatus(int i) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBdServiceStatus(i);
    }

    public int getPhoneBDServiceState() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getPhoneBdServiceState();
    }

    public int getBDSysStatus() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBdSysStatus();
    }

    public int sendBDMessage(int i, int[] iArr, String str, int i2, List list) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSlotId(i2);
            return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).sendBDMessage(i, iArr, str, i2, list);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int sendBDMessageWithLocation(int i, int[] iArr, String str, int i2, List list, double d, double d2) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSlotId(i2);
            return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).sendBDMessageWithLocation(i, iArr, str, i2, list, d, d2);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public int sendBDAudioMessage(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).sendBDAudioMessage(i, iArr, uri, uri2, i2, list);
    }

    public int sendBDAudioMessageWithLocation(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, double d, double d2) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).sendBDAudioMessageWithLocation(i, iArr, uri, uri2, i2, list, d, d2);
    }

    public int sendBDPictureMessage(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).sendBDPictureMessage(i, iArr, uri, uri2, i2, list);
    }

    public int sendBDPictureMessageWithLocation(int i, int[] iArr, Uri uri, Uri uri2, int i2, List list, double d, double d2) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).sendBDPictureMessageWithLocation(i, iArr, uri, uri2, i2, list, d, d2);
    }

    public int receiveBDMessage(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).setSlotId(i);
            return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).receiveBDMessage(i);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void startBdService(int i) {
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).startConnectSatelliteService(i);
    }

    public void closeBdService() {
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).closeBdService();
    }

    public int getAllowedSendTime() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getAllowedBDSmsSendTime();
    }

    public int getAllowedReceiveTime() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getAllowedBDSmsReceiveTime();
    }

    public int getRemainMsgCount(int i) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getRemainBDSmsCount(i);
    }

    public boolean isBDEnabledByUser() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBdSwitchState();
    }

    public void syncBDMail(int i) {
        ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).syncBDMail(i);
    }

    public int preLoad(int i) {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).preLoad(i);
    }

    public boolean isSupportedBDPicture() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSupportedBDPicture();
    }

    public boolean isSupportedBDAudio() {
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).isSupportedBDAudio();
    }

    public boolean isSupportBeiDouByDevice() {
        boolean z = this.mContext.getResources().getBoolean(R.bool.config_support_beidou);
        boolean z2 = SystemProperties.getBoolean("ro.bdsms", false);
        Rlog.d(LOG_TAG, "isSwSupportBeiDou =" + z + ", isHwSupportBeiDou = " + z2);
        return !Build.IS_INTERNATIONAL_BUILD && z && z2;
    }

    public boolean isSupportBeiDouByCarrier() {
        int phoneCount = getPhoneCount();
        for (int i = 0; i < phoneCount; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_beidou_support", false)) {
                return true;
            }
        }
        return false;
    }

    public boolean isShowSatelliteSetting() {
        return MiuiPhoneUtils.shouldShowSatelliteButton(this.mContext) || MiuiPhoneUtils.shouldShowBeiDouButton(this.mContext);
    }

    public boolean isDataOnlySlot(int i) {
        return MiuiPhoneUtils.isDataOnlySlot(i);
    }

    public void restartRILD(String str, int i, int i2) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() != 1001) {
            MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call restartRILD.");
        } else {
            PhoneAdapter.getInstance().restartRILD(this.mContext, i, i2);
        }
    }

    public boolean isVoiceLinkRegistered(String str, int i) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() != 1001) {
            MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call isVoiceLinkRegistered.");
            return false;
        }
        return PhoneAdapter.getInstance().isVoiceLinkRegistered(this.mContext, i);
    }

    public boolean isSupportSatelliteXingwangByCarrier() {
        int phoneCount = getPhoneCount();
        for (int i = 0; i < phoneCount; i++) {
            Phone phone = MiuiPhoneUtils.getPhone(i);
            boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_satellite_xingwang_support", false);
            Rlog.d(LOG_TAG, "isSupportSatelliteXingwangByCarrier i = " + i + ", isSupportSatellite = " + booleanConfigForSubId);
            if (booleanConfigForSubId) {
                return true;
            }
        }
        return false;
    }

    public boolean isSupportSatelliteXingwangByDevice() {
        boolean z = this.mContext.getResources().getBoolean(R.bool.config_satellite_xingwang_supported_by_device);
        Rlog.d(LOG_TAG, "isSupportSatelliteXingwangByDevice isSupportSatellite =" + z);
        return !Build.IS_INTERNATIONAL_BUILD && z;
    }

    public int getPitchOfSatelliteAntenna(boolean z) {
        int i;
        if (z) {
            i = R.integer.config_pitch_antenna_compensation_fold;
        } else if (((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBeanSwitchedStatus() == 1) {
            i = R.integer.config_front_screen_pitch_antenna_compensation;
        } else {
            i = R.integer.config_pitch_antenna_compensation;
        }
        return this.mContext.getResources().getInteger(i);
    }

    public int getAzimuthOfSatelliteAntenna(boolean z) {
        int i;
        if (z) {
            i = R.integer.config_azimuth_antenna_compensation_fold;
        } else if (((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).getBeanSwitchedStatus() == 1) {
            i = R.integer.config_front_screen_azimuth_antenna_compensation;
        } else {
            i = R.integer.config_azimuth_antenna_compensation;
        }
        return this.mContext.getResources().getInteger(i);
    }

    public Map sendRequestForResult(String str, int i, Map map) {
        this.mAppOps.checkPackage(Binder.getCallingUid(), str);
        if (Binder.getCallingUid() != 1001) {
            MiuiPhoneInterfaceManager$$ExternalSyntheticBUOutline0.m(str, " does not have permission to call sendRequestForResult.");
            return null;
        }
        return new HashMap();
    }

    public String getGameCellResidenceAndLagTime() {
        return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getGameCellResidenceAndLagTime();
    }

    public boolean registSatelliteLocationCallback(IRemoteCallback iRemoteCallback, int i, int i2) {
        if (this.mContext.checkCallingOrSelfPermission("xiaomi.permission.USE_SATELLITE_LOCATION_SERVICE") != 0) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("registSatelliteLocationCallback requires USE_SATELLITE_LOCATION_SERVICE");
            return false;
        }
        return ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).registSatelliteLocationCallback(iRemoteCallback, i, i2);
    }

    public void unRegistSatelliteLocationCallback(IRemoteCallback iRemoteCallback) {
        if (this.mContext.checkCallingOrSelfPermission("xiaomi.permission.USE_SATELLITE_LOCATION_SERVICE") != 0) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("unRegistSatelliteLocationCallback requires USE_SATELLITE_LOCATION_SERVICE");
        } else {
            ((ISatelliteModule) ModuleDelegate.get(ISatelliteModule.class)).unRegistSatelliteLocationCallback(iRemoteCallback);
        }
    }

    public boolean isChinaMobileSim(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null) {
            return MiuiPhoneUtils.isCmccSim(phone);
        }
        return false;
    }

    public boolean isChinaTelecomSim(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null) {
            return MiuiPhoneUtils.isChinaTelecomSim(phone);
        }
        return false;
    }

    public boolean isChinaUnicomSim(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null) {
            return MiuiPhoneUtils.isChinaUnicomSim(phone);
        }
        return false;
    }

    public String getModemProvider() {
        return ModemProvider.PROPERTY_MODEM_PROVIDER;
    }

    public void notifyEuiccBindingAuthFailure() {
        MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
        if (miuiEsimManagerBase != null) {
            miuiEsimManagerBase.showEuiccFailureAlertDialog();
        }
    }

    public boolean setFiveTupleBlackList(String str, List list, int i, ResultReceiver resultReceiver) {
        return XmTelephonyManagerInner.getDefault().setFiveTupleBlackList(str, list, i, resultReceiver);
    }

    public boolean isEsimEnabled() {
        MiuiEsimManagerBase miuiEsimManagerBase = MiuiEsimManagerBase.getInstance();
        if (miuiEsimManagerBase != null) {
            return miuiEsimManagerBase.isEsimEnabled();
        }
        return false;
    }

    public Bundle getModemQosParams(int i) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).getModemQosParams(i);
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "getModemQosParams exception", e);
            return null;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public String getRILMessageIDs(int i) {
        return ((IFiveGPowerModule) ModuleDelegate.get(IFiveGPowerModule.class)).getRILMessageIDs(i);
    }

    public boolean isFiveGARegistration(int i) {
        if (FiveGManagerBase.isFiveGCapable()) {
            return FiveGAStatistics.getInstance().isFiveGARegistration(i);
        }
        return false;
    }
}
