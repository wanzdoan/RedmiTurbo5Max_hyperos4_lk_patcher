package com.android.phone;

import android.app.compat.CompatChanges;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyFrameworkInitializer;
import android.telephony.ims.DelegateRequest;
import android.telephony.ims.ImsException;
import android.telephony.ims.RcsContactUceCapability;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsRcsController;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.telephony.ims.aidl.IRcsUceControllerCallback;
import android.telephony.ims.aidl.IRcsUcePublishStateCallback;
import android.telephony.ims.aidl.ISipDelegate;
import android.telephony.ims.aidl.ISipDelegateConnectionStateCallback;
import android.telephony.ims.aidl.ISipDelegateMessageCallback;
import android.util.Log;
import com.android.ims.ImsManager;
import com.android.ims.internal.IImsServiceFeatureCallback;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.IIntegerConsumer;
import com.android.internal.telephony.ISipDialogStateCallback;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.ims.ImsResolver;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.services.telephony.rcs.RcsFeatureController;
import com.android.services.telephony.rcs.SipTransportController;
import com.android.services.telephony.rcs.TelephonyRcsService;
import com.android.services.telephony.rcs.UceControllerManager;
import com.nxp.id.tp.common.HexString$$ExternalSyntheticBUOutline1;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public class ImsRcsController extends IImsRcsController.Stub {
    private static ImsRcsController sInstance;
    private PhoneGlobals mApp;
    private FeatureFlags mFeatureFlags;
    private ImsResolver mImsResolver;
    private PackageManager mPackageManager;
    private TelephonyRcsService mRcsService;
    private Boolean mSingleRegistrationOverride;
    private final int mVendorApiLevel;

    static ImsRcsController init(PhoneGlobals phoneGlobals, FeatureFlags featureFlags) {
        ImsRcsController imsRcsController;
        synchronized (ImsRcsController.class) {
            try {
                if (sInstance != null) {
                    Log.wtf("ImsRcsController", "init() called multiple times!  sInstance = " + sInstance);
                } else {
                    sInstance = new ImsRcsController(phoneGlobals, featureFlags);
                    TelephonyFrameworkInitializer.getTelephonyServiceManager().getTelephonyImsServiceRegisterer().register(sInstance);
                }
                imsRcsController = sInstance;
            } catch (Throwable th) {
                throw th;
            }
        }
        return imsRcsController;
    }

    private ImsRcsController(PhoneGlobals phoneGlobals, FeatureFlags featureFlags) {
        Log.i("ImsRcsController", "ImsRcsController");
        this.mApp = phoneGlobals;
        this.mFeatureFlags = featureFlags;
        this.mPackageManager = phoneGlobals.getPackageManager();
        this.mImsResolver = ImsResolver.getInstance();
        this.mVendorApiLevel = SystemProperties.getInt("ro.vendor.api_level", Build.VERSION.DEVICE_INITIAL_SDK_INT);
    }

    public void registerImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "registerImsRegistrationCallback");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "registerImsRegistrationCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                getRcsFeatureController(i).registerImsRegistrationCallback(i, iImsRegistrationCallback);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                Log.e("ImsRcsController", "registerImsRegistrationCallback: sudId=" + i + ", " + e.getMessage());
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "unregisterImsRegistrationCallback");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "unregisterImsRegistrationCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            getRcsFeatureController(i).unregisterImsRegistrationCallback(i, iImsRegistrationCallback);
        } catch (ServiceSpecificException e) {
            Log.e("ImsRcsController", "unregisterImsRegistrationCallback: error=" + e.errorCode);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void getImsRcsRegistrationState(int i, final IIntegerConsumer iIntegerConsumer) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getImsRcsRegistrationState");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getImsRcsRegistrationState");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            getRcsFeatureController(i).getRegistrationState(new Consumer() { // from class: com.android.phone.ImsRcsController$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ImsRcsController.m980$r8$lambda$Jcozl4XjeU5nWC7kh4mQciFbxk(iIntegerConsumer, (Integer) obj);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$Jcozl4XjeU5nWC7kh4mQ-ciFbxk, reason: not valid java name */
    public static /* synthetic */ void m980$r8$lambda$Jcozl4XjeU5nWC7kh4mQciFbxk(IIntegerConsumer iIntegerConsumer, Integer num) {
        int iIntValue;
        if (num == null) {
            iIntValue = 0;
        } else {
            try {
                iIntValue = num.intValue();
            } catch (RemoteException unused) {
                Log.w("ImsRcsController", "getImsRcsRegistrationState: callback is not available.");
                return;
            }
        }
        iIntegerConsumer.accept(iIntValue);
    }

    public void getImsRcsRegistrationTransportType(int i, final IIntegerConsumer iIntegerConsumer) {
        TelephonyPermissions.enforceCallingOrSelfReadPrecisePhoneStatePermissionOrCarrierPrivilege(this.mApp, i, "getImsRcsRegistrationTransportType");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getImsRcsRegistrationTransportType");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            getRcsFeatureController(i).getRegistrationTech(new Consumer() { // from class: com.android.phone.ImsRcsController$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ImsRcsController.$r8$lambda$l5fVKWpCwXTN1UUtyMBH1xBIuQI(iIntegerConsumer, (Integer) obj);
                }
            });
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public static /* synthetic */ void $r8$lambda$l5fVKWpCwXTN1UUtyMBH1xBIuQI(IIntegerConsumer iIntegerConsumer, Integer num) {
        try {
            iIntegerConsumer.accept(((Integer) RegistrationManager.IMS_REG_TO_ACCESS_TYPE_MAP.get(Integer.valueOf(num == null ? -1 : num.intValue()))).intValue());
        } catch (RemoteException unused) {
            Log.w("ImsRcsController", "getImsRcsRegistrationTransportType: callback is not available.");
        }
    }

    public void registerRcsAvailabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) {
        enforceReadPrivilegedPermission("registerRcsAvailabilityCallback");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "registerRcsAvailabilityCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                getRcsFeatureController(i).registerRcsAvailabilityCallback(i, iImsCapabilityCallback);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                Log.e("ImsRcsController", "registerRcsAvailabilityCallback: sudId=" + i + ", " + e.getMessage());
                throw new ServiceSpecificException(e.getCode());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterRcsAvailabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) {
        enforceReadPrivilegedPermission("unregisterRcsAvailabilityCallback");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "unregisterRcsAvailabilityCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            getRcsFeatureController(i).unregisterRcsAvailabilityCallback(i, iImsCapabilityCallback);
        } catch (ServiceSpecificException e) {
            Log.e("ImsRcsController", "unregisterRcsAvailabilityCallback: error=" + e.errorCode);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isCapable(int i, int i2, int i3) {
        enforceReadPrivilegedPermission("isCapable");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isCapable");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zIsCapable = getRcsFeatureController(i).isCapable(i2, i3);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsCapable;
            } catch (ImsException e) {
                Log.e("ImsRcsController", "isCapable: sudId=" + i + ", capability=" + i2 + ", " + e.getMessage());
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isAvailable(int i, int i2, int i3) {
        enforceReadPrivilegedPermission("isAvailable");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "isAvailable");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                boolean zIsAvailable = getRcsFeatureController(i).isAvailable(i2, i3);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsAvailable;
            } catch (ImsException e) {
                Log.e("ImsRcsController", "isAvailable: sudId=" + i + ", capability=" + i2 + ", " + e.getMessage());
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void requestCapabilities(int i, String str, String str2, List list, IRcsUceControllerCallback iRcsUceControllerCallback) {
        enforceAccessUserCapabilityExchangePermission("requestCapabilities");
        enforceReadContactsPermission("requestCapabilities");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.ims", "requestCapabilities");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
                if (uceControllerManager == null) {
                    throw new ServiceSpecificException(2, "This subscription does not support UCE.");
                }
                uceControllerManager.requestCapabilities(list, iRcsUceControllerCallback);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void requestAvailability(int i, String str, String str2, Uri uri, IRcsUceControllerCallback iRcsUceControllerCallback) {
        enforceAccessUserCapabilityExchangePermission("requestAvailability");
        enforceReadContactsPermission("requestAvailability");
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.ims", "requestAvailability");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
                if (uceControllerManager == null) {
                    throw new ServiceSpecificException(2, "This subscription does not support UCE.");
                }
                uceControllerManager.requestNetworkAvailability(uri, iRcsUceControllerCallback);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public int getUcePublishState(int i) {
        enforceReadPrivilegedPermission("getUcePublishState");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "getUcePublishState");
        int callingUid = Binder.getCallingUid();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
                if (uceControllerManager == null) {
                    throw new ServiceSpecificException(2, "This subscription does not support UCE.");
                }
                int ucePublishState = uceControllerManager.getUcePublishState(CompatChanges.isChangeEnabled(202894742L, callingUid));
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return ucePublishState;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public RcsContactUceCapability addUceRegistrationOverrideShell(int i, Set set) throws ImsException {
        try {
            UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
            if (uceControllerManager == null) {
                return null;
            }
            return uceControllerManager.addUceRegistrationOverride(set);
        } catch (ServiceSpecificException e) {
            throw new ImsException(e.getMessage(), e.errorCode);
        }
    }

    public RcsContactUceCapability removeUceRegistrationOverrideShell(int i, Set set) throws ImsException {
        try {
            UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
            if (uceControllerManager == null) {
                return null;
            }
            return uceControllerManager.removeUceRegistrationOverride(set);
        } catch (ServiceSpecificException e) {
            throw new ImsException(e.getMessage(), e.errorCode);
        }
    }

    public RcsContactUceCapability clearUceRegistrationOverrideShell(int i) throws ImsException {
        try {
            UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
            if (uceControllerManager == null) {
                return null;
            }
            return uceControllerManager.clearUceRegistrationOverride();
        } catch (ServiceSpecificException e) {
            throw new ImsException(e.getMessage(), e.errorCode);
        }
    }

    public RcsContactUceCapability getLatestRcsContactUceCapabilityShell(int i) throws ImsException {
        try {
            UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
            if (uceControllerManager == null) {
                return null;
            }
            return uceControllerManager.getLatestRcsContactUceCapability();
        } catch (ServiceSpecificException e) {
            throw new ImsException(e.getMessage(), e.errorCode);
        }
    }

    public String getLastUcePidfXmlShell(int i) throws ImsException {
        try {
            UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
            if (uceControllerManager == null) {
                return null;
            }
            String lastPidfXml = uceControllerManager.getLastPidfXml();
            return lastPidfXml == null ? "none" : lastPidfXml;
        } catch (ServiceSpecificException e) {
            throw new ImsException(e.getMessage(), e.errorCode);
        }
    }

    public boolean removeUceRequestDisallowedStatus(int i) throws ImsException {
        try {
            UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i, true).getFeature(UceControllerManager.class);
            if (uceControllerManager == null) {
                return false;
            }
            return uceControllerManager.removeUceRequestDisallowedStatus();
        } catch (ServiceSpecificException e) {
            throw new ImsException(e.getMessage(), e.errorCode);
        }
    }

    public boolean setCapabilitiesRequestTimeout(int i, long j) throws ImsException {
        try {
            UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i, true).getFeature(UceControllerManager.class);
            if (uceControllerManager == null) {
                return false;
            }
            return uceControllerManager.setCapabilitiesRequestTimeout(j);
        } catch (ServiceSpecificException e) {
            throw new ImsException(e.getMessage(), e.errorCode);
        }
    }

    public void registerUcePublishStateCallback(int i, IRcsUcePublishStateCallback iRcsUcePublishStateCallback) {
        enforceReadPrivilegedPermission("registerUcePublishStateCallback");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "registerUcePublishStateCallback");
        int callingUid = Binder.getCallingUid();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
                if (uceControllerManager == null) {
                    throw new ServiceSpecificException(2, "This subscription does not support UCE.");
                }
                uceControllerManager.registerPublishStateCallback(iRcsUcePublishStateCallback, CompatChanges.isChangeEnabled(202894742L, callingUid));
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterUcePublishStateCallback(int i, IRcsUcePublishStateCallback iRcsUcePublishStateCallback) {
        enforceReadPrivilegedPermission("unregisterUcePublishStateCallback");
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "unregisterUcePublishStateCallback");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                UceControllerManager uceControllerManager = (UceControllerManager) getRcsFeatureController(i).getFeature(UceControllerManager.class);
                if (uceControllerManager == null) {
                    throw new ServiceSpecificException(2, "This subscription does not support UCE.");
                }
                uceControllerManager.unregisterPublishStateCallback(iRcsUcePublishStateCallback);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            } catch (ServiceSpecificException e) {
                Log.e("ImsRcsController", "unregisterUcePublishStateCallback: error=" + e.errorCode);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isUceSettingEnabled(int i, String str, String str2) {
        if (!TelephonyPermissions.checkCallingOrSelfReadPhoneState(this.mApp, i, str, str2, "isUceSettingEnabled")) {
            Log.w("ImsRcsController", "isUceSettingEnabled: READ_PHONE_STATE app op disabled when accessing isUceSettingEnabled");
            return false;
        }
        enforceTelephonyFeatureWithException(str, "android.hardware.telephony.ims", "isUceSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return SubscriptionManager.getBooleanSubscriptionProperty(i, "ims_rcs_uce_enabled", false, this.mApp);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void setUceSettingEnabled(int i, boolean z) {
        enforceModifyPermission();
        enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims", "setUceSettingEnabled");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SubscriptionManager.setSubscriptionProperty(i, "ims_rcs_uce_enabled", z ? "1" : "0");
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public boolean isSipDelegateSupported(int i) {
        TelephonyPermissions.enforceAnyPermissionGranted(this.mApp, Binder.getCallingUid(), "isSipDelegateSupported", new String[]{"android.permission.PERFORM_IMS_SINGLE_REGISTRATION", "android.permission.READ_PRIVILEGED_PHONE_STATE"});
        if (!isImsSingleRegistrationSupportedOnDevice()) {
            return false;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            try {
                SipTransportController sipTransportController = (SipTransportController) getRcsFeatureController(i).getFeature(SipTransportController.class);
                if (sipTransportController == null) {
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                    return false;
                }
                boolean zIsSupported = sipTransportController.isSupported(i);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return zIsSupported;
            } catch (ImsException e) {
                throw new ServiceSpecificException(e.getCode(), e.getMessage());
            } catch (ServiceSpecificException e2) {
                if (e2.errorCode != 2) {
                    throw e2;
                }
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return false;
            }
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    public void createSipDelegate(int i, DelegateRequest delegateRequest, String str, ISipDelegateConnectionStateCallback iSipDelegateConnectionStateCallback, ISipDelegateMessageCallback iSipDelegateMessageCallback) throws ServiceSpecificException {
        boolean zIsSystem;
        enforceImsSingleRegistrationPermission("createSipDelegate");
        if (!isImsSingleRegistrationSupportedOnDevice()) {
            throw new ServiceSpecificException(2, "SipDelegate creation is only supported for devices supporting IMS single registration");
        }
        UserHandle mainUser = ((UserManager) this.mApp.getApplicationContext().getSystemService(UserManager.class)).getMainUser();
        if (mainUser != null) {
            zIsSystem = Binder.getCallingUserHandle().equals(mainUser);
        } else {
            zIsSystem = Binder.getCallingUserHandle().isSystem();
        }
        if (!zIsSystem) {
            throw new ServiceSpecificException(2, "SipDelegate creation is only available to primary user.");
        }
        try {
            if (!PccAwareUidComparator.isSameApp(this.mApp.getPackageManager(), Binder.getCallingUid(), this.mApp.getPackageManager().getPackageUidAsUser(str, 0, Binder.getCallingUserHandle().getIdentifier()))) {
                throw new SecurityException("passed in packageName does not match the caller");
            }
            int callingUid = Binder.getCallingUid();
            long jClearCallingIdentity = Binder.clearCallingIdentity();
            SipTransportController sipTransportController = (SipTransportController) getRcsFeatureController(i).getFeature(SipTransportController.class);
            if (sipTransportController == null) {
                throw new ServiceSpecificException(2, "This subscription does not support the creation of SIP delegates");
            }
            try {
                try {
                    sipTransportController.createSipDelegate(i, callingUid, delegateRequest, str, iSipDelegateConnectionStateCallback, iSipDelegateMessageCallback);
                    Binder.restoreCallingIdentity(jClearCallingIdentity);
                } catch (ImsException e) {
                    throw new ServiceSpecificException(e.getCode(), e.getMessage());
                }
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        } catch (PackageManager.NameNotFoundException unused) {
            ImsRcsController$$ExternalSyntheticBUOutline0.m("Passed in PackageName can not be found on device");
        }
    }

    public void destroySipDelegate(int i, ISipDelegate iSipDelegate, int i2) {
        if (iSipDelegate == null) {
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SipTransportController sipTransportController = (SipTransportController) getRcsFeatureController(i).getFeature(SipTransportController.class);
            if (sipTransportController == null) {
                return;
            }
            sipTransportController.destroySipDelegate(i, iSipDelegate, i2);
        } catch (ServiceSpecificException e) {
            Log.e("ImsRcsController", "destroySipDelegate: error=" + e.errorCode);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    public void triggerNetworkRegistration(int i, ISipDelegate iSipDelegate, int i2, String str) {
        enforceImsSingleRegistrationPermission("triggerNetworkRegistration");
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            SipTransportController sipTransportController = (SipTransportController) getRcsFeatureController(i).getFeature(SipTransportController.class);
            if (sipTransportController == null) {
                return;
            }
            sipTransportController.triggerFullNetworkRegistration(i, iSipDelegate, i2, str);
            return;
        } catch (ServiceSpecificException e) {
            Log.e("ImsRcsController", "triggerNetworkRegistration: error=" + e.errorCode);
            return;
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
        Binder.restoreCallingIdentity(jClearCallingIdentity);
    }

    public void registerSipDialogStateCallback(int i, ISipDialogStateCallback iSipDialogStateCallback) {
        enforceReadPrivilegedPermission("registerSipDialogStateCallback");
        if (iSipDialogStateCallback == null) {
            Folme$$ExternalSyntheticBUOutline0.m("SipDialogStateCallback is null");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims.singlereg", "registerSipDialogStateCallback");
            try {
                SipTransportController sipTransportController = (SipTransportController) getRcsFeatureController(i).getFeature(SipTransportController.class);
                if (sipTransportController == null) {
                    throw new ServiceSpecificException(1, "This transport does not support the registerSipDialogStateCallback of SIP delegates");
                }
                sipTransportController.addCallbackForSipDialogState(i, iSipDialogStateCallback);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
    }

    public void unregisterSipDialogStateCallback(int i, ISipDialogStateCallback iSipDialogStateCallback) {
        enforceReadPrivilegedPermission("unregisterSipDialogStateCallback");
        if (iSipDialogStateCallback == null) {
            Folme$$ExternalSyntheticBUOutline0.m("SipDialogStateCallback is null");
            return;
        }
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            enforceTelephonyFeatureWithException(getCurrentPackageName(), "android.hardware.telephony.ims.singlereg", "unregisterSipDialogStateCallback");
            try {
                SipTransportController sipTransportController = (SipTransportController) getRcsFeatureController(i).getFeature(SipTransportController.class);
                if (sipTransportController == null) {
                    throw new ServiceSpecificException(1, "This transport does not support the unregisterSipDialogStateCallback of SIP delegates");
                }
                sipTransportController.removeCallbackForSipDialogState(i, iSipDialogStateCallback);
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                return;
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(jClearCallingIdentity);
                throw th;
            }
        }
        HexString$$ExternalSyntheticBUOutline1.m("Invalid Subscription ID: ", i);
    }

    public void registerRcsFeatureCallback(int i, IImsServiceFeatureCallback iImsServiceFeatureCallback) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver == null) {
                throw new ServiceSpecificException(2, "Device does not support IMS");
            }
            imsResolver.listenForFeature(i, 2, iImsServiceFeatureCallback);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public void unregisterImsFeatureCallback(IImsServiceFeatureCallback iImsServiceFeatureCallback) {
        enforceModifyPermission();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            ImsResolver imsResolver = this.mImsResolver;
            if (imsResolver == null) {
                return;
            }
            imsResolver.unregisterImsFeatureCallback(iImsServiceFeatureCallback);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    private void enforceReadPrivilegedPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", str);
    }

    private void enforceImsSingleRegistrationPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.PERFORM_IMS_SINGLE_REGISTRATION", str);
    }

    private void enforceModifyPermission() {
        this.mApp.enforceCallingOrSelfPermission("android.permission.MODIFY_PHONE_STATE", null);
    }

    private void enforceAccessUserCapabilityExchangePermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.ACCESS_RCS_USER_CAPABILITY_EXCHANGE", str);
    }

    private void enforceReadContactsPermission(String str) {
        this.mApp.enforceCallingOrSelfPermission("android.permission.READ_CONTACTS", str);
    }

    private RcsFeatureController getRcsFeatureController(int i) {
        return getRcsFeatureController(i, false);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private RcsFeatureController getRcsFeatureController(int i, boolean z) throws ServiceSpecificException {
        if (!ImsManager.isImsSupportedOnDevice(this.mApp)) {
            throw new ServiceSpecificException(2, "IMS is not available on device.");
        }
        if (this.mRcsService == null) {
            throw new ServiceSpecificException(2, "IMS is not available on device.");
        }
        Phone phone = PhoneGlobals.getPhone(i);
        if (phone == null) {
            throw new ServiceSpecificException(3, "Invalid subscription Id: " + i);
        }
        int phoneId = phone.getPhoneId();
        if (!z) {
            verifyImsRcsConfiguredOrThrow(phoneId);
            verifyRcsSubIdActiveOrThrow(phoneId, i);
        }
        RcsFeatureController featureController = this.mRcsService.getFeatureController(phoneId);
        if (featureController == null) {
            throw new ServiceSpecificException(2, "The requested operation is not supported for subId " + i);
        }
        if (z || featureController.getAssociatedSubId() == i) {
            return featureController;
        }
        Log.w("ImsRcsController", "getRcsFeatureController: service unavailable on slot " + phoneId + " for subId " + i);
        throw new ServiceSpecificException(1, "The ImsService is not currently available for subid " + i + ", please try again");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private void verifyRcsSubIdActiveOrThrow(int i, int i2) throws ServiceSpecificException {
        if (this.mRcsService.verifyActiveSubId(i, i2)) {
            return;
        }
        Log.w("ImsRcsController", "verifyRcsSubIdActiveOrThrow: verify failed, service not set up yet on slot " + i + " for subId " + i2);
        throw new ServiceSpecificException(1, "ImsService set up in progress for subId " + i2 + ", please try again");
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ServiceSpecificException */
    private void verifyImsRcsConfiguredOrThrow(int i) throws ServiceSpecificException {
        ImsResolver imsResolver = this.mImsResolver;
        if (imsResolver == null || !imsResolver.isImsServiceConfiguredForFeature(i, 2)) {
            throw new ServiceSpecificException(2, "This subscription does not support RCS");
        }
    }

    private boolean isImsSingleRegistrationSupportedOnDevice() {
        Boolean bool = this.mSingleRegistrationOverride;
        return bool != null ? bool.booleanValue() : this.mApp.getPackageManager().hasSystemFeature("android.hardware.telephony.ims.singlereg");
    }

    private String getCurrentPackageName() {
        String[] packagesForUid;
        PackageManager packageManager = this.mApp.getBaseContext().createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager();
        if (packageManager == null || (packagesForUid = packageManager.getPackagesForUid(Binder.getCallingUid())) == null) {
            return null;
        }
        return packagesForUid[0];
    }

    private void enforceTelephonyFeatureWithException(String str, String str2, String str3) {
        TelephonyUtils.enforceTelephonyFeatureWithException(str, this.mPackageManager, this.mVendorApiLevel, str2, str3);
    }

    void setRcsService(TelephonyRcsService telephonyRcsService) {
        this.mRcsService = telephonyRcsService;
    }

    void setDeviceSingleRegistrationSupportOverride(Boolean bool) {
        this.mSingleRegistrationOverride = bool;
    }
}
