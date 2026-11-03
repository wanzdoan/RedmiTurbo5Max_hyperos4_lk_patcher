package com.hyper.internal.telephony;

import android.telephony.Rlog;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperCallManager;
import com.android.internal.telephony.IHyperCatManager;
import com.android.internal.telephony.IHyperDataManager;
import com.android.internal.telephony.IHyperEmergencyNumberTracker;
import com.android.internal.telephony.IHyperFrameworkTelephonyManager;
import com.android.internal.telephony.IHyperGsmCdmaCallTracker;
import com.android.internal.telephony.IHyperGsmCdmaPhone;
import com.android.internal.telephony.IHyperImsPhone;
import com.android.internal.telephony.IHyperNetworkTypeManager;
import com.android.internal.telephony.IHyperPhone;
import com.android.internal.telephony.IHyperServiceStateTracker;
import com.android.internal.telephony.IHyperSsManager;
import com.android.internal.telephony.IHyperTeleImsManager;
import com.android.internal.telephony.IHyperTeleSmsManager;
import com.android.internal.telephony.IHyperTimeZoneDetector;
import com.android.internal.telephony.IHyperUiccManager;
import com.android.internal.telephony.NetworkTypeController;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.common.IHyperCommonModule;
import com.android.internal.telephony.data.DataNetworkController;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.nitz.TimeServiceHelper;
import com.android.internal.telephony.nitz.TimeZoneSuggester;
import com.hyper.internal.telephony.emergency.HyperEmergencyNumberTrackerImpl;
import com.hyper.internal.telephony.imsphone.HyperImsPhoneImpl;
import com.hyper.internal.telephony.network.HyperNetworkTypeManagerImpl;

/* JADX INFO: loaded from: classes.dex */
public class HyperTelephonyFactoryImpl extends HyperTelephonyFactory {
    public <T extends IHyperCommonModule> T initModule(T t, Object... objArr) {
        try {
            verityParams(t);
            if (!HyperModuleManager.isSupport(t)) {
                return t;
            }
            switch (t.index().ordinal()) {
                case 1:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperPhone(objArr));
                case 2:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperGsmCdmaPhone(objArr));
                case 3:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperImsPhone(objArr));
                case 4:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperServiceStateTracker(objArr));
                case 5:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperGsmCdmaCallTracker(objArr));
                case 6:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperUiccManager(objArr));
                case 7:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperTimeZoneDetector(objArr));
                case 8:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperCatManager(objArr));
                case 9:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperCallManager(objArr));
                case 10:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperFrameworkTelephonyManager(objArr));
                case 11:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperDataManager(objArr));
                case 12:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperSsManager(objArr));
                case 13:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperTeleSmsManager(objArr));
                case 14:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperNetworkTypeManager(objArr));
                case 15:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperTeleImsManager(objArr));
                case 16:
                    return (T) HyperModuleManager.getTraceMonitor(getHyperEmergencyNumberTracker(objArr));
                default:
                    loge("Unknow Module:" + t.index().name());
                    return t;
            }
        } catch (Exception e) {
            loge("Unknow Module:" + t.index().name() + ",for Exception:" + e);
            e.printStackTrace();
            throw new IllegalStateException("for Exception:" + e);
        }
    }

    public <T extends IHyperCommonModule> T getModuleInCache(int i, T t) {
        return (T) HyperModuleCache.get(i, t);
    }

    public IHyperPhone getHyperPhone(Object... vars) {
        verityParamsType("getHyperPhone", vars, 1, new Class[]{Phone.class});
        Phone phone = (Phone) vars[0];
        HyperPhoneImpl impl = new HyperPhoneImpl(phone);
        HyperModuleCache.set(phone.getPhoneId(), impl);
        return impl;
    }

    public IHyperGsmCdmaPhone getHyperGsmCdmaPhone(Object... vars) {
        verityParamsType("getHyperGsmCdmaPhone", vars, 1, new Class[]{GsmCdmaPhone.class});
        GsmCdmaPhone phone = (GsmCdmaPhone) vars[0];
        HyperGsmCdmaPhoneImpl impl = new HyperGsmCdmaPhoneImpl(phone);
        HyperModuleCache.set(phone.getPhoneId(), impl);
        return impl;
    }

    public IHyperImsPhone getHyperImsPhone(Object... vars) {
        verityParamsType("getHyperImsPhone", vars, 1, new Class[]{ImsPhone.class});
        ImsPhone phone = (ImsPhone) vars[0];
        HyperImsPhoneImpl impl = new HyperImsPhoneImpl(phone);
        HyperModuleCache.set(phone.getPhoneId(), impl);
        return impl;
    }

    public IHyperServiceStateTracker getHyperServiceStateTracker(Object... vars) {
        verityParamsType("getHyperServiceStateTracker", vars, 2, new Class[]{ServiceStateTracker.class, GsmCdmaPhone.class});
        ServiceStateTracker sst = (ServiceStateTracker) vars[0];
        GsmCdmaPhone phone = (GsmCdmaPhone) vars[1];
        HyperServiceStateTrackerImpl impl = new HyperServiceStateTrackerImpl(sst, phone);
        HyperModuleCache.set(phone.getPhoneId(), impl);
        return impl;
    }

    public IHyperGsmCdmaCallTracker getHyperGsmCdmaCallTracker(Object... vars) {
        verityParamsType("getHyperGsmCdmaCallTracker", vars, 1, new Class[]{GsmCdmaCallTracker.class});
        GsmCdmaCallTracker gsmCdmaCallTracker = (GsmCdmaCallTracker) vars[0];
        HyperGsmCdmaCallTrackerImpl impl = new HyperGsmCdmaCallTrackerImpl(gsmCdmaCallTracker);
        HyperModuleCache.set(gsmCdmaCallTracker.getPhone().getPhoneId(), impl);
        return impl;
    }

    public IHyperUiccManager getHyperUiccManager(Object... vars) {
        return HyperUiccManagerImpl.getInstance();
    }

    public IHyperTimeZoneDetector getHyperTimeZoneDetector(Object... vars) {
        verityParamsType("getHyperTimeZoneDetector", vars, 3, new Class[]{Phone.class, TimeZoneSuggester.class, TimeServiceHelper.class});
        Phone phone = (Phone) vars[0];
        int phoneId = phone != null ? phone.getPhoneId() : -1;
        TimeZoneSuggester timeZoneSuggester = (TimeZoneSuggester) vars[1];
        TimeServiceHelper timeServiceHelper = (TimeServiceHelper) vars[2];
        HyperTimeZoneDetectorImpl impl = new HyperTimeZoneDetectorImpl(phone, timeZoneSuggester, timeServiceHelper);
        HyperModuleCache.set(phoneId, impl);
        return impl;
    }

    public IHyperCatManager getHyperCatManager(Object... vars) {
        return HyperCatManagerImpl.getInstance();
    }

    public IHyperCallManager getHyperCallManager(Object... vars) {
        return HyperCallManagerImpl.getInstance();
    }

    public IHyperFrameworkTelephonyManager getHyperFrameworkTelephonyManager(Object... vars) {
        return HyperFrameworkTelephonyManagerImpl.getInstance();
    }

    public IHyperDataManager getHyperDataManager(Object... vars) {
        verityParamsType("getHyperDataManager", vars, 2, new Class[]{DataNetworkController.class, Phone.class});
        DataNetworkController dnc = (DataNetworkController) vars[0];
        Phone phone = (Phone) vars[1];
        int phoneId = phone != null ? phone.getPhoneId() : -1;
        HyperDataManagerImpl impl = new HyperDataManagerImpl(dnc, phone);
        HyperModuleCache.set(phoneId, impl);
        return impl;
    }

    public IHyperSsManager getHyperSsManager(Object... vars) {
        return HyperSsManagerImpl.getInstance();
    }

    public IHyperTeleSmsManager getHyperTeleSmsManager(Object... vars) {
        return HyperTeleSmsManagerImpl.getInstance();
    }

    public IHyperTeleImsManager getHyperTeleImsManager(Object... vars) {
        return HyperTeleImsManagerImpl.getInstance();
    }

    public IHyperNetworkTypeManager getHyperNetworkTypeManager(Object... vars) {
        verityParamsType("getHyperNetworkTypeManager", vars, 2, new Class[]{Phone.class, NetworkTypeController.class});
        Phone phone = (Phone) vars[0];
        NetworkTypeController networkTypeController = (NetworkTypeController) vars[1];
        HyperNetworkTypeManagerImpl impl = new HyperNetworkTypeManagerImpl(phone, networkTypeController);
        HyperModuleCache.set(phone.getPhoneId(), impl);
        return impl;
    }

    public IHyperEmergencyNumberTracker getHyperEmergencyNumberTracker(Object... vars) {
        verityParamsType("getHyperEmergencyNumberTracker", vars, 1, new Class[]{Phone.class});
        Phone phone = (Phone) vars[0];
        HyperEmergencyNumberTrackerImpl impl = new HyperEmergencyNumberTrackerImpl(phone);
        HyperModuleCache.set(phone.getPhoneId(), impl);
        return impl;
    }

    void logd(String s) {
        Rlog.d("HyperTelephonyFactory", s);
    }

    void loge(String s) {
        Rlog.e("HyperTelephonyFactory", s);
    }
}
